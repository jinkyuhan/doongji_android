package com.example.doongji;

public class User {
    private static String id;
    private static String name;

    public static String getId() {
        return id;
    }

    public static String getName() {
        return name;
    }

    public static void setInfo(String id,String name) {
        User.id = id;
        User.name=name;
    }

}
