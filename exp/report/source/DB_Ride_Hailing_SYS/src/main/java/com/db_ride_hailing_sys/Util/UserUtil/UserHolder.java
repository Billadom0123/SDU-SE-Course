package com.db_ride_hailing_sys.Util.UserUtil;

import com.db_ride_hailing_sys.dto.UserDTO;

public class UserHolder
{
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserDTO user){
        tl.set(user);
    }

    public static UserDTO getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
