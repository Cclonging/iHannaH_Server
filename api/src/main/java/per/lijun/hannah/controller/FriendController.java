package per.lijun.hannah.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import per.lijun.hannah.service.ChatMsgService;
import per.lijun.hannah.service.FriendService;
import per.lijun.hannah.utils.JSONResult;

import javax.annotation.security.PermitAll;
import java.util.Objects;

@RestController
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private ChatMsgService chatMsgService;

    @GetMapping("/getAllMyFriends")
    public JSONResult getAllMyFriends(String userid){
        if (Objects.isNull(userid))
            return JSONResult.errorMsg("");
        return JSONResult.ok(friendService.queryAllMyFriends(userid));
    }

    @PostMapping("/deleteFriend/{myId}/{friendId}/action")
    public JSONResult deleteFriend(@PathVariable("myId") String myId, @PathVariable("friendId") String friendId){
        if (Objects.isNull(myId) ||Objects.isNull(friendId)){
            return JSONResult.errorMsg("params are null");
        }
        friendService.removeFriend(myId, friendId);
        chatMsgService.removeChatMsg(myId, friendId);
        return JSONResult.ok("删除成功");
    }
}
