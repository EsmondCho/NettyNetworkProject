
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.SystemColor;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.ImageIcon;

   

public class GameRoom extends BaseFrame{
   
   private JPanel jPanel;
   
   private String message = "";
   
   public GameRoom() {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(200, 100, 1000, 500);
      jPanel = new JPanel();
      jPanel.setBackground(new Color(255, 182, 193));
      jPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(jPanel);
      jPanel.setLayout(null);
      jPanel.addKeyListener(new MyKeyListener());
        jPanel.setFocusable(true);
       
        //캐릭터
        mycharacter = new JLabel(getUsername(), JLabel.CENTER);
        mycharacter.setLocation(50,50);
        mycharacter.setSize(70, 20);
        BevelBorder border;
        border=new BevelBorder(BevelBorder.RAISED);//3차원적인 테두리 효과를 위한것이고 양각의 옵션을 준다.
        mycharacter.setBorder(border);//라벨에 적용시킨다.
        //대화 후 초점이 대화창에 가있어서 자바가 캐릭터를 잡지 못한다. 그래서 캐릭터를 클릭하면 다시 움직일 수 있게 끔.
        jPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	jPanel.requestFocusInWindow();
            }
        });
        jPanel.add(mycharacter);
        tempPoint = new Point(50,50);

        for(int i=0;i<10;i++){
        	otherUser[i].setSize(70,20);
        	otherUser[i].setBorder(border);
        	otherUser[i].setVisible(false);
        	jPanel.add(otherUser[i]);
        }

      //대화가 이루어 지는 곳
      chatField = new JTextPane();
      chatField.setBounds(680, 70, 300, 280);
      chatField.setText("대화 시작!\n");
      jPanel.add(chatField);
      
      
      //대화 쓸 곳
      mychat = new JTextField();
      mychat.setBounds(680, 360, 300, 50);
      jPanel.add(mychat);
      mychat.setColumns(10);
      
      Action action = new AbstractAction()
      {
          @Override
          public void actionPerformed(ActionEvent e)
          {
        	  message = getMyChat();
              mychat.setText("");
              BaseFrame.setMessage(message);
//            SimpleAttributeSet keyWord = new SimpleAttributeSet();
//            try
//            {
//               StyledDocument doc = chatField.getStyledDocument();
//                doc.insertString(doc.getLength(), getUsername()+": "+ message +"\n", keyWord );
//                
//                
//            }
//            catch(Exception e1) { System.out.println(e1); }
          }
      };
      mychat.addActionListener( action );
      
      
      
      //메세지 보내는 버튼
      JButton sendMessage = new JButton("\uBA54\uC138\uC9C0 \uBCF4\uB0B4\uAE30");
      sendMessage.setBounds(680, 420, 300, 30);
      sendMessage.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {  
            message = getMyChat();
            mychat.setText("");
            BaseFrame.setMessage(message);
            }
      });
      jPanel.add(sendMessage);
      
      //대화창 위에 글씨
      JLabel label = new JLabel("\uB300\uD654\uCC3D");
      label.setFont(new Font("HY바다M", Font.PLAIN, 24));
      label.setBounds(681, 30, 94, 30);
      jPanel.add(label);
      
      JButton leaveRoom = new JButton("\uAC8C\uC784\uBC29 \uB098\uAC00\uAE30");
      leaveRoom.setIcon(new ImageIcon(GameRoom.class.getResource("/com/sun/javafx/scene/web/skin/Undo_16x16_JFX.png")));
      leaveRoom.setFont(new Font("함초롬돋움", Font.PLAIN, 15));
      leaveRoom.setBackground(new Color(255, 204, 102));
      leaveRoom.setBounds(817, 30, 155, 30);
      leaveRoom.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            s= new BaseFrame("gameLobby");
            setSelectroom(0);
            setVisible(false);
         }
      });
      jPanel.add(leaveRoom);
        
      //게임방이 안들가진다
      setTitle("Game Room"+String.valueOf(BaseFrame.getSelectroom()));
      setVisible(true);
      setResizable(true);
      
   }
   
   class MyKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            switch (keyCode) {
            case KeyEvent.VK_UP:
               if(getCharacterX() > 10)
//                  setCharacterY(mycharacter.getY()-10);
                  tempPoint.setLocation(mycharacter.getX(), mycharacter.getY()-10);
                break;
            case KeyEvent.VK_DOWN:
               if(getCharacterY() <430)
//                  setCharacterY(mycharacter.getY()+10);
                  tempPoint.setLocation(mycharacter.getX(), mycharacter.getY()+10);
                break;
            case KeyEvent.VK_LEFT:
               if(getCharacterY() > 10)
//                  setCharacterX(mycharacter.getX()-10);
                  tempPoint.setLocation(mycharacter.getX()-10, mycharacter.getY());
                break;
            case KeyEvent.VK_RIGHT:
               if(getCharacterX() < 600)
//                  setCharacterX(mycharacter.getX()+10);
                  tempPoint.setLocation(mycharacter.getX()+10, mycharacter.getY());
                break;
            }
        }
    }
}