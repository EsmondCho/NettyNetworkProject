import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;

public class ChatNettyClientInitializer extends ChannelInitializer<SocketChannel> {

	private static final StringDecoder DECODER = new StringDecoder();
	private static final StringEncoder ENCODER = new StringEncoder();
	
	private static ChatNettyClientHandler CLIENT_HANDLER = new ChatNettyClientHandler();
	private static BaseFrame bf;
	
	private SslContext sslCtx = null;
	
	public ChatNettyClientInitializer() {
	}

	public ChatNettyClientInitializer(BaseFrame bf, SslContext sslCtx) {
		this.bf = bf;
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
		pipeline.addLast(new ChatNettyClientHandler(bf));
	   
	}
}