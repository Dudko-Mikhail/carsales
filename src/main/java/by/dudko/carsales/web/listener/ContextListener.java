package by.dudko.carsales.web.listener;

import by.dudko.carsales.util.SessionFactoryHolder;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        SessionFactoryHolder.checkFactoryInitialization();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        SessionFactoryHolder.closeSessionFactory();
    }
}
