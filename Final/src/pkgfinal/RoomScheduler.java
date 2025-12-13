package pkgfinal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;
import java.sql.SQLException;


public class RoomScheduler 
{
    
    public static void main(String[] args) {
        try(
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "root", "nathan3110")
        ) 
        {
            Scanner in = new Scanner(System.in);
            Faculty faculty = new Faculty();
            Date dates = new Date();
            Reservations reservations = new Reservations();
            Rooms rooms = new Rooms();
            WaitList waitlist = new WaitList();
            
            
            
             
            }
        catch(SQLException e) {
              System.getLogger(RoomScheduler.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);
        }
    }
}
