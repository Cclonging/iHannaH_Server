package per.lijun.hannah.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import per.lijun.hannah.service.FriendService;
import per.lijun.hannah.utils.JSONResult;

import java.util.Objects;

@RestController
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @GetMapping("/getAllMyFriends")
    public JSONResult getAllMyFriends(String userid){
        if (Objects.isNull(userid))
            return JSONResult.errorMsg("");
        return JSONResult.ok(friendService.queryAllMyFriends(userid));
    }
}
