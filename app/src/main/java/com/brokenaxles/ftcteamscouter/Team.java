package com.brokenaxles.ftcteamscouter;


import java.util.ArrayList;
import java.util.List;

public class Team implements Comparable<Team> {

    private String teamName;
    private int teamNum;

    //Autonomous vars


    private List<List<Boolean>> autoBoolStats; //0 - skybridge 1 - parking  2 - safe zone  3 - initial pos
    private List<List<Integer>> autoIntStats; //0 - sky  1 - nonsky  2 - foundation

    //TeleOp vars
    private List<List<Integer>> teleIntStats; //0 - foundation  1 - highest stack  2 - stones
    private List<List<Boolean>> teleBoolStats; //0 - capping  1 - parking   2 - zone

    public Team(){
        //Instantiating matrix
        autoBoolStats = new ArrayList<List<Boolean>>();
        autoIntStats = new ArrayList<List<Integer>>();
        teleBoolStats = new ArrayList<List<Boolean>>();
        teleIntStats = new ArrayList<List<Integer>>();

            //Instantiating sublists
        autoBoolStats.add(new ArrayList<Boolean>());
        autoIntStats.add(new ArrayList<Integer>());
        teleBoolStats.add(new ArrayList<Boolean>());
        teleIntStats.add(new ArrayList<Integer>());
        for(int c = 0; c < 3; c++){
            autoBoolStats.get(0).add(false);
            autoIntStats.get(0).add(-1);
            teleBoolStats.get(0).add(false);
            teleIntStats.get(0).add(-1);
        }
        autoBoolStats.get(0).add(false); //Extra slot for initial position

    }

    public void updateBasicInfo(String name, int num){
        teamName = name;
        teamNum = num;
    }

    public void updateAutoInfo(int stg, List<Boolean> boolStats, List<Integer> intStats){

        autoBoolStats.get(stg).set(0, boolStats.get(0));
        autoBoolStats.get(stg).set(1, boolStats.get(1));
        autoBoolStats.get(stg).set(2, boolStats.get(2));
        autoBoolStats.get(stg).set(3, boolStats.get(3));

        autoIntStats.get(stg).set(0, intStats.get(0));
        autoIntStats.get(stg).set(1, intStats.get(1));
        autoIntStats.get(stg).set(2, intStats.get(2));
    }

    public void updateTeleInfo(int stg, List<Boolean> boolStats, List<Integer> intStats){

        teleBoolStats.get(stg).set(0, boolStats.get(0));
        teleBoolStats.get(stg).set(1, boolStats.get(1));
        teleBoolStats.get(stg).set(2, boolStats.get(2));

        teleIntStats.get(stg).set(0, intStats.get(0));
        teleIntStats.get(stg).set(1, intStats.get(1));
        teleIntStats.get(stg).set(2, intStats.get(2));

    }

    @Override
    public int compareTo(Team t){

        return 0;

    }

    public List<List<Boolean>> getAutoBoolStats(){
        return autoBoolStats;
    }
    public List<List<Boolean>> getTeleBoolStats(){ return teleBoolStats; }

    public List<List<Integer>> getAutoIntStats(){
        return autoIntStats;
    }
    public List<List<Integer>> getTeleIntStats(){
        return  teleIntStats;
    }

    public String getTeamName(){return teamName;}
    public int getTeamNum(){return teamNum;}

    public int perfStatsIn(){

        boolean oneOccupied  = false;
        boolean allOccupied = true;

        for(int idx = 0; idx < autoIntStats.size(); idx++){
            boolean all = true;
            for(int num = 0; num < 3; num++){

                if(autoIntStats.get(idx).get(num) == -1 || teleIntStats.get(idx).get(num) == -1){
                    all = false;
                    allOccupied = false;
                    break;
                }
            }
            if(all){
                oneOccupied = true;
            }

        }

        if(oneOccupied && allOccupied){
            return 2; //green
        } else if (oneOccupied){
            return 1; //orange
        } else {
            return 0; //red
        }

    }

    public int getAutoScore(int stg){

        int res = 0;
        List<Integer> intCheck = autoIntStats.get(stg);
        List<Boolean> boolCheck = autoBoolStats.get(stg);

        int cnt = 0;
        for(int idx : intCheck){
            if(idx < 0){
                return -1;
            } else {
                switch (cnt){
                    case 0:
                        res += 10 * idx;
                        break;
                    case 1:
                        res += 2 * idx;
                        break;
                    case 2:
                        res += 4 * idx;
                        break;
                }
            }
            cnt++;
        }
        cnt = 0;
        for(boolean idx : boolCheck){

            int bToInt = 0;
            if(idx){bToInt = 1;}

            switch (cnt){
                case 1:
                    res += 5 * bToInt;
                    break;
                case 2:
                    res += 10 * bToInt;
                    break;
            }
            cnt++;
        }

        return res;
    }

    public int getTeleScore(int stg){

        int res = 0;
        List<Integer> intCheck = teleIntStats.get(stg);
        List<Boolean> boolCheck = teleBoolStats.get(stg);
        int skybridge = 0;
        if(autoBoolStats.get(stg).get(0)){skybridge = 1;}
        int cnt = 0;
        for(int idx : intCheck){
            if(idx < 0){
                return -1;
            } else {
                switch (cnt){
                    case 0:
                        res += idx;
                        break;
                    case 1:
                        res += 2 * idx;
                        break;
                    case 2:
                        res += idx * skybridge;
                        break;
                }
            }
            cnt++;
        }
        cnt = 0;
        for(boolean idx : boolCheck){

            int bToInt = 0;
            if(idx){bToInt = 1;}

            switch (cnt){
                case 0:
                    res += 5 * bToInt;
                    break;

                case 1:
                    res += 5 * bToInt;
                    break;
                case 2:
                    res += 15 * bToInt;
                    break;
            }
        }

        return res;
    }

    public String toString(){
        return teamName;
    }
}
