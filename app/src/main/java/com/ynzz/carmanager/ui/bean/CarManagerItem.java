package com.ynzz.carmanager.ui.bean;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class CarManagerItem implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6465237897027410019L;
    /**
     * 栏目ID
     */
    public String pkid;
    /**
     * 栏目NAME
     */
    public String name;
    /**
     * 是否可通行状态0地面1地下
     */
    public String pass;

    /**
     * 是否选择0否1是
     */
    private int isChoose=0;


    public CarManagerItem() {
    }

    public CarManagerItem(String pkid, String name, String pass) {
        this.pkid = pkid;
        this.name = name;
        this.pass = pass;
    }

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getIsChoose() {
        return isChoose;
    }

    public void setIsChoose(int isChoose) {
        this.isChoose = isChoose;
    }
}