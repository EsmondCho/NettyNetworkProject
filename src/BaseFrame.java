

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import javax.swing.JFrame;


public class BaseFrame extends JFrame {

	private JPanel contentPane;
	public static BaseFrame s;
	public static GameLobby gameLobby = null;
	public static GameRoom gameRoom = null;
	private static String username = "";
	private static int selectroom;
	private static String message = "";
	protected static Point tempPoint = new Point(50,50);
	protected static JLabel mycharacter = new JLabel(username);
	protected static JLabel[] otherUser = new JLabel[10];
	
	private static int sumuser = 1;
	protected static JTextField mychat;
	protected static JTextPane chatField;
	/**
	 * Launch the application.
	 */


	/**
	 * Create the frame.
	 */
	public BaseFrame() {
		
	}
	
	
	public BaseFrame(String frameStr){
		if(frameStr.equals("gameLobby"))
		{gameLobby = new GameLobby();}
		mycharacter.setLocation(50,50);
		for(int i=0;i<10;i++){
			otherUser[i] = new JLabel("", JLabel.CENTER);
			otherUser[i].setLocation(50,50);
		}
		if(frameStr.equals("gameRoom"))
		{gameRoom = new GameRoom();}
	}
	public static void setUsername(String name){
		username = name;
	}
	public String getUsername(){
		return username;
	}
	public static int getSelectroom(){
		return selectroom;
	}
	public static void setSelectroom(int select){
		selectroom = select;
	}

    public String getMessage(){
    	return message;
    }
    public static void setMessage(String msg){
    	message = msg;
    }
    
    /* position of label before move */
    public int getCharacterX(){
        return mycharacter.getX();
     }
     public int getCharacterY(){
        return mycharacter.getY();
     }
     
     /* position of label to move */
     public void setCharacterX(int cX){
        mycharacter.setLocation(cX, mycharacter.getY());
     }
     
     public void setCharacterY(int cY){
        mycharacter.setLocation(mycharacter.getX(), cY);
     }
     
     public int getTempPointX(){
         return (int)tempPoint.getX();
      }
     
     public int getTempPointY(){
         return (int)tempPoint.getY();
      }
     
     public void setTempPointX(int cX){
         tempPoint.setLocation(cX, mycharacter.getY());
      }
     
     public void setTempPointY(int cY){
         tempPoint.setLocation(mycharacter.getX(), cY);
      }
     
     public static void setUserVisible(int i, boolean vsb){
    	 System.out.println("setuservisible: "+vsb+", i : "+i);
    	 otherUser[i].setVisible(vsb);
     }
     
     public static void setUserLocation(int i, int x, int y){
    	 System.out.println("setuserlocation: "+x+y+" i : "+i);
    	 otherUser[i].setLocation(x, y);
     }
     
     public static void CreateOtherUser(int i, String name){
    	 System.out.println("settext other user i : "+i);
    	 otherUser[i].setText(name);
     }
    
     public static void sendMessage(String msg){
         
         SimpleAttributeSet keyWord = new SimpleAttributeSet();
//         JScrollPane jsp = new JScrollPane(chatField);
         try
         {
        	 
            StyledDocument doc = chatField.getStyledDocument();
             doc.insertString(doc.getLength(), msg+"\n", keyWord );
         }
         catch(Exception e1) { System.out.println(e1); }
     }
     
     public static String getMyChat(){
    	 return mychat.getText();
     }
     public static void setSumUser(int sum){
    	 sumuser = sum;
     }public static int getSumUser(){
    	 return sumuser;
     }
     
}