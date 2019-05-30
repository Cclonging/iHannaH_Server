package per.lijun.hannah.netty.model.relationship;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;
/**
 * User and Channel's map
 */
public class UserChannelRel {

    private static ConcurrentHashMap<String, Channel> manager = new ConcurrentHashMap<>(20);

    public static void put(String userid, Channel channel){
        manager.put(userid, channel);
    }

    public static Channel get(String userid){
        return manager.get(userid);
    }

    public static void print(){
        for (ConcurrentHashMap.Entry<String, Channel> entry : manager.entrySet()){
            System.out.println("<Userid: " + entry.getKey() + ", Channelid: " + entry.getValue() + ">");
        }
    }

}
