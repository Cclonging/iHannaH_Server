package per.lijun.hannah.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import per.lijun.hannah.entity.User;
import per.lijun.hannah.entity.vo.UserVo;
import per.lijun.hannah.mapper.MyFriendsMapperCustom;
import per.lijun.hannah.mapper.UserMapper;
import per.lijun.hannah.service.FriendService;


import java.util.List;
import java.util.Objects;

@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MyFriendsMapperCustom myFriendsMapperCustom;


    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<UserVo> queryAllMyFriends(String userid) {
        //0.需要检查这个id的用户是否存在
        User temp = userMapper.selectByPrimaryKey(userid);
        if (Objects.isNull(temp))
            return null;


        return myFriendsMapperCustom.selectAllmyFriends(userid);
    }

}
