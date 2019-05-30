package per.lijun.hannah.netty.model.relationship;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

public class ChannelUserRel {

    private static ConcurrentHashMap<String, String> manager = new ConcurrentHashMap<>(20);

    public static void put(String channel, String userId){
        manager.put(channel, userId);
    }

    public static String get(String channel){
        return manager.get(channel);
    }

    public static void print(){
        for (ConcurrentHashMap.Entry<String, String> entry : manager.entrySet()){
            System.out.println("<Channel: " + entry.getKey() + ", User: " + entry.getValue() + ">");
        }
    }
}
