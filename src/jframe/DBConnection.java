/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jframe;
import java.sql.*;
/**
 *
 * @author SONI
 */
public class DBConnection {
    static Connection con= null;
    
    public static Connection getConnection(){
        String url="jdbc:mysql://localhost:3310/expensetracker";
        String user="root";
        String pass="root";
    
     try{
        con=DriverManager.getConnection(url,user,pass);
     }
       catch (SQLException e)
       {
           System.out.println(e);
       }
     return con;
    }
}
