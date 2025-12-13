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
public class WaitList 
{
    public boolean AddToWaitList(String name, String date,int seats,Connection conn) throws SQLException
    {
        PreparedStatement pstat =conn.prepareStatement("Select * from WAITLIST where Dates = ? and FACULTYNAME = ?");
        pstat.setString(1,date);
        pstat.setString(2,name);
        
        
        ResultSet result = pstat.executeQuery();
        
        if(!result.next())
        {
            pstat =conn.prepareStatement("Select * from WAITLIST where Dates = ?");
            pstat.setString(1,date);
            result = pstat.executeQuery();

            if(result.next())
            {
                int order =1;
                if(result.isLast())
                {
                    order = result.getInt(3)+1;
                }
                else
                {
                    result.last();
                    order = result.getInt(3)+1;
                }

                pstat =conn.prepareStatement("INSERT INTO WAITLIST VALUES(?,?,?,?)");
                pstat.setInt(4, seats);
                pstat.setInt(3,order);
                pstat.setString(2,name);
                pstat.setString(1,date);
                pstat.execute();
            }
            else
            {
                pstat =conn.prepareStatement("Select * from AvailableDates where Dates = ?");
                pstat.setString(1,date);

                result = pstat.executeQuery();

                if(result.next())
                {
                    pstat =conn.prepareStatement("INSERT INTO WAITLIST VALUES(?,?,?,?)");
                    pstat.setInt(4, seats);
                    pstat.setInt(3,1);
                    pstat.setString(2,name);
                    pstat.setString(1,date);
                    pstat.execute();
                }
                else
                {
                    return false;
                }
            }
            return true;
        }
        else
        {
            return true;
        }
    }
    
    public void printWaitList(Connection conn) throws SQLException
    {
        Statement stat =conn.createStatement();
        ResultSet result;
        result = stat.executeQuery("select * from WAITLIST");
           
           ResultSetMetaData Meta = result.getMetaData();
           

           
           for(int j=1; j<=Meta.getColumnCount()-1; j++)
           {
               System.out.print(Meta.getColumnName(j)+"\t");
           }
           
           System.out.println();
           result = stat.executeQuery("select * from AvailableDates");
           int i =1;
           while(result.next())
           {
                ResultSet nresult;

                PreparedStatement pstat =conn.prepareStatement("Select * from WAITLIST where Dates = ?");
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
    
    public void removeFromWaitlist(String name, String date,Connection conn) throws SQLException
    {
        PreparedStatement pstat = conn.prepareStatement("Select * from Waitlist where FACULTYNAME = ? and dates =?");
        pstat.setString(2,date);
        pstat.setString(1,name);

        ResultSet result = pstat.executeQuery();

        pstat = conn.prepareStatement("DELETE from Waitlist where facultyname = ? and dates =?");
        pstat.setString(2,date);
        pstat.setString(1,name);

        if(result.next())
        {

            pstat = conn.prepareStatement("Select * from Waitlist where queue > ?");
            pstat.setInt(1, result.getInt("Queue"));
            result = pstat.executeQuery();
        }
        while(result.next())
        {
            pstat = conn.prepareStatement("UPDATE waitlist SET queue = ? where dates = ?");
            pstat.setInt(1, result.getInt("queue")-1);
            pstat.setString(2, date);
            pstat.executeUpdate();
        }
    }
}
