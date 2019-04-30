package per.lijun.hannah.service.impl;

import per.lijun.hannah.com.FastDFSClient;
import per.lijun.hannah.com.FileUtils;
import per.lijun.hannah.idworker.Sid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import per.lijun.hannah.entity.FriendsRequest;
import per.lijun.hannah.entity.MyFriends;
import per.lijun.hannah.entity.User;
import per.lijun.hannah.entity.bo.FriendsRequestBo;
import per.lijun.hannah.entity.vo.FriendRequestVo;
import per.lijun.hannah.enums.OperFriendsRequestEnum;
import per.lijun.hannah.enums.SearchFriendsStatusEnum;
import per.lijun.hannah.mapper.FriendsRequestMapper;
import per.lijun.hannah.mapper.MyFriendsMapper;
import per.lijun.hannah.mapper.UserMapper;
import per.lijun.hannah.mapper.UserMapperCustom;
import per.lijun.hannah.service.UserService;
import per.lijun.hannah.utils.*;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private Sid sid;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MyFriendsMapper myFriendsMapper;

    @Autowired
    private FriendsRequestMapper friendsRequestMapper;

    @Autowired
    private QRCodeUtils qrCodeUtils;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private UserMapperCustom userMapperCustom;

    @Autowired
    private BusinessUtils businessUtils;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean isExit(String username) {

        User user = new User();
        user.setUsername(username);

        User result = userMapper.selectOne(user);
        return result != null ? true : false;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User queryUserForLogin(String username, String password) {
        Example userExample = new Example(User.class);

        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username",username);
        criteria.andEqualTo("password", password);
        User result = userMapper.selectOneByExample(userExample);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User saveUser(User user) {

        //生成用户的唯一id
        String userid = sid.nextShort();

        //生成唯一的二维码
        String qrcode_path = Content.QR_CODE_PATH + user.getUsername() + Content.PNG;
        qrCodeUtils.createQRCode(qrcode_path,Content.IHANNAH_QRCODE + user.getUsername());
        MultipartFile qrCodeFile = FileUtils.fileToMultipart(qrcode_path);
        String qrCodeUrl = "";
        try {
            qrCodeUrl = fastDFSClient.uploadQRCode(qrCodeFile);
            logger.info("上传 " + user.getUsername() + "的 qrcode");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ihannah_qrcode
        user.setQrcode(qrCodeUrl);
        user.setId(userid);

        userMapper.insert(user);
        return user;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User updateUserInfo(User user) {
        userMapper.updateByPrimaryKeySelective(user);
        return queryUserById(user.getId());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User queryUserById(String id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer preCondintionSearchFriends(String myUserId, String friendName) {
        User user = queryByUsername(friendName);
        if (Objects.isNull(user)){
            return SearchFriendsStatusEnum.USER_NOT_EXIT.status;
        }else if (Objects.equals(myUserId, user.getId())){
            return SearchFriendsStatusEnum.NOT_YOURSELF.status;
        }else {
            MyFriends myFriends = queryUserFriends(myUserId, user);
            if (!Objects.isNull(myFriends)) {
                return SearchFriendsStatusEnum.ALREADY_FRIEND.status;
            }
        }

        return SearchFriendsStatusEnum.SUCCESS.status;
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public User queryByUsername(String username){
        Example uf = new Example(User.class);
        Example.Criteria criteria = uf.createCriteria();
        criteria.andEqualTo("username",username);
        return userMapper.selectOneByExample(uf);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void sendAddFriendRequest(String myUserId, String friendName) {
        //0.判断是否已经发送了这个请求了
        // 获取朋友的信息
        User friend = queryByUsername(friendName);

        //查询发送好友请求列表
        Example friendsRequestExample = new Example(FriendsRequest.class);
        Example.Criteria criteria = friendsRequestExample.createCriteria();
        criteria.andEqualTo("sendUserId",myUserId);
        criteria.andEqualTo("acceptUserId",friend.getId());
        FriendsRequest friendsRequest = friendsRequestMapper.selectOneByExample(friendsRequestExample);

        //1. 如果已经请求了,那么修改请求时间为当前时间
        //2. 如果没有请求,则插入数据库
        if (Objects.isNull(friendsRequest)){
            String id = sid.nextShort();
            logger.info("新增" + id + "的发送请求");
            FriendsRequest temp = new FriendsRequest();
            temp.setSendUserId(myUserId);
            temp.setAcceptUserId(friend.getId());
            temp.setRequestDateTime(new Date());
            temp.setId(id);
            temp.setStatus("0");

            //插入数据库
            friendsRequestMapper.insert(temp);
        }else{
            logger.info("更新" + friendsRequest.getId() + "的发送请求");
            friendsRequest.setRequestDateTime(new Date());
            friendsRequest.setStatus("0");
            //更新数据库
            friendsRequestMapper.updateByPrimaryKeySelective(friendsRequest);
        }


    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<FriendRequestVo> queryFriendRequest(String acceptUserid) {
        return userMapperCustom.sqlFriendRequestList(acceptUserid);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String operFriendRequest(FriendsRequestBo friendsRequestBo) {
        String status = friendsRequestBo.getStatus();
        //通过requestid修改friends_request表中的状态
        if (!Objects.equals("1",status) && !Objects.equals("2",status))
            return "error!!!";
        FriendsRequest friendsRequest = new FriendsRequest();
        friendsRequest.setId(friendsRequestBo.getId());
        friendsRequest.setStatus(status);
        friendsRequestMapper.updateByPrimaryKeySelective(friendsRequest);

        //添加好友, 需要双重添加
        User user = new User();
        user.setId(friendsRequestBo.getMyFriendid());
        if (Objects.equals("1",status) && Objects.isNull(queryUserFriends(friendsRequestBo.getMyUserid(),user))){
            for (MyFriends myFriend :
                    businessUtils.addFriend(friendsRequestBo.getMyUserid(),friendsRequestBo.getMyFriendid()))
                myFriendsMapper.insert(myFriend);
        }
        return OperFriendsRequestEnum.getMsg(Integer.valueOf(status));
    }

    public MyFriends queryUserFriends(String userid, User user){
        Example myfriends = new Example(MyFriends.class);
        Example.Criteria fc = myfriends.createCriteria();
        fc.andEqualTo("myUserId", userid);
        fc.andEqualTo("myFriendUserId",user.getId());
        return myFriendsMapper.selectOneByExample(myfriends);
    }



}
