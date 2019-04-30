package per.lijun.hannah.mapper;

import per.lijun.hannah.entity.User;
import per.lijun.hannah.entity.vo.UserVo;
import per.lijun.hannah.base.MyMapper;

import java.util.List;

public interface MyFriendsMapperCustom extends MyMapper<User> {

    List<UserVo> selectAllmyFriends(String userid);

}
