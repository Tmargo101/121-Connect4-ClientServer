import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
@author: Josh Schrader, Tom Margosian

@description: BoardColumn represents a single column on the board.
Each column has a border layout that separates the JButton from the
rest of the slots in the panel. The slots are structured using a grid
layout and each slot is represented by the BoardSlot class. These board slots
are managed in an array called slots. Each column has its own array of slots.
The logic for interacting with those slots should be placed within this class.  
*/

public class BoardColumn_Server extends JPanel {

      Actions actions = new Actions();
      GameLogic logic = new GameLogic();
      
      public static final int columnWidth = (MainGUI.GUI_WIDTH / 7);
      public static final int columnHeight = (MainGUI.GUI_HEIGHT - MainGUI.HEADER_HEIGHT);
      
      private BoardSlot_Server[] slots = new BoardSlot_Server[6];
      
      JButton topBtn = new JButton("Press Me!");
      JPanel slotPanel = new JPanel();
      
      public int columnNum = 0;
      
      public BoardColumn_Server(int inColumnNum) {
      
         columnNum = inColumnNum;
         
         setLayout(new BorderLayout());
         setPreferredSize(new Dimension((columnWidth - 10), columnHeight - 50));
         
         slotPanel.setLayout(new GridLayout(6, 1, 0, 0));
         slotPanel.setBackground(new Color(79, 109, 206));
         
         add(topBtn, BorderLayout.NORTH);
         add(slotPanel, BorderLayout.CENTER);
         
         // adds a new BoardSlot starting from the last position in the slots array
         // This fixed the issue of having the first index in the array (slot[0]) from starting at the top of the board
         for(int i = slots.length - 1 ; i >= 0; i--){
            slots[i] = new BoardSlot_Server();
            slotPanel.add(slots[i]);
         }
         topBtn.addActionListener(actions);

         setVisible(true);
      }//End BoardColumn Constructor
      
      /**
         @return the number of board slots in the column
      */
      public int getNumSlots(){
         return slots.length;
      }
      
      /**
         Allows the button at the top of each board column to be clickable.
      */
      public void resetBtn() {
         topBtn.setEnabled(true);
      }
      
      /**
         Obtains a specific slot in the BoardSlot array called slots.
         @return the location of a specific slot in the array
      */
      public BoardSlot_Server getSlot(int index){
         return slots[index];
      }
      
      /**
         @return the reference number of a board column
      */
      public int getColumnNum() {
         return columnNum;
      }  
      
      class Actions implements ActionListener {
         public void actionPerformed(ActionEvent ae) {
            if(ae.getActionCommand().equals("Press Me!")) {
               System.out.println("\n");
               logic.playerTurn(columnNum);
               if(slots[5].getState() == 1 || slots[5].getState() == 2) {
                  topBtn.setEnabled(false);
               }
               
            }
         }  
      }

}//End BoardColumn
   
