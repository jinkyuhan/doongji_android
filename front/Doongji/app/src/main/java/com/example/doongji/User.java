package com.example.doongji;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private static String name;
    private static String token;

    private static ArrayList<Group> myGroups = new ArrayList<Group>();

    public static String getToken() {
        return token;
    }
    public static String getName() {
        return name;
    }

    public static void initialize() {
        User.name = null;
        User.token = null;
        User.myGroups.clear();
    }

    public static void setInfo(String name, String token) {
        User.name = name;
        User.token = token;
    }

    public static ArrayList<Group> getMyGroups() {
        return myGroups;
    }
    public static void clearMygroups() {
        myGroups.clear();
    }

    public static Group getGroupById(int group_id) {
        for (Group grp : myGroups) {
            if (grp.getId() == group_id) {
                return grp;
            }
        }
        return null;
    }
    public static void addGroup(Group _group) {
        User.myGroups.add(_group);
    }
    public static void exitGroup(Group _group) {
        User.myGroups.remove(_group);
    }
}
