import java.io.File;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatNettyServer {
	
	private static final int PORT = 8888;
	
    public static void main(String[] args) throws Exception {
    	
    	SslContext sslCtx = null;
    	SslContextBuilder builder = null;
    	
        EventLoopGroup bossGroup = new NioEventLoopGroup(1); // Single thread
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // Multi thread - CPUs X 2
        
        final ChannelGroup[] channels = new ChannelGroup[7];
        
        for(int i=0; i<7; i++){
        	channels[i] = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        }
       
    	try {
    		
    		File certChainFile = new File("netty.crt");
    		File keyFile = new File("privatekey.pem");
    		
    		builder = SslContextBuilder.forServer(certChainFile, keyFile, "rkawkwlduswjddus");
    		
    		sslCtx =  builder.build();
    		
            ServerBootstrap b = new ServerBootstrap()
            		.group(bossGroup, workerGroup)
            		.channel(NioServerSocketChannel.class)
            		.option(ChannelOption.SO_KEEPALIVE, true)
					.handler(new LoggingHandler(LogLevel.INFO))
            		.childHandler(new ChatNettyServerInitializer(channels, sslCtx));

            ChannelFuture f = b.bind(PORT).sync();
            f.channel().closeFuture().sync();
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}