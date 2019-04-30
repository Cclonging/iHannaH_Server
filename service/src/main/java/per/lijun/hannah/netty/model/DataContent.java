package per.lijun.hannah.netty.model;



import java.io.Serializable;

public class DataContent implements Serializable {

    private static final long serialVersionUID = 5277428097173511652L;

    private Integer action; //动作类型

    private ChatContent chatContent; //具体信息

    private String extand; //扩展字段

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public ChatContent getChatContent() {
        return chatContent;
    }

    public void setChatContent(ChatContent chatContent) {
        this.chatContent = chatContent;
    }

    public String getExtand() {
        return extand;
    }

    public void setExtand(String extand) {
        this.extand = extand;
    }
}
