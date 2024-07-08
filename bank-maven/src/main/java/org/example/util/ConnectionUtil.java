package org.example.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Properties;
public class ConnectionUtil {

    static Connection conn;
    static boolean connected = false;

    static void init() throws SQLException, IOException, ClassNotFoundException {

        Class.forName("org.postgresql.Driver");

        InputStream inputStream = ConnectionUtil.class.getClassLoader().getResourceAsStream("application.properties");
        Properties props = new Properties();
        props.load(inputStream);

        conn = DriverManager.getConnection(props.getProperty("url"),props.getProperty("username"),props.getProperty("password"));

        connected = true;
    }
    public static Connection getConnection() throws SQLException, IOException, ClassNotFoundException {

        if (!connected) init();

        return conn;
    }


}