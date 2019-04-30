package per.lijun.hannah.service;

import per.lijun.hannah.entity.User;
import per.lijun.hannah.entity.bo.FriendsRequestBo;
import per.lijun.hannah.entity.vo.FriendRequestVo;

import java.util.List;

public interface UserService {

    /**
     * 判断用户名和密码
     * @param username
     * @return
     */

    boolean isExit(String username);

    /**
     * 查询登录
     * @param username
     * @param password
     * @return
     */
    User queryUserForLogin(String username, String password);


    /**
     * 用户注册业务
     * @param user
     * @return
     */
    User saveUser(User user);

    /**
     * 修改用户记录
     * @param user
     */
    User updateUserInfo(User user);

    /**
     * 通过id查询user
     * @param id
     * @return
     */
    User queryUserById(String id);

    /**
     * 前置条件, 搜索用户
     * @param myUserId
     * @param friendName
     * @return
     */
    Integer preCondintionSearchFriends(String myUserId, String friendName);

    /**
     * 根据username查询
     * @param username
     * @return
     */
    User queryByUsername(String username);

    /**
     * 发送添加好友请求记录保存到数据库
     * @param myUserId
     * @param friendName
     */
    void sendAddFriendRequest(String myUserId, String friendName);


    /**
     * 查找个人的好友请求
     * @param acceptUserid
     * @return
     */
    List<FriendRequestVo> queryFriendRequest(String acceptUserid);


    String operFriendRequest(FriendsRequestBo friendsRequestBo);


}
