package per.lijun.hannah.mapper;

import per.lijun.hannah.entity.User;
import per.lijun.hannah.entity.vo.FriendRequestVo;
import per.lijun.hannah.base.MyMapper;


import java.util.List;

public interface UserMapperCustom extends MyMapper<User> {

    List<FriendRequestVo> sqlFriendRequestList(String acceptUserid);
}