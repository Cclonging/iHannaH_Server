package per.lijun.hannah.service;

import per.lijun.hannah.entity.ChatMsg;
import per.lijun.hannah.netty.model.ChatContent;

import java.util.List;

public interface ChatMsgService {

    //保存聊天记录
    String saveIntoChatMsg(ChatContent chatContent);

    //批量签收聊天信息
    void updateChatMsgFlag(List<String> msgList);

    void updatePushMsgFlag(String msgId);

    //获取用户的未读信息
    List<ChatMsg> getNoReadingMsg(String receiverId);

}
