package com.example.doongji;

import java.util.ArrayList;

public class Group {
    private int id;
    private String name;
    private double xpos;
    private double ypos;
    private ArrayList<Member> members = new ArrayList<Member>();

    public Group(int id, String name, double xpos, double ypos) {
        this.id = id;
        this.name = name;
        this.xpos = xpos;
        this.ypos = ypos;
    }

    public int getId(){
        return id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getXpos() {
        return xpos;
    }
    public double getYpos() {
        return ypos;
    }

    public void setPos(double xpos, double ypos) {
        this.xpos = xpos;
        this.ypos = ypos;
    }
    public void clearMember(){
        this.members.clear();
    }
    public void addMember(Member member){
        this.members.add(member);
    }
    public ArrayList<Member> getMembers(){
        return this.members;
    }
}
