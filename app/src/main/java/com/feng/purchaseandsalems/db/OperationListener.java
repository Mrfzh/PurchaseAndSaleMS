package com.feng.purchaseandsalems.db;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/8
 */
public interface OperationListener {
    void success();
    void error(String errorMsg);
}
