package per.lijun.hannah.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
import per.lijun.hannah.entity.User;
import per.lijun.hannah.base.MyMapper;

public interface UserMapper extends MyMapper<User> {

    @Update("update user set isOnline = #{param2} where id = #{param1}")
    void updateIsOnline(String userId, int isOnline);

}