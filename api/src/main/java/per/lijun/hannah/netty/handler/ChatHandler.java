package per.lijun.hannah.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import per.lijun.hannah.enums.MsgActionEnum;
import per.lijun.hannah.netty.model.ChatContent;
import per.lijun.hannah.netty.model.DataContent;
import per.lijun.hannah.netty.model.relationship.UserChannelRel;
import per.lijun.hannah.service.ChatMsgService;
import per.lijun.hannah.service.impl.ChatMsgServiceImpl;
import per.lijun.hannah.utils.JsonUtils;
import per.lijun.hannah.utils.SpringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 聊天handler
 * TextWebSocketFrame: 用于为websocket处理文本的对象,frame是消息的载体
 *
 */
@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    

    //用于记录和管理所有客户端的channel
    static ChannelGroup clientChannels;

    public ChatHandler() {
        clientChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel channel = ctx.channel();
        //1. 获取信息
        String context = msg.text();
        log.info("accept client " + channel.id().asShortText() + "'s message: " + context);
        if (Objects.equals("undefined", context)){
            return;
        }

        Channel currentChannel = ctx.channel();

        DataContent dataContent = JsonUtils.jsonToPojo(context, DataContent.class);
        //判断全双工是否开启, 没有则开启
        if (Objects.isNull(clientChannels.find(channel.id()))){
            log.warn(channel.id().asShortText() + " is not in opening state, so now serve reopen this channel.");
            handlerAdded(ctx);
        }
        //2. 判断类别, 根据不同的类型来处理不同的业务
        Integer action = dataContent.getAction();
        log.info("At this time, client request to " + MsgActionEnum.getContentByType(action));
        // client connect the netty server
        if (action == MsgActionEnum.CONNECT.getType()) {
            //  2.1 当websocket第一次连接时, 初始化channel, 将channel和userid关联起来
            String senderId = dataContent.getChatContent().getSenderId();
            UserChannelRel.put(senderId, currentChannel);

        }// client send msg to netty server... and netty server should send msg to the other client ...
        else if (action == MsgActionEnum.CHAT.getType()) {
            //  2.2 聊天类型的信息, 将信息保存到数据库, 然后标记状态(未读)
            //handler中是无法使用spring的注解的, 所以采用自定义的Spring工具类来完成响应的操作
            ChatMsgService chatMsgService = (ChatMsgService) SpringUtils.getBean("chatMsgServiceImpl");
            ChatContent chatContent = resolveChatContent(dataContent, chatMsgService);
            String receiverId = chatContent.getReceiverId();
            String msgId = chatContent.getMsgId();
            //发送消息
            //获取接收方的channel
            Channel receiverChannel = UserChannelRel.get(receiverId);
            if (Objects.isNull(receiverChannel)){
                //TODO channel为空说明对方离线, 推送消息(个推, JPush, 小米)
                //用户离线
                //信息已经存在了数据库里面了, 修改sign的状态为2
                log.info(receiverId + "关闭了websocket连接, 此时不推送信息");
                chatMsgService.updatePushMsgFlag(msgId);
            }else{
                //TODO channel不为空, 说明对方在线, 从channelGroup中查找对应的channel是否存在, 只有存在才能发送消息
                Channel finalChannel = clientChannels.find(receiverChannel.id());
                if (!Objects.isNull(finalChannel)){
                    //用户在线
                    receiverChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(chatContent)));
                }else {
                    //用户离线
                    //信息已经存在了数据库里面了, 修改sign的状态为2
                    log.info(receiverId + "关闭了websocket连接, 此时不推送信息");
                    chatMsgService.updatePushMsgFlag(msgId);
                }
            }
        }// client signs the msg...
        else if (action == MsgActionEnum.SIGNED.getType()) {

            //  2.3 聊天信息已经被读取, 针对具体信息进行签收, 并且修改数据库中的状态
            ChatMsgService chatMsgService = (ChatMsgService) SpringUtils.getBean(ChatMsgServiceImpl.class);
            // 扩展字段在signed类型的字段中, 代表需要去签收的消息id, 逗号间隔
            String msgIdsStr = dataContent.getExtand();
            String[] msgIds = msgIdsStr.split(",");
            List<String> msgIdList = new ArrayList<>(5);
            for (String msgId : msgIds){
                if (StringUtils.isNotBlank(msgId)) {
                    msgIdList.add(msgId);
                }
            }
            if (!Objects.isNull(msgIdList) && !msgIdList.isEmpty()){
                //如果有需要批量签收的消息
                chatMsgService.updateChatMsgFlag(msgIdList);
            }
        }//heart beat check
        else if (action == MsgActionEnum.KEEPALIVE.getType()) {
            //  2.4 心跳类型的信息处理(自定义)
            log.info(dataContent.getChatContent().getSenderId() + " send heart beat to keep " + channel.id().asShortText() + " connecting...");
        }




    }

    /**
     * 当客户端连接服务器后(打开链接)
     * 获取客户端的channel,并且放到ChannelGroup中
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clientChannels.add(ctx.channel());
    }

    /**
     * 当触发handlerRemoved,client Group会自动移除channel
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        clientChannels.remove(ctx.channel());
        log.info("客户端断开,channel对应的长id:" + ctx.channel().id().asLongText());
        log.info("客户端断开,channel对应的短id:" + ctx.channel().id().asShortText());
    }

    /**
     * 处理channel的异常
     * 1. 关闭channel, 释放资源
     * 2. 将channel从从组中移除
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
        ctx.channel().close();
        clientChannels.remove(ctx.channel());
    }


    public ChatContent resolveChatContent(DataContent dataContent, ChatMsgService chatMsgService){
        ChatContent chatContent = dataContent.getChatContent();
        String content = chatContent.getContent();
        //插入数据并得到其id
        String msgId = chatMsgService.saveIntoChatMsg(chatContent);
        chatContent.setMsgId(msgId);
        chatContent.setContent(per.lijun.illegalRemarks.filter.IrregularitiesFilter.fenci(content, chatContent.getSenderId(), chatContent.getReceiverId()));
        return chatContent;
    }
}


