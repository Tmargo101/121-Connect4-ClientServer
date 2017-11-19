import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.awt.event.*;


public class ConnectFour extends JFrame {


   StartActions actions = new StartActions();
   
   public static final int SERVER_PORT = 23001; // the port that the server is going to be listening on
   private static Socket socket;
   
   

   private String name;
   private String ip;
    private  boolean validData = false;


   private JPanel headerPanel = new JPanel();
   private JPanel mainPanel = new JPanel();
   private JPanel footerPanel = new JPanel();

   private JTextField ipField = new JTextField();
   private JTextField nameField = new JTextField();
   
   private JButton connectButton = new JButton("Connect");
   private JLabel ipLabel = new JLabel("IP Address");
   private JLabel nameLabel = new JLabel("Name (For Chat)");
   private JLabel topLabel = new JLabel("Welcome to Connect4.");

   public ConnectFour(){
   
      setSize(300, 175);
      setLocationRelativeTo(null);
      setTitle("Connect Four - Connect");
      setLayout(new BorderLayout());
      setResizable(false);
      mainPanel.setLayout(new GridLayout(2,2));
      
      
      
      mainPanel.add(ipLabel);
      mainPanel.add(ipField);
      mainPanel.add(nameLabel);
      mainPanel.add(nameField);
      
      footerPanel.add(connectButton);
      
      headerPanel.add(topLabel);
      
      connectButton.addActionListener(actions);
      nameField.addKeyListener(actions);
//      ipField.addKeyListener(actions);
      
//getRootPane().setDefaultButton(connectButton);

      add(headerPanel, BorderLayout.NORTH);
      add(mainPanel, BorderLayout.CENTER);
      add(footerPanel, BorderLayout.SOUTH);
      
            
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setVisible(true);
      
   }   
   
   public void startGame() {
   
      name = nameField.getText();
      ip = ipField.getText();
      try{         
         validateSetupData(name, ip);
         if (validData = true) {
            socket = new Socket(ip, SERVER_PORT);
            System.out.println("You've connected to the server");
         }
      }
      
      catch(UnknownHostException uhe){
         System.out.println("The host might not exist: " + uhe);
         JOptionPane.showMessageDialog(this, "The host might not exist");
      }
      
      catch (ConnectException ce) {
         JOptionPane.showMessageDialog(this, "No response from Server.\nMake sure the server is up and try again.\n Press OK.");
         System.out.println("Connection Exception" + ce);
      }
      
      catch(IOException ioe){
          System.out.println("IOException: " + ioe);
      }
      try {
         if (socket.isConnected() == true) {
            new MainGUI(socket);
            setVisible(false);
         }
      } catch (NullPointerException npe) {
         System.out.println("NullPointerException" + npe);
      }
      
       
       
   }//End startGame
   
   public void validateSetupData(String inName, String ipAddress){
      validData = false;
      
      if(inName.equals("")){
         name = "Guest";
      }
      if(ipAddress.equals("")){
         while(validData == false){
            if(!ipAddress.equals("")){
              ip = ipAddress;
              validData = true;
            } else {
               JOptionPane.showMessageDialog(this, "Invalid IP Address.");
               break;
            }

         }
      }
   }//End validateSetupData
   
   public static void main(String[] args) {
      new ConnectFour();
   }
   
   
   
   class StartActions extends KeyAdapter implements ActionListener {
      public void actionPerformed(ActionEvent ae) {
         if(ae.getActionCommand().equals("Connect")) {
            startGame();    
         }
      }
       public void keyReleased(KeyEvent ke) {
          if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
            ipField.requestFocusInWindow();
            startGame();
          }
       }
   }//End StartActions Class



}//End Class