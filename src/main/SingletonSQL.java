package main;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SingletonSQL {
    private static SingletonSQL instance = null;
    Connection conn=null;
    SingletonSQL(){}
    
    public static SingletonSQL access(){
        if (SingletonSQL.instance==null){
            return instance = new SingletonSQL();
        }
        return instance;
    }
    public int createConnection(String url, String login, String password){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, login, password);
            return 1;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(SingletonSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
            return 0;}
    public synchronized ResultSet exequteQuery(String query){
        if (conn==null){
            System.out.println("No connection");
            return null;
        }
        try (PreparedStatement p = conn.prepareStatement(query)){
            try (ResultSet result = p.executeQuery()){
                return result;
        }} catch (SQLException ex) {
            System.out.println("Error in query" + ex.getMessage());
        }
        return null;
    }
    public synchronized int executeUpdate(String query){
        if (conn==null){
            System.out.println("No connection");
            return 0;
        }
        try
            (PreparedStatement p = conn.prepareStatement(query)){
            p.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error in query" + ex.getMessage());
        }
        return 0;
    }
}

