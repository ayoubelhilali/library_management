package com.library.patterns.singleton;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static DBConnection instance;
    private Connection connection;
    private DBConnection() {
        try{
            Properties prop = new Properties();
            InputStream input=DBConnection.class.getClassLoader().getResourceAsStream("db.properties");
            if(input==null) {
                throw new RuntimeException("Properties file not found!");
            }
            prop.load(input);

            String driver = prop.getProperty("db.driver");
            String url = prop.getProperty("db.url");
            String username = prop.getProperty("db.username");
            String password = prop.getProperty("db.password");

            Class.forName(driver);

            connection=DriverManager.getConnection(url,username,password);
            System.out.println("Connected to database successfully");
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }
    public Connection getConnection() {
        return connection;
    }
}