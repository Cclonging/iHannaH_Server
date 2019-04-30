package per.lijun.hannah.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 继承ChannelInboundHandlerAdapter, 用于检测handler的心跳
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {


    /**
     * 重写userEventTriggered方法, 对心跳监测结果做出相应的应对策略
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        Logger logger = LoggerFactory.getLogger(HeartBeatHandler.class);

        //A user event triggered is idle
        if (evt instanceof IdleStateEvent) {
            //find out which state the idle is in
            IdleStateEvent idle = (IdleStateEvent) evt;
            switch (idle.state()) {
                case READER_IDLE:
                    //读空闲
                    //logger.info("<" + ctx.channel().id().asShortText() + ">Channel goes into reading idle..");
                    break;
                case WRITER_IDLE:
                    //写空闲
                    //logger.info("<" + ctx.channel().id().asShortText() + ">Channel goes into writing idle..");
                    break;
                case ALL_IDLE:
                    //读写空闲, 关闭channel
                    logger.info("Before closing ALL_IDLE channels, There are " + ChatHandler.clientChannels.size() + " channels in this netty server");
                    Channel currentChannel = ctx.channel();
                    ctx.close();
                    logger.info("After closing ALL_IDLE channels, There are " + ChatHandler.clientChannels.size() + " channels in this netty server");
                    break;
                default:
                    break;
            }
        }
    }
}
