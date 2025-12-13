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
import java.util.Scanner;
/**
 *
 * @author Minec
 */
public class Faculty {
    public void addFacultyMember(String lname, String fname, String deptName,Connection conn) throws SQLException
    {
        
        
        PreparedStatement pstat = conn.prepareStatement("INSERT INTO Faculty VALUES(?, ?, ?)");
        pstat.setString(1, lname);
        pstat.setString(2, fname);
        pstat.setString(3, deptName);
        pstat.executeUpdate();
    }
    
    public void printFacultyMembers(Connection conn) throws SQLException
    {
                
        Statement stat = conn.createStatement();
        ResultSet result;
        result = stat.executeQuery("SELECT * FROM Faculty");
        
        ResultSetMetaData Meta = result.getMetaData();
        
        for(int i=1; i<=Meta.getColumnCount(); i++){
            System.out.print(Meta.getColumnName(i)+"\t");
        }
        System.out.println();
        
        while(result.next()){
            System.out.println(result.getString(1)+"\t\t"+result.getString(2)+"\t"+result.getString(3));
        }
        result.close();
    }
    public void statusFaculty(String FACULTYNAME, Connection conn)throws SQLException {
        

	Statement stat = conn.createStatement();	
        ResultSet result = stat.executeQuery("select * from AvailableDates");
        ResultSet nresult;
		while(result.next()){
			
			PreparedStatement pstat = conn.prepareStatement("SELECT * FROM RESERVATIONS WHERE FACULTYNAME = ? ");
			pstat.setString(1, FACULTYNAME);
			nresult = pstat.executeQuery();
		
			while(nresult.next()){
				System.out.println(nresult.getString(1)+"\t"+nresult.getString(2)+"\t"+nresult.getString(3));
			   }
				
		}
 
		//waitlist
        result = stat.executeQuery("select * from AvailableDates");
		while(result.next()){
			
			PreparedStatement pstat = conn.prepareStatement("SELECT * FROM WAITLIST WHERE FACULTYNAME = ?");
			pstat.setString(1, FACULTYNAME);
			nresult = pstat.executeQuery();
		
			while(nresult.next()){
				System.out.println(nresult.getString(1)+"\t"+nresult.getString(2)+"\t"+nresult.getString(3));
			   }
				
		}
		
		
        
        
    }
    

}
