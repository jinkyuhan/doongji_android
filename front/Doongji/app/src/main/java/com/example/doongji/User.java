package com.example.doongji;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

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

    public static void initialize() {
        User.id = null;
        User.name = null;
        User.myGroups.clear();
    }

    public static void setInfo(String id, String name) {
        User.id = id;
        User.name = name;
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

    public static void unsubscribeMyId() {
        if (User.id != null) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(User.id);
        }
    }

    public static void unsubscribeMyGroups() {
        if (myGroups.size() != 0)
            for (Group grp : myGroups) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Integer.toString(grp.getId()));
            }
    }

    public static void unsubscribeMyGroupById(int group_id){
        Group target = User.getGroupById(group_id);
        if(target != null){
            FirebaseMessaging.getInstance().unsubscribeFromTopic(Integer.toString(group_id));
        }
    }

    public static void clearMySubscribeTopics() {
        unsubscribeMyId();
        unsubscribeMyGroups();
    }

    public static void subscribeMyId() {
        if (User.id != null)
            FirebaseMessaging.getInstance().subscribeToTopic(User.id);
    }

    public static void subscribeMyGroups() {
        if (myGroups.size() != 0)
            for (Group grp : myGroups) {
                FirebaseMessaging.getInstance().subscribeToTopic(Integer.toString(grp.getId()));
            }
    }
}
