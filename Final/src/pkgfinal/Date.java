/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkgfinal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * @author Minec
 */
public class Date {
    
    public void printDates(Connection conn) throws SQLException{
        
        Statement stat = conn.createStatement();
        ResultSet result;
        result = stat.executeQuery("SELECT * FROM AvailableDates");
        
        ResultSetMetaData Meta = result.getMetaData();

        
        while(result.next()){
            System.out.println(result.getString(1)+"\t");
            
        }
        
    }
    
    public void addDateToDatabase(String date, Connection conn) throws SQLException
    {
        PreparedStatement pstat = conn.prepareStatement("INSERT into availableDates values (?)");
        pstat.setString(1, date);
        pstat.executeUpdate();
    }
    
}

