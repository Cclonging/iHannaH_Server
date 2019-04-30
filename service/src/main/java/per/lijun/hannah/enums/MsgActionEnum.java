package per.lijun.hannah.enums;

public enum MsgActionEnum {
    CONNECT(1, "第一次(或重连)初始化连接"),
    CHAT(2, "聊天消息"),
    SIGNED(3, "消息签收"),
    KEEPALIVE(4, "客户端保持心跳"),
    PULL_FRIEND(5, "拉取好友");

    private Integer type;
    private String content;

    MsgActionEnum(Integer type, String content) {
        this.type = type;
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public static String getContentByType(Integer type){
        for (MsgActionEnum ms : MsgActionEnum.values()){
            if (ms.type == type){
                return ms.content;
            }
        }
        return null;
    }
}
