package per.lijun.hannah.enums;

import java.util.Objects;

public enum OperFriendsRequestEnum {
    AGREE(1,"添加成功"),
    REFUSED(2,"拒绝成功")
    ;
    public final Integer status;
    public final String msg;

    OperFriendsRequestEnum(Integer status, String msg){
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public static String getMsg(Integer status) {
        for (OperFriendsRequestEnum enums : OperFriendsRequestEnum.values()){
            if (Objects.equals(status,enums.getStatus())){
                return enums.msg;
            }

        }
        return null;
    }
}
