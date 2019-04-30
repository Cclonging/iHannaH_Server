package per.lijun.hannah.utils;

import per.lijun.hannah.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import per.lijun.hannah.entity.MyFriends;

@Component
public class BusinessUtils {

    @Autowired
    private Sid sid;

    public MyFriends[] addFriend( String id1, String id2){
        MyFriends[] myFriends = new MyFriends[2];
        for (int i = 0; i < myFriends.length; i ++){
            String id = sid.nextShort();
            myFriends[i] = new MyFriends();
            myFriends[i].setId(id);
        }

        myFriends[0].setMyUserId(id1);
        myFriends[0].setMyFriendUserId(id2);

        myFriends[1].setMyUserId(id2);
        myFriends[1].setMyFriendUserId(id1);

        return myFriends;
    }


}
