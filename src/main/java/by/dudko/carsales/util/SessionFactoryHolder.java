package by.dudko.carsales.util;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@Log4j2
@UtilityClass
public class SessionFactoryHolder {
    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
            configuration.configure("hibernate.cfg.xml");
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            log.fatal("Failed to load session factory", e);
            throw new ExceptionInInitializerError(e);
        }

    }

    public static void checkFactoryInitialization() {
        if (sessionFactory == null) {
            throw new ExceptionInInitializerError("Session factory is null");
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        sessionFactory.close();
    }
}
