package com.example.doongji;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private static String id;
    private static String name;
    private static ArrayList<Group> myGroups = new ArrayList<Group>();

    public static String getId() {
        return id;
    }
    public static String getName() {
        return name;
    }

    public static void initialize(){
        User.id = null;
        User.name = null;
        User.myGroups.clear();
    }

    public static void setInfo(String id,String name) {
        User.id = id;
        User.name=name;
    }
    public static ArrayList<Group> getMyGroups(){
        return myGroups;
    }
    public static void clearMygroups(){
        myGroups.clear();
    }
    public static Group getGroupById(int group_id){
        for (Group grp : myGroups){
            if(grp.getId() == group_id){
                return grp;
            }
        }
        return null;
    }

    public static void addGroup(Group _group){
        User.myGroups.add(_group);
    }
    public static void exitGroup(Group _group){
        User.myGroups.remove(_group);
    }
}
