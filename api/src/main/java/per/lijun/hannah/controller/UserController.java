package per.lijun.hannah.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import per.lijun.hannah.com.FastDFSClient;
import per.lijun.hannah.com.FileUtils;
import per.lijun.hannah.entity.User;
import per.lijun.hannah.entity.bo.FriendsRequestBo;
import per.lijun.hannah.entity.bo.ResourceBo;
import per.lijun.hannah.entity.bo.UserBo;
import per.lijun.hannah.entity.vo.UserVo;
import per.lijun.hannah.enums.SearchFriendsStatusEnum;
import per.lijun.hannah.service.UserService;
import per.lijun.hannah.utils.Content;
import per.lijun.hannah.utils.JSONResult;
import per.lijun.hannah.utils.MD5Utils;
import java.util.Objects;

import static per.lijun.hannah.com.FileUtils.uploadRes;

@RestController
@RequestMapping("/u")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private FastDFSClient fastDFSClient;

    /**
     * 注册登录
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping("/regsitOrLogin")
    public JSONResult registOrlogin(@RequestBody User user) throws Exception {
        //0. 判断用户名和密码不能为空
        if (StringUtils.isBlank(user.getUsername())
                || StringUtils.isBlank(user.getPassword())){
            return JSONResult.errorMsg("用户名和密码不能为空");
        }

        //1. 判断user是否存在,存在则登录, 不存在则注册
        boolean isExit = userService.isExit(user.getUsername());
        User result = null;
        if (isExit){
            //1.1 登录
            result = userService.queryUserForLogin(user.getUsername(),MD5Utils.getMD5Str(user.getPassword()));
            if (result == null){
                return JSONResult.errorMsg("用户名或密码不正确");
            }
            logger.info(user.getUsername() + "登录");
        }else {
            //1.2 注册
            user.setNickname(user.getUsername());
            user.setFaceImageBig("");
            user.setFaceImage("");
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            result = userService.saveUser(user);
            logger.info(user.getUsername() + "注册");
        }

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(result,userVo);
        return  JSONResult.ok(userVo);
    }

    /**
     * 上传图片
     * @param user
     * @return
     * @throws Exception
     */
    @RequestMapping("/uploadFaceBase64")
    public JSONResult uploadFaceBase64(@RequestBody UserBo user) throws Exception{
        //获取前端传过来的base64字符串,然后转换成文件对象,上传至fdfs
        String faceBase64 = user.getFaceData();
        String userFaceBase64Path = Content.USER_FACE_PATH + user.getUserId() + Content.BASE64_PNG;
        //上传到fdfs
        FileUtils.base64ToFile(userFaceBase64Path,faceBase64);
        MultipartFile face = FileUtils.fileToMultipart(userFaceBase64Path);

        //大图的url
        String bigUrl = fastDFSClient.uploadBase64(face);

        logger.info("fdfs路径: " + bigUrl);

        //获取缩略图的url
        String suffix = "_100x100.";
        String[] arr = bigUrl.split("\\.");
        String thumpImgUrl = arr[0] + suffix + arr[1];

        //提取数据到user
        User newUser = new User();
        newUser.setId(user.getUserId());
        newUser.setFaceImage(thumpImgUrl);
        newUser.setFaceImageBig(bigUrl);
        //将数据保存至数据库
        User u = userService.updateUserInfo(newUser);

        return JSONResult.ok(u);
    }

    /**
     * 设置昵称
     * @param userBo
     * @return
     */
    @PostMapping("/setNickname")
    public JSONResult setNickName(@RequestBody UserBo userBo){
        logger.info(userBo.getUserId() + "修改昵称为" + userBo.getFaceData());
        User user = new User();
        user.setId(userBo.getUserId());
        user.setNickname(userBo.getFaceData());
        User newUser = userService.updateUserInfo(user);
        return JSONResult.ok(newUser);
    }

    /**
     * 发起查找朋友的请示
     * @param myUserId
     * @param friendName
     * @return
     */
    @PostMapping("/searchFriend")
    public JSONResult searchUser(String myUserId, String friendName){
        logger.info(myUserId + " 查询好友 : " + friendName);
        //0. 首先先判断前端数据不为空
        if (StringUtils.isBlank(myUserId) || StringUtils.isBlank(friendName)){
            return JSONResult.errorMsg("");
        }

        // 1.前置条件,如果搜索的用户不存在, 返回[用户不存在]
        // 2.前置条件,如果搜索的已经是好友了, 返回[不能添加已经添加的好友]
        // 3.前置条件,如果搜索的是自己, 返回[不能添加自己]

        Integer status = userService.preCondintionSearchFriends(myUserId,friendName);
        if (Objects.equals(status,Content.SEARCH_FRIEND_STATUS_ENUM_SUCCESS)){
            User user = userService.queryByUsername(friendName);
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            return JSONResult.ok(userVo);
        }else {
            String result = SearchFriendsStatusEnum.getMsgByStatus(status);
            return JSONResult.errorMsg(result);
        }


    }

    /**
     * 发起请添加好友请求
     * @param myUserId
     * @param friendName
     * @return
     */
    @PostMapping("/addFriendRequest")
    public JSONResult addFriendRequest(String myUserId, String friendName){
        logger.info(myUserId + " 添加好友 : " + friendName);
        //0. 首先先判断前端数据不为空
        if (StringUtils.isBlank(myUserId) || StringUtils.isBlank(friendName)){
            return JSONResult.errorMsg("");
        }

        // 1.前置条件,如果搜索的用户不存在, 返回[用户不存在]
        // 2.前置条件,如果搜索的已经是好友了, 返回[不能添加已经添加的好友]
        // 3.前置条件,如果搜索的是自己, 返回[不能添加自己]

        Integer status = userService.preCondintionSearchFriends(myUserId,friendName);
        if (Objects.equals(status,Content.SEARCH_FRIEND_STATUS_ENUM_SUCCESS)){
            userService.sendAddFriendRequest(myUserId, friendName);
            return JSONResult.ok("发送请求成功");
        }else {
            return JSONResult.errorMsg(SearchFriendsStatusEnum.getMsgByStatus(status));
        }


    }

    /**
     * 查询好友请求
     * @param acceptUserid
     * @return
     */
    @PostMapping("/queryFriendsRequest")
    public JSONResult queryFriendsRequest(String acceptUserid){
        if (Objects.isNull(acceptUserid)){
            return JSONResult.errorMsg("");
        }
        return JSONResult.ok(userService.queryFriendRequest(acceptUserid));
    }

    /**
     * 操作好友请求
     * @param friendsRequestBo
     * @return
     */
    @PostMapping("/operFriendsRequest")
    public JSONResult operFriendsRequest(@RequestBody FriendsRequestBo friendsRequestBo){
        if (Objects.isNull(friendsRequestBo)){
            return JSONResult.errorMsg("添加失败");
        }
        return JSONResult.ok(userService.operFriendRequest(friendsRequestBo));
    }


    @RequestMapping("/uploadSource")
    public JSONResult uploadSource(@RequestBody ResourceBo resourceBo) throws Exception{

        String url = "";
        if (resourceBo.getResourceStr().indexOf("image") != -1){
            String userFaceBase64Path = Content.CHAT_RESOURCE_PATH + resourceBo.getName() + Content.BASE64_PNG;
            url = uploadRes(userFaceBase64Path, resourceBo.getResourceStr(), fastDFSClient);
        }else if (resourceBo.getResourceStr().indexOf("audio") != -1){
            String userFaceBase64Path = Content.CHAT_RESOURCE_PATH + resourceBo.getName() + ".arm";
            url = uploadRes(userFaceBase64Path, resourceBo.getResourceStr(), fastDFSClient);
        }
        logger.info("fdfs路径: " + url);
        return JSONResult.ok(url);
    }



}
