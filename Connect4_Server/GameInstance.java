import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;



public class GameInstance extends Thread {

   //Which game is this?
   private int currentGame;
   
   //INIT variables for this game
   public static int currentTurn = 0;
   public static int currentPlayer = 0;
   public static int currentColumn;
   public static int gameID = -1;
   
   private static String sendWinConditions;
      
   private static Socket socketOne, socketTwo;
   private static PrintWriter clientWriterOne, clientWriterTwo;
   private Scanner clientReaderOne, clientReaderTwo;
   private static String networkResponse;

   public static GameLogic logic = new GameLogic();

   //Create ArrayList for this game
   public static ArrayList <BoardColumn_Server> columns = new ArrayList<>();

   /**
   Constructor
   @param inSocketOne, inSocketTwo sockets for the two connected players
   @param inGameNum value which specifies which game this is
   */


   public GameInstance(Socket inSocketOne, Socket inSocketTwo, int inGameId) {
      
      socketOne = inSocketOne;
      socketTwo = inSocketTwo;
      gameID = inGameId;
      
      System.out.println("Instance: " + socketOne + ", Is connected = " + socketOne.isConnected());
      System.out.println("Instance: " + socketTwo + ", Is connected = " + socketTwo.isConnected());
      
      try{
         clientWriterOne = new PrintWriter(new OutputStreamWriter(socketOne.getOutputStream()));
         clientWriterTwo = new PrintWriter(new OutputStreamWriter(socketTwo.getOutputStream()));
      }
      catch(IOException ioe){System.out.println("Tried to create Printwriters in constructor" + ioe);}
      
      currentGame = inGameId;
      //Create columns for this game
      for(int i = 0; i < 7; i++) {
         columns.add(new BoardColumn_Server(i));
      }//End FOR
      
      sendStartingData();      

      
      ServerListener sl = new ServerListener();
      
   }//End Constructor
   
   /**
   Create setters and getters for THIS instance of the game
   */
   
   public static int getPlayer() {
      return currentPlayer;
   }
   
   public static void setPlayer(int inCurrentPlayer) {
      currentPlayer = inCurrentPlayer;
   }
   
   public static int getNumSlots(int inColumnNum) {
      //Figure out what this is
      int numSlots = 5;
      return numSlots;
   }
   
   public static int getTurn() {
      return currentTurn;
   }  
   
   public static void setTurn(int inNewTurn) {
      currentTurn = inNewTurn;
   }
   
   /**
      Obtains the state of a specific slot.
      @param inX represents the index in the arraylist called columns
      @param the location of the boardslot in a board column
      @return the state of a specific slot
   */
   public static int getSlot(int inX, int inY) { 
      int slotValue = columns.get(inX).getSlot(inY).getState();
      return slotValue;
   }

   
   /**
      Updates the state of the slot and changes the color based on the state.
      @param inX represents the index in the arraylist called columns
      @param inY the location of the boardslot in a board column 
      @param inState integer that determines the state of a slot
   */
   public static void setSlot(int inX, int inY, int inState) {
      columns.get(inX).getSlot(inY).setState(inState);
   } 
   
   public static void formatNetworkResponse(int inX, int inY, int inState, int inPlayerTurn, int inWinConditions) {
     String sendX = Integer.toString(inX);
     String sendY = Integer.toString(inY);
     String sendState = Integer.toString(inState);
     String sendPlayerTurn = Integer.toString(inPlayerTurn);
     sendWinConditions = Integer.toString(inWinConditions);

     networkResponse = String.format(sendX + "," + sendY + "," + sendState + "," + sendPlayerTurn + "," + sendWinConditions);
     sendNetworkData();
   }  
   
   
   /**We need to identify who's player 0 (red), and who's player 1 (yellow).
   When the new GameInstance is constructed, this sends a string which will tell the game who's starting
   and who's not.
   */
   public static void sendStartingData(){
            
      clientWriterOne.println("0,0,0,0,4");
      System.out.println("Sending Starting Data (Client One): 0,0,0,0,4");
      clientWriterTwo.println("0,0,0,0,5");
      System.out.println("Sending Starting Data (Client Two): 0,0,0,0,5");
      
      clientWriterOne.flush();
      clientWriterTwo.flush();
   
   }

   
   
  public static void sendNetworkData(){
      
      clientWriterOne.println(networkResponse);
      System.out.println("Network Response Sent (Both Clients): " + networkResponse);
      clientWriterTwo.println(networkResponse);
      
      clientWriterOne.flush();
      clientWriterTwo.flush();
      

   }
   
   public static void resetBoard() {
      
      
      for(int x = 0; x < 7; x++) {
         for(int y = 0; y < 6; y++) {
            setSlot(x,y,0);
         }
      }
      logic.resetWinVariable();
      sendStartingData();
      
      currentTurn = 0; //to prevent the board from catching a Stalemate when it's not
      currentPlayer = 0; //Allow red to go again after win
      
      
   }
   
   
   class ServerListener implements Runnable {
      
      Thread thread;
      
      public ServerListener(){
         thread = new Thread(this);
         thread.start();
      }
      
      public void run() {
         try {
            clientReaderOne = new Scanner(new InputStreamReader(socketOne.getInputStream()));
            clientReaderTwo = new Scanner(new InputStreamReader(socketTwo.getInputStream()));
         }
         catch(IOException ioe){System.out.println("Run method IOException: " + ioe);}
         
         //Change
         while(true) {
            if (currentPlayer == 0) {
               String clientOneData = clientReaderOne.nextLine();
               System.out.println("*************************************");
               System.out.println("START - Game " + gameID + " Data Information");
               System.out.println("*************************************");
               System.out.println("Player 1 sent: " + clientOneData);
               currentColumn = Integer.parseInt(clientOneData);
               logic.playerTurn(currentColumn);
               System.out.println("Now it's " + currentPlayer + "'s Turn");
               System.out.println("Client One isClosed: " + socketOne.isClosed());
               System.out.println("Client One isConnected: " + socketOne.isConnected());
               System.out.println("Client Two isClosed: " + socketTwo.isClosed());
               System.out.println("Client Two isConnected: " + socketTwo.isConnected());
               System.out.println("Current Turn Number: " + currentTurn);
               System.out.println("*************************************");
               System.out.println("END - Game " + gameID + " Data Information");
               System.out.println("*************************************");
               
               
            } else if (currentPlayer == 1) {
               String clientTwoData = clientReaderTwo.nextLine();
               System.out.println("*************************************");
               System.out.println("START - Game " + gameID + " Data Information");
               System.out.println("*************************************");
               System.out.println("Player 2 sent: " + clientTwoData);
               currentColumn = Integer.parseInt(clientTwoData);
               logic.playerTurn(currentColumn);
               System.out.println("Now it's " + currentPlayer + "'s Turn");
               System.out.println("Client One isClosed: " + socketOne.isClosed());
               System.out.println("Client One isConnected: " + socketOne.isConnected());
               System.out.println("Client Two isClosed: " + socketTwo.isClosed());
               System.out.println("Client Two isConnected: " + socketTwo.isConnected());
               System.out.println("Current Turn Number: " + currentTurn);
               System.out.println("*************************************");
               System.out.println("END - Game " + gameID + " Data Information");
               System.out.println("*************************************");
            }

         }
      }
   } //END OF SERVERLISTENER CLASS

   public static void printDataServer(String message) {
      //Method is strictly used by GameLogic to print
      //data to the server for troubleshooting purposes
      System.out.println(message);
   }

   
}//End GameInstance