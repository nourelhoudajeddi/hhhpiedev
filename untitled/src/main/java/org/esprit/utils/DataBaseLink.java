package org.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Singleton Design Pattern
public class DataBaseLink {

    private final String URL = "jdbc:mysql://localhost:3306/base23";
    private final String USER = "root";
    private final String PASS = "";
    private Connection connection;
    private static DataBaseLink instance;

    private DataBaseLink(){
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connection established");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static DataBaseLink getInstance(){
        if(instance == null)
            instance = new DataBaseLink();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}