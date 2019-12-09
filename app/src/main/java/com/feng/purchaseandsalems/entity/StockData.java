package com.feng.purchaseandsalems.entity;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/9
 */
public class StockData {
    private int id;
    private int autoPartsId;
    private int num;
    private String storehouseName;
    private String storehouseAddress;

    public StockData(int autoPartsId, int num, String storehouseName, String storehouseAddress) {
        this.autoPartsId = autoPartsId;
        this.num = num;
        this.storehouseName = storehouseName;
        this.storehouseAddress = storehouseAddress;
    }

    public StockData(int id, int autoPartsId, int num, String storehouseName, String storehouseAddress) {
        this.id = id;
        this.autoPartsId = autoPartsId;
        this.num = num;
        this.storehouseName = storehouseName;
        this.storehouseAddress = storehouseAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAutoPartsId() {
        return autoPartsId;
    }

    public void setAutoPartsId(int autoPartsId) {
        this.autoPartsId = autoPartsId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getStorehouseName() {
        return storehouseName;
    }

    public void setStorehouseName(String storehouseName) {
        this.storehouseName = storehouseName;
    }

    public String getStorehouseAddress() {
        return storehouseAddress;
    }

    public void setStorehouseAddress(String storehouseAddress) {
        this.storehouseAddress = storehouseAddress;
    }
}
