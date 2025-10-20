package com.sanglink.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.Map;

public class JpaBootstrapListener implements ServletContextListener {

    private static EntityManagerFactory emf;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Initializing JPA...");

        Dotenv dotenv = Dotenv.configure()
                .directory(System.getProperty("/home/ahmed/sanglink"))//user.dir
                .ignoreIfMissing()
                .load();

        Map<String, String> props = new HashMap<>();
        props.put("jakarta.persistence.jdbc.driver", dotenv.get("JDBC_DRIVER"));
        props.put("jakarta.persistence.jdbc.url", dotenv.get("DB_URL"));
        props.put("jakarta.persistence.jdbc.user", dotenv.get("DB_USER"));
        props.put("jakarta.persistence.jdbc.password", dotenv.get("DB_PASS"));
        props.put("hibernate.dialect", dotenv.get("HIBERNATE_DIALECT"));

        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.format_sql", "true");
        props.put("hibernate.archive.autodetection", "class");
        props.put("javax.persistence.schema-generation.database.action", "update");

        emf = Persistence.createEntityManagerFactory("myPU", props);
        sce.getServletContext().setAttribute("emf", emf);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("JPA closed");
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
}
