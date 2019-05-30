package per.lijun.hannah.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import per.lijun.hannah.netty.initialzer.WsServerInitialzer;

public class WsServer {

    private Logger logger = LoggerFactory.getLogger(WsServer.class);

    private EventLoopGroup mainGroup;

    private EventLoopGroup subGroup;

    private ServerBootstrap server;

    private ChannelFuture channelFuture;

    private static class SingletionWsServer{
        static final WsServer INSTANCE = new WsServer();
    }

    public static WsServer getInstance(){
        return SingletionWsServer.INSTANCE;
    }

    public WsServer(){
        mainGroup = new NioEventLoopGroup();
        subGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(mainGroup,subGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WsServerInitialzer());
    }

    public void start(){
        channelFuture = server.bind(8880);
        logger.info(server + "netty websocket start....");
    }

}
