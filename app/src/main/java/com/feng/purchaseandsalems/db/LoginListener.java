package com.feng.purchaseandsalems.db;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/6
 */
public interface LoginListener {
    void loginSuccess();
    void loginError(String errorMsg);
}
