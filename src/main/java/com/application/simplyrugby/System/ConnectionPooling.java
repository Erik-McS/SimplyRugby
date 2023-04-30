package com.application.simplyrugby.System;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Custom connection pooling class to manage and solve connection leaks encountered during the development.<br>
 * DCBP2 was too messy to install, trying the HIKARICP api to use connection pooling
 * Resource: <a href="https://coderstea.in/post/java/jdbc-connection-pooling-explained-with-hikaricp/">HikariCP</a><br>
 */
public class ConnectionPooling {
    // variable holding the path to the database.
    private static final String DBURL="JDBC:sqlite:SimplyRugbyDB.db";
    // the Hikari datasource, it contains the connection pool.
    private static HikariDataSource dataSource;

    /**
     * Function used to get a connection from the pool.<br>
     * if the Datasource does not exist, it creates one, set the config to access the database<br>
     * and return the datasource/connection.
     * @return The dataSource/connection
     */
    public static HikariDataSource getDataSource(){
        if (dataSource==null){
            HikariConfig config=new HikariConfig();
            config.setJdbcUrl(DBURL);
            config.setMaximumPoolSize(100);
            dataSource=new HikariDataSource(config);
        }
        return dataSource;
    }

    /**
     * This function will close the datasource and the connection pool.<br>
     * it should be used when exiting the application.
      */
    public static void closeDataSource(){
        dataSource.close();
    }

    /**
     * Function to reset the datasource and the pool.<br>
     * only used for debugging purposes.
     */
    public static void resetDatasource(){
        if (dataSource != null) {
            dataSource.close();
            initializeDataSource();
        }
    }
    // function to open a new datasource.
    private static void initializeDataSource(){
        dataSource=new HikariDataSource();
        dataSource.setJdbcUrl(DBURL);
    }
}
