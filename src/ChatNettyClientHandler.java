import java.util.ArrayList;

import org.json.*;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ChatNettyClientHandler extends SimpleChannelInboundHandler<String> {
	
	private BaseFrame bf;
	ArrayList userinfo = new ArrayList<String>();
	int count = 1;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("welcome :" + bf.getName());
		for(int i=0;i<100;i++){
		userinfo.add("");
		}
	}
	public ChatNettyClientHandler() {
		// TODO Auto-generated constructor stub
	}
	public ChatNettyClientHandler(BaseFrame bf) {
		this.bf = bf;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		
		System.out.println(msg);
		
		if(bf.getSumUser() >= 3){
			//예외처리
		}
		
		String temp[] = msg.split(":");
		String name = temp[1];
		System.out.println("userlist: "+userinfo.toString());
		System.out.println("bf's username" + bf.getUsername());
		
		if(temp[0].equals("GAMEIN")){
			int x = Integer.parseInt(temp[2]);
			int y = Integer.parseInt(temp[3]);
			
			if(temp[1].equals(bf.getUsername())){
				System.out.println("i'm wndls "+name);

				userinfo.set(0, name);
				bf.setCharacterX(50);
				bf.setCharacterY(50);
			}else{
				userinfo.add(count, name);
				bf.CreateOtherUser(count, name);
				bf.setUserLocation(count, x, y);
				bf.setUserVisible(count, true);	
				System.out.println("other client "+ userinfo.get(count));
				count++;
			
			
			}
			
			int size = userinfo.size();
			
		}
		
		if(temp[0].equals("LOCATIONCHANGED")){
			int x = Integer.parseInt(temp[2]);
			int y = Integer.parseInt(temp[3]);

			if(bf.getUsername().equals(name)){	
				bf.setCharacterX(x);
				bf.setCharacterY(y);
			}
			
			for(int i=1;i<userinfo.size();i++){
				if(userinfo.get(i).equals(name)){
					System.out.println("in userinfo, name"+name+" i: "+ i);
					bf.CreateOtherUser(i, name);
					bf.setUserLocation(i, x, y); 
					bf.setUserVisible(i, true);
					break;
				}
			}
		}
		
		else if(temp[0].equals("MSG")){
			BaseFrame.sendMessage(name+ ": "+temp[2]);
		}
		
		else if(temp[0].equals("GAMEOUT")){
			if(!name.equals(bf.getUsername())){
				for(int i=1;i<userinfo.size();i++){
					if(userinfo.get(i).equals(name)){
						userinfo.set(i, "");
						bf.setUserLocation(i, 50, 50);
						bf.setUserVisible(i, false);
					}
				}
			}
			else if(name.equals(bf.getUsername())){
				userinfo.clear();
				for(int i=0;i<100;i++){
					userinfo.add("");
				}
			}
		}
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx){
		
	}
		
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
        System.out.println("exceptionCaught");
    }
}
