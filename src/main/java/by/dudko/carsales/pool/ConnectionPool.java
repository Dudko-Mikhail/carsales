package by.dudko.carsales.pool;

import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

@Log4j2
public class ConnectionPool {
    private static final int DEFAULT_POOL_SIZE = 10;
    private static final int CREATION_ATTEMPTS = 3;
    private static final String DRIVER;
    private static final String URL;
    private static final String USER_NAME;
    private static final String PASSWORD;
    private static final int POOL_SIZE;
    private static final ReentrantLock lock;
    private static final AtomicBoolean isCreated;
    private static ConnectionPool instance;
    private final BlockingQueue<ProxyConnection> freeConnections;
    private final BlockingQueue<ProxyConnection> givenAwayConnections;

    static {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("config/database");
            USER_NAME = bundle.getString("db.userName");
            PASSWORD = bundle.getString("db.password");
            DRIVER = bundle.getString("db.driver");
            URL = bundle.getString("db.url");
            POOL_SIZE = bundle.containsKey("db.poolSize")
                    ? Integer.parseInt(bundle.getString("db.poolSize"))
                    : DEFAULT_POOL_SIZE;
        } catch (MissingResourceException e) {
            log.fatal("Failed to find pool configuration file", e);
            throw new ExceptionInInitializerError(e);
        }
        lock = new ReentrantLock();
        isCreated = new AtomicBoolean();
    }

    public static ConnectionPool getInstance() {
        if (!isCreated.get()) {
            try {
                lock.lock();
                if (instance == null) {
                    instance = new ConnectionPool();
                    isCreated.set(true);
                }
            }
            finally {
                lock.unlock();
            }
        }
        return instance;
    }

    private ConnectionPool() {
        freeConnections = new LinkedBlockingDeque<>(POOL_SIZE);
        givenAwayConnections = new LinkedBlockingDeque<>(POOL_SIZE);

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            var error = new ExceptionInInitializerError(e);
            log.error("Failed to register driver", e);
            throw error;
        }

        init();
        log.info("Connection pool has been successfully initialized");
    }

    private void init() {
        int i = 0;
        while (POOL_SIZE != freeConnections.size() && i < CREATION_ATTEMPTS) {
            createConnections();
            i++;
        }
        if (freeConnections.size() < POOL_SIZE) {
            var error = new ExceptionInInitializerError(
                    String.format("Insufficient number of connections in the pool %d. Required number is %d",
                            freeConnections.size(), POOL_SIZE)
            );
            log.fatal("Pool initialization error", error);
            throw error;
        }
    }

    private void createConnections() {
        log.info("Attempt to create pool connections");
        for (int i = freeConnections.size(); i < POOL_SIZE; i++) {
            try {
                var connection = new ProxyConnection(DriverManager.getConnection(URL, USER_NAME, PASSWORD));
                freeConnections.add(connection);
            } catch (SQLException e) {
                log.error("Failed to create connection", e);
            }
        }
    }

    public Connection takeConnection() {
        ProxyConnection connection = null;
        try {
            connection = freeConnections.take();
            givenAwayConnections.put(connection);
        } catch (InterruptedException e) {
            log.error("Current thread has been interrupted", e);
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    void releaseConnection(ProxyConnection connection) {
        if (!givenAwayConnections.remove(connection)) {
            log.warn("Attempt to add an illegal connection to the pool");
            return;
        }
        freeConnections.offer(connection);
    }

    public void destroyPool() {
        for (int i = 0; i < POOL_SIZE; i++) {
            try {
                freeConnections.take().finalClose();
            } catch (InterruptedException e) {
                log.error("Current thread has been interrupted", e);
                Thread.currentThread().interrupt();
            } catch (SQLException e) {
                log.warn("Connection closing issues", e);
            }
        }
        deregisterDrivers();
        log.info("Connection pool has been destroyed");
    }

    private void deregisterDrivers() {
        DriverManager.drivers().forEach(driver -> {
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                log.warn(String.format("Failed to deregister driver %s", driver), e);
            }
        });
    }
}