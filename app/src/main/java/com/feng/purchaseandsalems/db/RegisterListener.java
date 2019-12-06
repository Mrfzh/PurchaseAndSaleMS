package com.feng.purchaseandsalems.db;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/6
 */
public interface RegisterListener {
    void registerSuccess();
    void registerError(String errorMsg);
}
