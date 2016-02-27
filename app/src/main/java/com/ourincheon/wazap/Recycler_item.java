package com.ourincheon.wazap;

/**
 * Created by Youngdo on 2016-01-19.
 */
public class Recycler_item {

    private String title;
    private String text;
    private String name;
    private int recruit;
    private int member;


    String getTitle(){
        return this.title;
    }

    String getText(){
        return this.text;
    }
    String getName(){
        return this.name;
    }

    public int getRecruit() {
        return recruit;
    }

    public int getMember() {
        return member;
    }

    Recycler_item(String title, String text, String name, int recruit,int member){
        this.title=title;
        this.text=text;
        this.name=name;
        this.recruit = recruit;
        this.member = member;
    }
}
