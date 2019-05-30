package per.lijun.hannah.mapper;

import org.apache.ibatis.annotations.Delete;
import per.lijun.hannah.entity.ChatMsg;
import per.lijun.hannah.base.MyMapper;


import java.util.List;

public interface ChatMsgMapper extends MyMapper<ChatMsg> {

    //批量签收消息
    void batchUpdateMsgFlag(List<String> msgList);

    //批量签收消息
    void batchSignMsgFlag(String receiverId);

    //批量签收消息
    void updatePushMsgFlag(String id);

    //根据id获取未读信息
    List<ChatMsg> getNoReadingMsg(String receiverId);

    @Delete("delete from chat_msg where send_user_id = #{param1} and accept_user_id = #{param2}")
    void deleteChatMsg(String myId, String friendId);
}