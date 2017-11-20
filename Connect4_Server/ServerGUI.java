//VERSION 1.2 - MULTI THREADING WORKS - Caution, Don't try on a old computer.

package Connect4_Server;

import javax.swing.*;
import javax.swing.border.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;


public class ServerGUI extends JFrame {

   private ServerSocket sSocket = null;
   // public InetAddress localhost = null;
   public static final int SERVER_PORT = 23001;
   public int clientsConnected = 0;
   public int gamesConnected = 0;
   
   private Socket cSocket = null;
   
   public JLabel jLogLabel = new JLabel("Log:");
   public JTextArea jLogArea = new JTextArea(10, 35);
   public JLabel clientsConnected_label = new JLabel("Clients Connected: 0");
   public JLabel ipLabel = new JLabel("");
   
   public JButton start = new JButton("Start Server");
   public JButton stop = new JButton("Stop Server");
   
   ArrayList<ClientThread> threads = new ArrayList<ClientThread>();
   ArrayList<GameLogic> gameLogic_threads = new ArrayList<GameLogic>();
   ArrayList<GameInstance> gameInstance_threads = new ArrayList<GameInstance>();

   public ServerGUI() {
      // Window setup
      this.setTitle("Server GUI");
      this.setSize(450, 250);
      this.setLocation(600, 50);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      //this.add(new JLabel("Server GUI - Coming Soon", SwingConstants.CENTER));
      
      JPanel central = new JPanel();
      JPanel northern = new JPanel();
      JPanel southern = new JPanel();
      central.add(jLogLabel);
      central.add(new JScrollPane(jLogArea));
      southern.add(clientsConnected_label);
      
       // localhost = new Socket();
//         try {
//            String x = sSocket.getInetAddress().toString();
//         } catch (IOException e2){}
//         
//         }
// //        ipLabel.setText(localhost.getHostAddress());
// 
//        ipLabel.setText(x);
//        southern.add(ipLabel);
      //northern.add(start);
      //northern.add(stop);
      this.add(northern, BorderLayout.NORTH);
      this.add(central, BorderLayout.CENTER);
      this.add(southern, BorderLayout.SOUTH);
      

      this.setVisible(true); //last note


       Thread chatThread = new Thread() {
           @Override
           public void run(){
               ChatServer cServer = new ChatServer();
           }
       };

       chatThread.start();
      
         
         // SERVER INFORMATION
      try {
         sSocket = new ServerSocket(SERVER_PORT);
         jLogArea.append("Server Started!\n");
      }
      catch(IOException e1) {
         System.out.println("Uh oh! An exception");
      }
      
   while(true) {
         Socket cSocket = null; //Client Socket

         try {
            // Wait for a connection
            cSocket = sSocket.accept();
            clientsConnected++;
            
            clientsConnected_label.setText("Clients Connected: " + clientsConnected);
            

         }
         catch(IOException e1) {
            System.out.println("Uh oh! An exception");
         }
         
         System.out.println("Client connected!");
         jLogArea.append("Client connected!\n");
         
         System.out.println(cSocket);
         
         synchronized(threads) {
         
            threads.add(new ClientThread(cSocket));
            
            Thread t = new Thread(threads.get(clientsConnected - 1));
            t.start();
            
         }
         
      } 
    } 
      
   
      
   class ClientThread implements Runnable {
      Socket cSocket = null;
      
      public ClientThread(Socket clientsSocket) {
         cSocket = clientsSocket;
         
      }
         
      public Socket getSocket() {
         return cSocket;
         
      }
         
      
      public void run() {
         PrintWriter pwt = null;
         Scanner scn = null;
         boolean message_sent = false;
         boolean gameStarted = false;
         
         try {
               pwt = new PrintWriter(new OutputStreamWriter(cSocket.getOutputStream()));
               scn = new Scanner(new InputStreamReader(cSocket.getInputStream()));
            }
            catch (IOException e1) {
               System.out.println("Uh oh! An exception");
            }
        while (getSocket().isConnected()) {
           while (gameStarted == false & clientsConnected > 0) { 
         
               if (clientsConnected % 2 != 0) {
                     if (message_sent == false) {
                        //pwt.println("Please hold while I continue to look for another player");
                        //pwt.flush();
                        message_sent = true;
                     }
                  
               }
               else {
                  System.out.println("Starting Game...");
                  jLogArea.append("Starting Game " + (gamesConnected + 1) + "\n");
                  //pwt.println("Starting Game...");
                  //pwt.flush();
                  
                  gameStarted = true;
                  gamesConnected++;
                  
                  
                  gameInstance_threads.add(new GameInstance(threads.get(clientsConnected - 2).getSocket(), threads.get(clientsConnected - 1).getSocket(), gamesConnected));
                  gameLogic_threads.add(new GameLogic(gameInstance_threads.get(gamesConnected - 1)));
                  
                  
               }
            }
         }
         
            System.out.println("Client disconnected!");
            jLogArea.append("Client disconnected!\n");
            
            clientsConnected--;
            clientsConnected_label.setText("Clients Connected: " + clientsConnected);
         
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