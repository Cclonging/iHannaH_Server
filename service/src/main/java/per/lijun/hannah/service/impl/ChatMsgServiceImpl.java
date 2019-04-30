package per.lijun.hannah.service.impl;

import per.lijun.hannah.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import per.lijun.hannah.entity.ChatMsg;
import per.lijun.hannah.enums.MsgSignFlagEnum;
import per.lijun.hannah.mapper.ChatMsgMapper;
import per.lijun.hannah.netty.model.ChatContent;
import per.lijun.hannah.service.ChatMsgService;


import java.beans.Transient;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ChatMsgServiceImpl implements ChatMsgService {

    @Autowired
    private ChatMsgMapper chatMsgMapper;

    @Autowired
    private Sid sid;


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveIntoChatMsg(ChatContent chatContent) {
        if (!Objects.isNull(chatContent)) {
            ChatMsg chatMsg = new ChatMsg();
            chatMsg.setSendUserId(chatContent.getSenderId());
            chatMsg.setAcceptUserId(chatContent.getReceiverId());
            chatMsg.setCreateTime(new Date());
            chatMsg.setMsg(chatContent.getContent());
            chatMsg.setSignFlag(MsgSignFlagEnum.unsign.type);
            chatMsg.setId(sid.nextShort());
            chatMsg.setType(chatContent.getType());
            chatMsgMapper.insert(chatMsg);
            return chatMsg.getId();
        }else{
            //TODO
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateChatMsgFlag(List<String> msgList) {
        chatMsgMapper.batchUpdateMsgFlag(msgList);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePushMsgFlag(String msgId) {
        chatMsgMapper.updatePushMsgFlag(msgId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<ChatMsg> getNoReadingMsg(String receiverId) {
        if (receiverId != null){
            List<ChatMsg> list =  chatMsgMapper.getNoReadingMsg(receiverId);
            //再修改所有的签收状态
            chatMsgMapper.batchSignMsgFlag(receiverId);
            return list;
        }
        return null;
    }
}
