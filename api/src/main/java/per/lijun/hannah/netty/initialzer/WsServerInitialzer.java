package per.lijun.hannah.netty.initialzer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import per.lijun.hannah.netty.handler.ChatHandler;
import per.lijun.hannah.netty.handler.HeartBeatHandler;

public class WsServerInitialzer extends ChannelInitializer<SocketChannel> {

    private static  int IDLE_READ_SECONDS = 20;

    private static  int IDLE_WRITE_SECONDS = 40;

    private static  int IDLE_ALL_SECONDS = 60;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //==================================支持http协议=======================================
        //webscoket基于http协议,所以需要http编解码器
        pipeline.addLast(new HttpServerCodec());

        //对写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        //对httpmessage进行聚合,聚合成fullHttpRequest或fullHttpResponse, 几乎在netty中的编程中都会使用handler
        pipeline.addLast(new HttpObjectAggregator(1024*64));
        //====================================================================================

        //==================================增加自定义的心跳检测机制=======================================
        //设置读, 写, 读写空闲的限制时间
        pipeline.addLast(new IdleStateHandler(IDLE_READ_SECONDS,IDLE_WRITE_SECONDS,IDLE_ALL_SECONDS));
        pipeline.addLast(new HeartBeatHandler());
        //====================================================================================


        //websocket 服务器处理的协议, 并且指定给客户端访问的路由 /ws
        //本handler会帮助处理繁重复杂的工作, 比如握手(close,ping,pong) ping + pong = hreatbreak
        //对于websocket都会以frames进行传输,不同的数据类型对应的frames也不同
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        //自定义的handler
        pipeline.addLast(new ChatHandler());
    }
}
