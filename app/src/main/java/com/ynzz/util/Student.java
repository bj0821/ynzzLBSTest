package com.ynzz.util;

/**
 * Created by ynzz on 2017/8/29.
 */

public class Student {
    private String name;
    private int age;
    private char sex;

    public Student(){}
    public Student(String name, int age, char sex) {
        super();
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "name:"+this.name+" \tage:"+this.age+" \t sex:"+this.sex;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public char getSex() {
        return sex;
    }
    public void setSex(char sex) {
        this.sex = sex;
    }

}
