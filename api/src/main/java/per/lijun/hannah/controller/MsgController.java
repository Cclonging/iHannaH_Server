package per.lijun.hannah.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import per.lijun.hannah.service.ChatMsgService;
import per.lijun.hannah.utils.JSONResult;
import per.lijun.hannah.utils.JsonUtils;

@RestController
@RequestMapping("/chat")
public class MsgController {

    @Autowired
    private ChatMsgService chatMsgService;

    @GetMapping("/initMsg")
    public JSONResult initMsg(String receiverId){
        return JSONResult.ok(chatMsgService.getNoReadingMsg(receiverId));
    }
}
