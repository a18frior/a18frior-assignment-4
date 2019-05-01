package com.example.brom.listviewjsonapp;

import org.json.JSONArray;

import java.lang.reflect.Array;

public class Mountain {

    private String name;
    private String location;
    private int height;
    private String img;

    public Mountain(String inName, String inLocation, int inHeight, String inImg){
        name=inName;
        location=inLocation;
        height=inHeight;
        img=inImg;

    }

    public Mountain(String inName){
        name=inName;
        location="";
        height=-1;
    }

    @Override
    public String toString() {
        return name;
    }

    public String info(){
        String str=name;
        str+=" is located in ";
        str+=location;
        str+= " and has an height of ";
        str+= Integer.toString(height);
        str+="m. ";

        return str;
    }

    public String bild(){
        String bild =img;
        return bild;
    }

    public void setHeight(int newHeight){
        height=newHeight;
    }

    public String getName(){
        String getname= name;
    return getname;
    }

    public String getLocation(){
        String getloc= location ;
        return getloc;
    }
    public String getHeight(){
        String getheight= height+ "m" ;
        return getheight;
    }
}