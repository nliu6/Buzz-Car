package gatech.cs.buzzcar.utils;

import gatech.cs.buzzcar.common.UserHolder;

public class UserUtils {

    public static String getUsername(){
        return UserHolder.getCurrentUser().get().getUsername();
    }

    public static String getUserRole(){
        return UserHolder.getCurrentUser().get().getRole();
    }
}
