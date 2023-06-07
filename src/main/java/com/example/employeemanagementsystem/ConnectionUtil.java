package com.example.employeemanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionUtil {
    // Declare JDBC Driver
    static String JDBC_DRIVER = "org.postgresql.Driver";
    static String connStr = "jdbc:postgresql://db.ywhttjyplrvxytezzazx.supabase.co:5432/postgres";

    public static Connection connect() {
        Connection connect = null;

        try {
            Class.forName(JDBC_DRIVER);
            connect = DriverManager.getConnection(connStr , "jjohnson0204", "Pixel071421!!!");
            if (connect != null) {
                System.out.println("Connection Established");
            } else {
                System.out.println("Connection Failed");
            }
            return connect;
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static void writeToDatabase(String user, String pass) {
        String url = "jdbc:postgresql://db.ywhttjyplrvxytezzazx.supabase.co:5432/postgres";
        String username = user;
        String password = pass;

        String query = "INSERT INTO admins(username, password) VALUES(?, ?)";

        try (Connection con = DriverManager.getConnection(url, "jjohnson0204", "Pixel071421!!!")){
        PreparedStatement pst = con.prepareStatement(query);

        pst.setString(1, username);
        pst.setString(2, password);
        pst.executeUpdate();
            System.out.println("Successfully created.");
        }
        catch (SQLException ex) {

        }
    }
}
