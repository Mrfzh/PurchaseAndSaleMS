package com.feng.purchaseandsalems.entity;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/6
 */
public class UserData {
    private String userName;
    private String password;
    private USER_TYPE userType;

    public enum USER_TYPE {
        PURCHASE,   // 进货员
        SALE        // 销售员
    }

    public UserData(String userName, String password, USER_TYPE userType) {
        this.userName = userName;
        this.password = password;
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public USER_TYPE getUserType() {
        return userType;
    }

    public void setUserType(USER_TYPE userType) {
        this.userType = userType;
    }
}
