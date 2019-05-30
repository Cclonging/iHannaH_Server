package per.lijun.hannah.mapper;

import org.apache.ibatis.annotations.Delete;
import per.lijun.hannah.entity.MyFriends;
import per.lijun.hannah.base.MyMapper;

public interface MyFriendsMapper extends MyMapper<MyFriends> {
    @Delete("delete from my_friends where my_user_id = #{param1} and my_friend_user_id = #{param2}")
    void deleteFriend(String myId, String friendId);
}