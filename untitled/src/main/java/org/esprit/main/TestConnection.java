package org.esprit.main;

import org.esprit.utils.DataBaseLink;
import java.sql.Connection;
import java.util.List;

public class TestConnection {
    public static void main(String[] args) {
        Connection conn = DataBaseLink.getInstance().getConnection();

        if (conn != null) {
            System.out.println("Connected to MySQL successfully!");
        } else {
            System.out.println("Connection failed!");
        }
    }
}