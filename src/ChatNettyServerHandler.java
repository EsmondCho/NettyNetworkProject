import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.json.*;

/**
 * Handler implementation for the echo server.
 */
@Sharable
public class ChatNettyServerHandler extends SimpleChannelInboundHandler<String> {
	
    private ChannelGroup[] channels = new ChannelGroup[7];
	private int value = 0;
	private int myChannelGroupNum;
	final AttributeKey<Integer> id = AttributeKey.newInstance("id");
	private static final AtomicInteger count = new AtomicInteger(0);
	
	final static int D = 0;
	final static int MSG = 1;
	final static int GAMEIN = 3;
	final static int GAMEOUT  = 4;
	final static int LOCATIONCHANGED= 5;
	
	private int index;

	ArrayList<UserInfo> userlist = new ArrayList<>();
//	UserInfo user = null;

	public ChatNettyServerHandler(ChannelGroup[] channels) {
		this.channels = channels;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		System.out.println("channelActive\r\n");
		
		this.myChannelGroupNum = 0;
		value = count.incrementAndGet();
		ctx.channel().attr(id).set(value);

		channels[myChannelGroupNum].add(ctx.channel());
		
		ctx.flush();

	}
	
    @Override
    public void channelInactive(ChannelHandlerContext ctx)throws Exception {		
    
    	syncMyChannelGroupNum(ctx.channel().id().toString());
		
    	channels[myChannelGroupNum].remove(ctx.channel());
		System.out.println("channel removed!");
		
		channels[myChannelGroupNum].writeAndFlush(ctx.channel().remoteAddress() + " has left!\r\n");
    }
    
	@Override
	public void channelRead0(ChannelHandlerContext ctx, String request)throws Exception {
		
		syncMyChannelGroupNum(ctx.channel().id().toString());
    	
    	Channel incoming = ctx.channel();

		JSONObject json = null;
		json = new JSONObject(request);
		System.out.println(request);
		UserInfo user = new UserInfo(json.getInt("roomNum"),json.getString("nickName"),json.getInt("x"),json.getInt("y"));
		if((json.getInt("state") == 0)){
			userlist.add(user);			
//			user.setRoom(json.getInt("roomNum"));
//			user.setNickname(json.getString("nickName"));
//			user.setX(json.getInt("x"));
//			user.setY(json.getInt("y"));
		}
		
//		for(int i = 0 ; i<userlist.size() ; i++){
//			if(userlist.get(i).getNickname() == json.getString("nickName")){
//				index = i;
//				break;
//			}
//		}
		
		for(int i = 0 ; i<userlist.size() ; i++){
			if(userlist.get(i).getNickname().equals(json.getString("nickName"))){
				index = i;
				break;
			}
		}
//		
		if((json.getInt("state") == GAMEIN)){
			setRoom(ctx,json.getInt("roomNum"));
			userlist.get(index).setRoom(json.getInt("roomNum"));
			userlist.get(index).setPreroomNum(json.getInt("roomNum"));
			for(int i = 0; i< userlist.size() ;i++){
				if(json.getInt("roomNum") == userlist.get(i).getRoomNum()){
					if(json.getString("nickName").equals(userlist.get(i).getNickname()))
						continue;
					else
						ctx.channel().writeAndFlush("GAMEIN:"+userlist.get(i).getNickname()+":"+userlist.get(i).getX()+":"+userlist.get(i).getY()+"\r\n");
				}
			}
			channels[json.getInt("roomNum")].writeAndFlush("GAMEIN:"+json.getString("nickName")+":"+json.getInt("x")+":"+json.getInt("y")+"\r\n");
		}else if((json.getInt("state") == GAMEOUT)){
			channels[userlist.get(index).getPreroomNum()].writeAndFlush("GAMEOUT:"+json.getString("nickName")+"\r\n");
			setRoom(ctx,json.getInt("roomNum"));
			userlist.get(index).setRoom(json.getInt("roomNum"));
		}else if((json.getInt("state") == LOCATIONCHANGED)){
			userlist.get(index).setX(json.getInt("x"));
			userlist.get(index).setY(json.getInt("y"));
			System.out.println("�쐞移섎컮�� : " + user.getX() + " "+ user.getY());

			sendData(json.getInt("roomNum"),"LOCATIONCHANGED:"+json.getString("nickName")+":"+json.getInt("x")+":"+json.getInt("y"));
		}else if((json.getInt("state") == MSG)){
			sendData(json.getInt("roomNum"),"MSG:"+json.getString("nickName")+":"+json.getString("message"));
		}
		
		System.out.println("userlist size : " + userlist.size());
		for(int i = 0; i<userlist.size(); i++){
			System.out.println("user name : " + userlist.get(i).getNickname() + " "+ userlist.get(i).getRoomNum() + " "+userlist.get(i).getX() + " "+userlist.get(i).getY());
		}
		
		//sendData(json.getInt("roomNum"),request);		
//		System.out.println("ch0 size : " + channels[0].size());
//		System.out.println("ch1 size : " + channels[1].size());
//		System.out.println("ch2 size : " + channels[2].size());
//		System.out.println("ch3 size : " + channels[3].size());
//		System.out.println("ch4 size : " + channels[4].size());
//		System.out.println("ch5 size : " + channels[5].size());
//		System.out.println("ch6 size : " + channels[6].size());
//		System.out.println("\r\n");
	}
	
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    	//ctx.flush();
        //System.out.println("channelReadCompleted");
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	System.out.println("exceptionCaught\r\n");
    	
        cause.printStackTrace();
        ctx.close();
        System.out.println("exceptionCaught");
    }
    
    public void syncMyChannelGroupNum(String channelID){
    	
    	for(int i=0; i < 7; i++){
    		for (Channel ch : channels[i]){
    			if (ch.id().toString().equals(channelID)){
    				this.myChannelGroupNum = i;
    				return;
    			}
    		}
    	}
    	return;
    }
    
    public void setRoom(ChannelHandlerContext ctx,int room){
    	if(room == 1){
    		channels[0].remove(ctx.channel());
    		channels[1].add(ctx.channel());
    	}else if(room == 2){
    		channels[0].remove(ctx.channel());
    		channels[2].add(ctx.channel());
    	}else if(room == 3){
    		channels[0].remove(ctx.channel());
    		channels[3].add(ctx.channel());
    	}else if(room == 4){
    		channels[0].remove(ctx.channel());
    		channels[4].add(ctx.channel());
    	}else if(room == 5){
    		channels[0].remove(ctx.channel());
    		channels[5].add(ctx.channel());
    	}else if(room == 6){
    		channels[0].remove(ctx.channel());
    		channels[6].add(ctx.channel());
    	}else if(room == 0){
    		channels[0].add(ctx.channel());
    		channels[1].remove(ctx.channel());
    		channels[2].remove(ctx.channel());
    		channels[3].remove(ctx.channel());
    		channels[4].remove(ctx.channel());
    		channels[5].remove(ctx.channel());
    		channels[6].remove(ctx.channel());
    	}
    }
    
    public void sendData(int room, String json){
    	String temp = json + "\r\n";
    	channels[room].writeAndFlush(temp);
    };
    
    public void toAll(ChannelHandlerContext ctx, JSONObject json){
    	try {
			if(json.getString("message").startsWith("/all")){
	    		channels[0].writeAndFlush(json.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    };
    
}
