package com.feng.purchaseandsalems.entity;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/8
 */
public class PurchaseData {
    private int autoPartsId;
    private int num;
    private String factoryName;
    private String factoryAddress;
    private String factoryContact;
    private int staffId;

    public PurchaseData(int autoPartsId, int num, String factoryName,
                        String factoryAddress, String factoryContact, int staffId) {
        this.autoPartsId = autoPartsId;
        this.num = num;
        this.factoryName = factoryName;
        this.factoryAddress = factoryAddress;
        this.factoryContact = factoryContact;
        this.staffId = staffId;
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

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getFactoryAddress() {
        return factoryAddress;
    }

    public void setFactoryAddress(String factoryAddress) {
        this.factoryAddress = factoryAddress;
    }

    public String getFactoryContact() {
        return factoryContact;
    }

    public void setFactoryContact(String factoryContact) {
        this.factoryContact = factoryContact;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }
}
