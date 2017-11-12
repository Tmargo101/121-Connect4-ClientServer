import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
@author: Josh Schrader, Tom Margosian

@description: This class is the Main GUI for Connect4.
The class sets up all needed components for the game to look authentic.
It also holds all variables which are needed GameWide, as GameLogic
cannot hold them.
*/

public class MainGUI extends JFrame{
//Original 1260x900
   public static final int GUI_WIDTH =  1260;
   public static final int GUI_HEIGHT = 900;
   public static final int HEADER_WIDTH = 50;
   public static final int HEADER_HEIGHT = 50;
   
   public static int currentTurn = 0;
   public static int currentPlayer = 0;
   
   // the components for the GUI
   JPanel header = new JPanel();
   JMenuBar menuBar = new JMenuBar();
   JMenu options = new JMenu("Options");
   JMenu help = new JMenu("Help");
   JMenuItem instructions = new JMenuItem("Instructions");
   JMenuItem resetItem = new JMenuItem("Reset Game");
   JMenuItem exitItem = new JMenuItem("Exit");
   JLabel gameName = new JLabel("Connect4");
   public static JLabel whosTurn = new JLabel("Red's Turn");
   public static JLabel turnNumber = new JLabel("Current Turn: 0");
   
   JPanel board = new JPanel();
   MenuActions mActions = new MenuActions();
      
   //Set the ArrayList to Static
   public static ArrayList <BoardColumn> columns = new ArrayList<>();
   
   /**
      Takes care of the main setup of the gui.
   */
   public MainGUI(){
      
      setSize(GUI_WIDTH, GUI_HEIGHT);
      setLocationRelativeTo(null);
      setTitle("Connect Four");
      setLayout(new BorderLayout());
      setResizable(false);
      
      
      // menu components
      add(menuBar);
      setJMenuBar(menuBar);
      menuBar.add(options);
      menuBar.add(help);
      options.add(resetItem);
      options.add(exitItem);
      help.add(instructions);
      
      instructions.addActionListener(mActions);
      resetItem.addActionListener(mActions);
      exitItem.addActionListener(mActions);
      
      
      // header implementation
      add(header, BorderLayout.NORTH);
      header.setPreferredSize(new Dimension(HEADER_WIDTH, HEADER_HEIGHT));
      header.setLayout(new BorderLayout());
      header.setBackground(new Color(251, 251, 251));
      header.setBorder(new EmptyBorder(0, 40, 0, 40)); //provides padding for the elements in the header
      
      
      //adds the guts of the header panel
      gameName.setFont(new Font("SAN_SERIF", 1, 25));
      whosTurn.setFont(new Font("SAN_SERIF", 1, 25));
      whosTurn.setHorizontalAlignment(SwingConstants.CENTER);
      turnNumber.setFont(new Font("SAN_SERIF", 1, 20));

      header.add(gameName, BorderLayout.WEST);
      header.add(whosTurn, BorderLayout.CENTER);
      header.add(turnNumber, BorderLayout.EAST);
      
      
      //main board implementation
      add(board, BorderLayout.CENTER);
      board.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
      board.setBackground(new Color(251, 251, 251));
      
      
      //adds all the columns to the board
      for(int i = 0; i < 7; i++) {
         columns.add(new BoardColumn(i));
      }
      
      // adds all the column from the arraylist into the panel
      for(BoardColumn column : columns){
         board.add(column);
      }   
      
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setVisible(true);
   }
     
   //Put Setters and Getters for currentTurn, currentPlayer 
   
   /**
      Updates the variable that determines the turn number.
      @param inTurn the value that determines the current turn number
   */
   public static void setTurn(int inTurn) {
      currentTurn = inTurn;
   }
   
   /**
      Obtains the variable that determines the turn count.
      @return the turn count
   */
   public static int getTurn() {
      return currentTurn;
   }
   
   /**
      Updates the variable that determines whose turn it is.
      @param inPlayer the value that determines the value of currentPlayer
   */
   public static void setPlayer(int inPlayer) {
      currentPlayer = inPlayer;
   }
   
   /**
      Obtains the variable that determines whose turn it is.
      @return the value of the variable currentPlayer
   */
   public static int getPlayer() {
      return currentPlayer;
   }
   
   /**
      obtains the number of slots in a specific board column in the arraylist
      param inColumn integer that represents the index in the arrayList columns
   */
   public static int getNumSlots(int inColumn) {
      int numSlots = columns.get(inColumn).getNumSlots();
      return numSlots;
   }
   
   /**
      Updates the state of the slot and changes the color based on the state.
      @param inX represents the index in the arraylist called columns
      @param inY the location of the boardslot in a board column 
      @param inState integer that determines the state of a slot
   */
   public static void setSlot(int inX, int inY, int inState) {
      columns.get(inX).getSlot(inY).setState(inState);
      columns.get(inX).getSlot(inY).updateState();
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
      Resets all values in the entire board to their starting state
      and re-enables any buttons that may have been disabled during gameplay.
   */
   public static void resetBoard() {
      for(int x = 0; x < 7; x++) {
         for(int y = 0; y < 6; y++) {
            setSlot(x,y,0);
         }
      }
      currentTurn = 0;
      currentPlayer = 0;
      String currentColor = "";
      if (MainGUI.getPlayer() == 0) {
         currentColor = "Red";
      } else if (MainGUI.getPlayer() == 1) {
         currentColor = "Yellow";
      }
      turnNumber.setText("Current Turn: 0");
      whosTurn.setText(currentColor + "'s Turn");
      
      //Re-Enable Buttons
      for (int i = 0; i < columns.size(); i++) {
         columns.get(i).resetBtn();
      }

   } 
   
   /**
      Shows a goodbye message to the players.
   */
   public static void exit() {
      JOptionPane.showMessageDialog(null, "Thanks for Playing.  Press OK to Exit.");
      System.exit(0);
   }
   
   /**
      Allows the players read instructions on how to play the game.
   */
   public static void showInstructions() {
      String insTextNew = "Instructions:\n\nThe pieces fall straight down, occupying the next available space within the column. \n\nThe objective of the game is to be the first to form a horizontal, vertical, or diagonal \nline of four of one's own color discs. Press the buttons  of the desired column and \nit will fall to the next available slot. Each player can one make one move at a time. \nThe game will stop once the first player has won and give you an option to reset. \nYou can restart and save your game at any time by clicking reset on the drop down menu.";
      String insText = "The objective of the game is for a player to place their pieces\nSo that they are 4 in a Row.  This can be done\nHorizontally, Vertically, or Diagonally.";
      JOptionPane.showMessageDialog(null, insTextNew);
 
   }
   
   public static void main(String[] args){
      new MainGUI();
   }
}



