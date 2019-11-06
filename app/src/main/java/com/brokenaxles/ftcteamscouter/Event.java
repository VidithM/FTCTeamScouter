package com.brokenaxles.ftcteamscouter;
import java.util.*;

public class Event {

    private List<Team> teamList;
    private String name, location;
    private int teamCnt;


    public Event(){teamList = new ArrayList<>(); teamCnt = 0;}
    public void setName(String name){
        this.name = name;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public void addTeam(Team j){
        teamList.add(j);
    }
    public void setTeams(List<Team> j){
        teamList = new ArrayList<>();
        for(Team idx : j){
            teamList.add(idx);
        }
    }
    public List<Team> getTeams(){
        return teamList;
    }



    public String getName(){
        return name;
   }

   public String getLocation(){
        return location;
   }

   public String toString(){
        return name;
   }
}
