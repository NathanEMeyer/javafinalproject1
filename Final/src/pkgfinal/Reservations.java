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
public class Reservations 
{
    public void ReservationsbyDate(Connection conn) throws SQLException
    {
        Statement stat =conn.createStatement();
        ResultSet result;
        result = stat.executeQuery("select * from RESERVATIONS");
           
           ResultSetMetaData Meta = result.getMetaData();
           

           
           for(int j=1; j<=Meta.getColumnCount(); j++)
           {
               System.out.print(Meta.getColumnName(j)+"\t");
           }
           
           System.out.println();
           result = stat.executeQuery("select * from AvailableDates");
           int i =1;
           while(result.next())
           {
                ResultSet nresult;

                PreparedStatement pstat =conn.prepareStatement("Select * from RESERVATIONS where Dates = ?");
                pstat.setString(1, result.getString(1));

                nresult = pstat.executeQuery();
                if(nresult.next())
                    do
                    {
                        System.out.println(nresult.getString(1)+"\t"+nresult.getString(2)+"\t\t"+nresult.getString(3));

                    }while(nresult.next());
                
                i++;
           }
    }
    public boolean makeAReservation(String name, String date, int seats,Connection conn) throws SQLException
    {
        Rooms R = new Rooms();
        String Room = R.closestToSeats(seats, conn);
        int s = seats;
        boolean onList = false;
        
        if(Room.equals("0"))
        {
            WaitList WL = new WaitList();
            WL.AddToWaitList(name, date,seats, conn);
            return false;
        }
        
        
        PreparedStatement pstat =conn.prepareStatement("Select * from AvailableDates where Dates = ?");
        pstat.setString(1,date);
        
        ResultSet result = pstat.executeQuery();
        
        if(result.next())
        {
            onList = true;
        }
        
        if(onList)
        {
            
        pstat =conn.prepareStatement("Select * from RESERVATIONS where Dates = ? and FACULTYNAME = ?");
        pstat.setString(1,date);
        pstat.setString(2,name);
        
        
        result = pstat.executeQuery();

        if(!result.next())
        {
            boolean isroom = false;
            pstat =conn.prepareStatement("Select * from RESERVATIONS where Dates = ? and RMNO = ?");
            pstat.setString(1,date);
            pstat.setString(2,Room);


            result = pstat.executeQuery();
            
            if(!result.next())
            {   
                pstat =conn.prepareStatement("INSERT INTO RESERVATIONS VALUES(?,?,?)");
                pstat.setString(3,date);
                pstat.setString(2,name);
                pstat.setString(1,Room);
                pstat.execute();
                return true;

            }
            else 
            {
                do
                {
                    
                    Room = R.closestToSeats(s, conn);
                    while(Room.equals(R.closestToSeats(s, conn)))
                    {
                        s++;
                    }
                    Room = R.closestToSeats(s, conn);
                    pstat =conn.prepareStatement("Select * from RESERVATIONS where Dates = ? and RMNO = ?");
                    pstat.setString(1,date);
                    pstat.setString(2,Room);
                    result = pstat.executeQuery();
                        


                }while(result.next());
            }

                if(Room.equals("0"))
                {
                    WaitList WL = new WaitList();
                    WL.AddToWaitList(name, date,seats, conn);
                        
                    return false;
                }
                    pstat =conn.prepareStatement("INSERT INTO RESERVATIONS VALUES(?,?,?)");
                    pstat.setString(3,date);
                    pstat.setString(2,name);
                    pstat.setString(1,Room);
                    pstat.execute();
                    return true;
                

            }
            //check if the room is avaliable on that date
            //if no then go back and check for higher seat counts until you can't find one or you find one
            //if you find a room on that date then add it to the reservation list in the spot with the other dates like it.

            }
        
        return false;
    }
    public boolean cancelReservation(String name,String date,Connection conn) throws SQLException
    {
        Rooms R = new Rooms();
        String roomFromR;
        PreparedStatement pstat =conn.prepareStatement("Select * from RESERVATIONS where dates = ? and FACULTYNAME = ?");
        pstat.setString(1,date);
        pstat.setString(2,name);
        ResultSet result = pstat.executeQuery();
        if(result.next())
        {
            roomFromR = result.getString("RMNO");
        }
        else
            return false;
        
        pstat = conn.prepareStatement("DELETE from RESERVATIONS where FACULTYNAME = ? and dates = ?");
        pstat.setString(2,date);
        pstat.setString(1,name);
        
        pstat = conn.prepareStatement("Select * from WAITLIST where dates = ?");
        pstat.setString(1, date);
        
        result = pstat.executeQuery();
        
        if(result.next())
        {
            
            do
            {
                pstat = conn.prepareStatement("Select * from Rooms where seats >= ? and RMNO = ?");
                pstat.setInt(1, result.getInt("seats"));
                pstat.setString(2,roomFromR);
                
                ResultSet nresult = pstat.executeQuery();
                
                if(nresult.next())
                {
                    if(makeAReservation(result.getString("FACULTYNAME"), date, nresult.getInt(2),conn))
                    {
                        WaitList WL = new WaitList();
                        WL.removeFromWaitlist(result.getString("FACULTYNAME"), date, conn);
                        return true;
                    }
                }
            }while(result.next());
        }
        return true;
    }
    public boolean replaceAReservation(String name, String date, int seats,Connection conn) throws SQLException
    {
        Rooms R = new Rooms();
        String Room = R.closestToSeats(seats+1, conn);
        boolean onList = false;
        PreparedStatement pstat =conn.prepareStatement("Select * from AvailableDates where Date = ?");
        pstat.setString(1,date);
        
        ResultSet result = pstat.executeQuery();
        
        if(result.next())
        {
            onList = true;
        }
        
        if(onList)
        {
            
        pstat =conn.prepareStatement("Select * from RESERVATIONS where Date = ? and FACULTYNAME = ?");
        pstat.setString(1,date);
        pstat.setString(2,name);
        
        
        result = pstat.executeQuery();

        if(!result.next())
        {
            
            boolean isroom = false;
            pstat =conn.prepareStatement("Select * from RESERVATIONS where Date = ? and RMNO = ?");
            pstat.setString(1,date);
            pstat.setString(2,Room);


            result = pstat.executeQuery();
            
            if(!result.next())
            {
                do
                {
                    if(result.getString(1).equals(Room))
                        isroom = true;
                }while(result.next());
                if(!isroom)
                {
                    
                pstat =conn.prepareStatement("INSERT INTO RESERVATIONS(RMNO,FACULTYNAME,DATE) VALUES(?,?,?)");
                pstat.setString(3,date);
                pstat.setString(2,name);
                pstat.setString(1,Room);
                pstat.execute();
                return true;
                }
                else
                {
                    WaitList WL = new WaitList();
                    WL.AddToWaitList(name, date,seats, conn);
                    
                    return false;
                }

            }
            else 
            {
                if(Room.equals("0"))
                {
                    do
                    {

                        while(Room.equals(R.closestToSeats(seats, conn)))
                        {
                            seats++;
                        }
                        Room = R.closestToSeats(seats, conn);
                        if(Room.equals("0"))
                        {
                            WaitList WL = new WaitList();
                            WL.AddToWaitList(name, date,seats, conn);

                            return false;

                        }
                        else
                        {
                            pstat =conn.prepareStatement("Select * from RESERVATIONS where Date = ? and RMNO = ?");
                            pstat.setString(1,date);
                            pstat.setString(2,Room);
                            result = pstat.executeQuery();

                        }

                    }while(result.next());
                }
                else
                {
                    WaitList WL = new WaitList();
                    WL.AddToWaitList(name, date,seats, conn);
                }

                if(Room.equals("0"))
                {
                    WaitList WL = new WaitList();
                    WL.AddToWaitList(name, date,seats, conn);

                    return false;
                }
                    pstat =conn.prepareStatement("INSERT INTO RESERVATIONS(RMNO,FACULTYNAME,DATE) VALUES(?,?,?)");
                    pstat.setString(3,date);
                    pstat.setString(2,name);
                    pstat.setString(1,Room);
                    pstat.execute();
                

            }

            return true;
            }
        }
        return false;
    }
}
