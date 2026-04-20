/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package databaseAccess;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {


    private static Connection conn;
    private static final String URL = "jdbc:sqlite:app.db";

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection(URL);
            }
        } catch (Exception e) {
            System.out.println("DB Error: " + e.getMessage());
        }
        return conn;
    }
}
