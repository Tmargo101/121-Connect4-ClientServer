import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;


public class GameInstance extends Thread {
   
   //INIT variables for THIS game
   public static int currentTurn = 0;
   public static int currentPlayer = 0;

   //Create ArrayList for THIS game
   public static ArrayList <BoardColumn_Server> columns = new ArrayList<>();

   public GameInstance() {
      //Create columns for THIS game
      for(int i = 0; i < 7; i++) {
         columns.add(new BoardColumn_Server(i));
      }//End FOR
   }
}//End GameInstance