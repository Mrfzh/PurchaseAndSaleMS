package com.feng.purchaseandsalems.entity;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/9
 */
public class StockSecondData {
    private int id;
    private int num;
    private String storehouseName;
    private String storehouseAddress;
    private String name;
    private String use;
    private float price;

    public StockSecondData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "StockSecondData{" +
                "id=" + id +
                ", num=" + num +
                ", storehouseName='" + storehouseName + '\'' +
                ", storehouseAddress='" + storehouseAddress + '\'' +
                ", name='" + name + '\'' +
                ", use='" + use + '\'' +
                ", price=" + price +
                '}';
    }
}
