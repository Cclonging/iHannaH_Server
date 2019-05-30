package per.lijun.hannah.service;

import org.springframework.stereotype.Service;
import per.lijun.hannah.entity.vo.UserVo;

import java.util.List;

@Service
public interface FriendService {


    /**
     * 查找当前用户的所有好友
     * @param userid
     * @return
     */
    List<UserVo> queryAllMyFriends(String userid);

    void removeFriend(String MyId, String friendId);
}
