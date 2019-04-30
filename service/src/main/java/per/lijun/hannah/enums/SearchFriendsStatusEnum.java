package per.lijun.hannah.enums;

public enum  SearchFriendsStatusEnum {

    SUCCESS(0,"OK"),
    USER_NOT_EXIT(1,"用户不存在..."),
    NOT_YOURSELF(2,"不能添加你自己..."),
    ALREADY_FRIEND(3,"已经是你的好友了...");

    public final Integer status;
    public final String msg;

    SearchFriendsStatusEnum (Integer status, String msg){
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus(){
        return status;
    }

    public static String getMsgByStatus(Integer status){
        for (SearchFriendsStatusEnum type : SearchFriendsStatusEnum.values()){
            if (type.getStatus() == status)
                return type.msg;
        }
        return null;
    }
}
