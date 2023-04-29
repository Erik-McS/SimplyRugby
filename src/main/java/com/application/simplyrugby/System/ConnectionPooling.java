package com.application.simplyrugby.System;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Trying to use connection pooling to solve that locked database issue.
 * DCBP2 was too messy to install, trying the HIKARICP api to use connections pooling
 * Resource: <a href="https://coderstea.in/post/java/jdbc-connection-pooling-explained-with-hikaricp/">HikariCP</a>
 */
public class ConnectionPooling {

    public static final String DBURL="JDBC:sqlite:SimplyRugbyDB.db";

    private static HikariDataSource dataSource;

    public static HikariDataSource getDataSource(){
        if (dataSource==null){
            HikariConfig config=new HikariConfig();
            config.setJdbcUrl(DBURL);
            config.setMaximumPoolSize(10);
            dataSource=new HikariDataSource(config);
        }
        return dataSource;
    }

    public static void closeDataSource(){
        dataSource.close();
    }
}
