/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkgfinal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
/**
 *
 * @author 17248
 */
public class Rooms 
{
    public void listRooms(Connection conn) throws SQLException
    {
        Statement stat =conn.createStatement();
        ResultSet result;
        result = stat.executeQuery("select * from ROOMS");
           
           ResultSetMetaData Meta = result.getMetaData();
           

           
           for(int j=1; j<=Meta.getColumnCount(); j++)
           {
               System.out.print(Meta.getColumnName(j)+"\t");
           }
           
           System.out.println();
           while(result.next())
            {
                System.out.println(result.getString(1)+"\t"+result.getInt(2));
                
            }
    }
    
    public String closestToSeats(int seats,Connection conn) throws SQLException
    {
        int best=0;
        PreparedStatement pstat =conn.prepareStatement("Select * from ROOMS where SEATS >= ?");
        pstat.setInt(1, seats);
        
        ResultSet result = pstat.executeQuery();
        
        while(result.next())
        {
            if(best == 0)
            {
                best = result.getInt(2);
            }
            else if(best >= result.getInt(2))
            {
                best = result.getInt(2);
            }
        }
        pstat =conn.prepareStatement("Select * from ROOMS where seats = ?");
        pstat.setInt(1, best);
        result = pstat.executeQuery();
        if(result.next())
            return result.getString(1);
        else
            return "0";
    }
    
    public void dropRoom(Connection conn, String room) throws SQLException
    {
        Reservations r = new Reservations();
        int i = 1;
        PreparedStatement pstat =conn.prepareStatement("Select * from RESERVATIONS where RMNO = ?");
        pstat.setString(1, room);
        ResultSet result = pstat.executeQuery();

        while(result.next())
        {
            String name = result.getString("FACULTYNAME");
            String date = result.getString("DATES");
            
            pstat =conn.prepareStatement("Delete from Reservations where RMNO = ? and FACULTYNAME =? and DATES =?");
            pstat.setString(1, room);
            pstat.setString(2,name);
            pstat.setString(3,date);
            pstat.executeUpdate();
            
            pstat =conn.prepareStatement("Select * from Rooms where RMNO = ?");
            pstat.setString(1, room);
            ResultSet nresult = pstat.executeQuery();
            nresult.next();
            boolean added =r.replaceAReservation(name,date,nresult.getInt(2),conn);
            
            if(added)
            {
                
            }
            
            else
            {
                WaitList wl = new WaitList();
                wl.AddToWaitList(name, date, nresult.getInt(2), conn);
            }
        }
        
        pstat =conn.prepareStatement("DELETE from ROOMS where RMNO = ?");
        pstat.setString(1, room);
        pstat.executeUpdate();
        
    }
    
    public void addRoomToDatabase(String roomName, int seats, Connection conn) throws SQLException{
        
        Statement stat = conn.createStatement();
        
        PreparedStatement pstat = conn.prepareStatement("INSERT INTO Rooms values (?, ?)");
        pstat.setString(1, roomName);
        pstat.setInt(2, seats);
        pstat.executeUpdate();
        
        ResultSet result = stat.executeQuery("SELECT * FROM availableDates");
        
        while(result.next())
        {
            //get the waitlist where date =?  result.getString(1)
            pstat = conn.prepareStatement("select * from waitlist where date =?");
            pstat.setString(1, result.getString(1));
            
            ResultSet nresult = pstat.executeQuery();
            
            while(nresult.next()){
                if(nresult.getInt(4) <= seats){
                    Reservations reservations = new Reservations();
                    WaitList waitlist = new WaitList();
                    
                    reservations.makeAReservation(result.getString(2), result.getString(1), result.getInt(3), conn);
                    waitlist.removeFromWaitlist(result.getString(2), result.getString(1), conn);
                    
                    
                }
            }
        }
        
    }
}
