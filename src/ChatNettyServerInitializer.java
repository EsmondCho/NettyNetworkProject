import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;

public class ChatNettyServerInitializer extends ChannelInitializer<SocketChannel>{
	private static final StringDecoder DECODER = new StringDecoder();
	private static final StringEncoder ENCODER = new StringEncoder();

	private static ChatNettyServerHandler SERVER_HANDLER = null;
	
	private final SslContext sslCtx;

	public ChatNettyServerInitializer(ChannelGroup[] channels, SslContext sslCtx) {
		SERVER_HANDLER = new ChatNettyServerHandler(channels);
		this.sslCtx = sslCtx;
	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		if(sslCtx != null){
			pipeline.addLast(sslCtx.newHandler(ch.alloc()));
		}
		
		pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));	    
		pipeline.addLast(DECODER);	    
		pipeline.addLast(ENCODER);	   
		pipeline.addLast(SERVER_HANDLER);
	   
	}
}
