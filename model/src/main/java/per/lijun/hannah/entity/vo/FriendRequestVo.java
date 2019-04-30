package per.lijun.hannah.entity.vo;

public class FriendRequestVo {

    private String requestid;

    private String sendUserId;

    private String sendUsername;

    private String sendUserFace;

    private String sendNickname;

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUsername() {
        return sendUsername;
    }

    public void setSendUsername(String sendUsername) {
        this.sendUsername = sendUsername;
    }

    public String getSendUserFace() {
        return sendUserFace;
    }

    public void setSendUserFace(String sendUserFace) {
        this.sendUserFace = sendUserFace;
    }

    public String getSendNickname() {
        return sendNickname;
    }

    public void setSendNickname(String sendNickname) {
        this.sendNickname = sendNickname;
    }

    @Override
    public String toString() {
        return "FriendRequestVo{" +
                "sendUserId='" + sendUserId + '\'' +
                ", sendUsername='" + sendUsername + '\'' +
                ", sendUserFace='" + sendUserFace + '\'' +
                ", sendNickname='" + sendNickname + '\'' +
                '}';
    }
}
