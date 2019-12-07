package com.feng.purchaseandsalems.constant;

import com.feng.purchaseandsalems.entity.User;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/7
 */
public class UserInfo {
    private static User sUser;

    public static void setUser(User user) {
        sUser = user;
    }

    public static User getUser() {
        return sUser;
    }

    public static void clearUser() {
        sUser = null;
    }
}
