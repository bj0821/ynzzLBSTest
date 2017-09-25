package com.ynzz.carmanager.ui.bean;

/**
 * Created by yanhao on 2017/8/10.
 */

public class StatusIinfo {

    private String id;

    private String name;

    private int isChoose=0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsChoose() {
        return isChoose;
    }

    public void setIsChoose(int isChoose) {
        this.isChoose = isChoose;
    }
}
