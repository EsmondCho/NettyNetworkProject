
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.JButton;
import javax.swing.JTextPane;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public final class ChatNettyClient {

	private static final int PORT = 8888;

	final static int D = 0;
	final static int MSG = 1;
	final static int GAMEIN = 3;
	final static int GAMEOUT  = 4; 
	
	final static int LOCATIONCHANGED= 5;
	
	static String data = "";
    
    public static void main(String[] args) throws Exception {

    	String nickName = "";
    	String Msg = "";
        String preMsg = "";
    	
    	int RoomNum = 0, preRoomNum = 0;
    	int X = 0, preX = 50;
    	int Y = 0, preY = 50;
    	int state = 0;

    	SslContext sslCtx = null;
    	SslContextBuilder builder = null;
    	SelfSignedCertificate cert = null;
    	
    	EventLoopGroup group = new NioEventLoopGroup(); // multi-thread : CPUs X 2
        BaseFrame bf = new BaseFrame("gameLobby"); // send object to ChatNettyClientHandler, if not valid, final
        
        try {
        
    		File certChainFile = new File("netty.crt");
    		File keyFile = new File("privatekey.pem");
    		      	
    		builder = SslContextBuilder.forClient().trustManager(certChainFile);
    		
    		sslCtx =  builder.build();
    		
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new ChatNettyClientInitializer(bf, sslCtx));
            
            ChannelFuture f = b.connect("localhost", PORT).sync();

            Channel currentChannel = f.channel();
            Msg = "";
        	nickName = bf.getUsername();
        	RoomNum = 0; 
        	X = 50;
        	Y = 50;
        	
        	data = setJsonData(nickName, RoomNum, X, Y, Msg, D);
    		currentChannel.writeAndFlush(data + "\n\r");
    		
            while(true){
            	
            	try {
            		Thread.sleep(1);
            	} catch(Exception e){
            		System.out.println(e);
            	}
            	
            	Msg = bf.getMessage();
            	nickName = bf.getUsername();
            	RoomNum = bf.getSelectroom(); 
            	
            	X = bf.getTempPointX();
            	Y = bf.getTempPointY();
     	
	        	if(!Msg.equals(preMsg) || RoomNum != preRoomNum || X != preX || Y != preY){
	        		
	        		if(!Msg.equals(preMsg))
	        			data = setJsonData(nickName, RoomNum, X, Y, Msg, MSG);
	        		else if(RoomNum != preRoomNum){
	        			if(RoomNum != 0){
	        				data = setJsonData(nickName, RoomNum, X, Y, Msg, GAMEIN);
	        			}else{
	        				data = setJsonData(nickName, RoomNum, X, Y, Msg, GAMEOUT);
	        			}
	        		}
	        		else if(X != preX || Y != preY){
	        			data = setJsonData(nickName, RoomNum, X, Y, Msg, LOCATIONCHANGED);
	        		}

	            	currentChannel.writeAndFlush(data+"\n\r");
	            	
	        		preMsg = Msg;
		        	preRoomNum = RoomNum;
		        	preX = X;
		        	preY = Y;
	        	}
            }
        }
        finally {
            group.shutdownGracefully();
        }
    }

    public static String setJsonData(String nickName, int roomNum, int x, int y, String Msg, int state){
    
    	JSONObject data = new JSONObject();
    	
    	try {
			data.put("nickName", nickName);
			data.put("message", Msg);
			data.put("roomNum", roomNum);
			data.put("x", x);
			data.put("y", y);
			data.put("state", state);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	
    	return data.toString();
    }    
}

