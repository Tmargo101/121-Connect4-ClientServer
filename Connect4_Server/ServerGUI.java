import javax.swing.*;
import javax.swing.border.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;


public class ServerGUI extends JFrame {

   private ServerSocket sSocket = null;
   public static final int SERVER_PORT = 23001;

   public ServerGUI() {
      // Window setup
      this.setTitle("Server GUI");
      this.setSize(450, 250);
      this.setLocation(600, 50);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      this.add(new JLabel("Server GUI - Coming Soon", SwingConstants.CENTER));
      
      this.setVisible(true); //last note
   
   // SERVER INFORMATION
      try {
         sSocket = new ServerSocket(SERVER_PORT);
      }
      catch(IOException e1) {
         System.out.println("Uh oh! An exception");
      }
      
      
   while(true) {
         Socket cSocket = null; //Client Socket
         try {
            // Wait for a connection
            cSocket = sSocket.accept();

         }
         catch(IOException e1) {
            System.out.println("Uh oh! An exception");
         }
         
         System.out.println("Client connected!");
         System.out.println(cSocket);
         
         ClientThread ct = new ClientThread(cSocket);
         Thread t = new Thread(ct);
         t.start();
         
      } 
   }  
   
   class ClientThread implements Runnable {
      private Socket cSocket = null;
      
      public ClientThread(Socket clientsSocket) {
         cSocket = clientsSocket;
         
      }
      
      public void run() {
         PrintWriter pwt = null;
         Scanner scn = null;
      
         try {
            pwt = new PrintWriter(new OutputStreamWriter(cSocket.getOutputStream()));
            scn = new Scanner(new InputStreamReader(cSocket.getInputStream()));
         }
         catch (IOException e1) {
            System.out.println("Uh oh! An exception");
         }
         while (scn.hasNextLine()) {
            String temp_num = scn.nextLine();
            System.out.println("Column Number: " + temp_num);
            
            int columnNum = Integer.parseInt("1");
            
            String response = changeCourse(columnNum);
            
            pwt.println(response); 
            pwt.flush();
            System.out.println("Response Sent: " + response);
         }
         
         System.out.println("Client disconnected!");
         
      }
   }

   
   public static void main (String [] args) {
      new ServerGUI();
   }
   
   
   
   public String changeCourse(int columnNum) {
      
      int columnNum_1 = columnNum;
      int rowNum = 0;
      int state = 1;
      int playerTurn = 2;
      int winCondition = 0;
      
      String respond = Integer.toString(columnNum_1) + "," + Integer.toString(rowNum) + "," + Integer.toString(state) + "," + Integer.toString(playerTurn) + "," + Integer.toString(winCondition);
      
      
      return respond;
      
   }

}