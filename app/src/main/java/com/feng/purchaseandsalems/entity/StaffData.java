package com.feng.purchaseandsalems.entity;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/9
 */
public class StaffData {
    private int id;
    private String name;
    private String contact;

    public StaffData(int id, String name, String contact) {
        this.id = id;
        this.name = name;
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
