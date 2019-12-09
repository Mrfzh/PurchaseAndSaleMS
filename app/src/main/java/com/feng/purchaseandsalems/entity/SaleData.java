package com.feng.purchaseandsalems.entity;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/8
 */
public class SaleData {
    private int id;
    private int autoPartsId;
    private int num;
    private String customerName;
    private String customerContact;
    private int staffId;

    public SaleData() {
    }

    public SaleData(int id, int autoPartsId, int num, String customerName, String customerContact, int staffId) {
        this.id = id;
        this.autoPartsId = autoPartsId;
        this.num = num;
        this.customerName = customerName;
        this.customerContact = customerContact;
        this.staffId = staffId;
    }

    public SaleData(int autoPartsId, int num, String customerName, String customerContact, int staffId) {
        this.autoPartsId = autoPartsId;
        this.num = num;
        this.customerName = customerName;
        this.customerContact = customerContact;
        this.staffId = staffId;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }
}
