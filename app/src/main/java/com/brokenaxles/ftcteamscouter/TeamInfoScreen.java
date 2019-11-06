package com.brokenaxles.ftcteamscouter;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.ArrayList;

public class TeamInfoScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{



    private EditText teamIn;
    private EditText[] freps = {null, null, null};
    private TextView[] frepLbl = {null, null, null};

    private Switch[] switches = {null, null, null};

    private Switch quarry;

    private Button getTeam, teleSwitch;


    private TextView ptsCat, pts, title;

    private int teamIdx = -1;

    private int whichTeam;

    private List<Team> teams;

    private FirebaseDatabase db;
    private DatabaseReference dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        teamIn = findViewById(R.id.findTeam);


        freps[0] = findViewById(R.id.freeResp1);
        freps[1] = findViewById(R.id.freeResp2);
        freps[2] = findViewById(R.id.freeResp3);

        frepLbl[0] = findViewById(R.id.frep1Lbl);
        frepLbl[1] = findViewById(R.id.frep2Lbl);
        frepLbl[2] = findViewById(R.id.frep3Lbl);

        switches[0] = findViewById(R.id.slider1);
        switches[1] = findViewById(R.id.slider2);
        switches[2] = findViewById(R.id.slider3);

        getTeam = findViewById(R.id.getTeamBtn);
        teleSwitch = findViewById(R.id.teleAutoSwitch);

        ptsCat = findViewById(R.id.autoTeleTitle);

        pts = findViewById(R.id.ptsDisp);

        quarry = findViewById(R.id.quarryStart);
        quarry.setChecked(false);
        whichTeam = 0;

        title = findViewById(R.id.title);

        teams = Sharables.allEvents.get(Sharables.currEvent).getTeams();

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference().child("Events").child("list").child(Integer.toString(Sharables.currEvent)).child("teams");

        Intent reader = getIntent();
        if(reader.hasExtra("teamIdx")){
           getTeam1(reader.getIntExtra("teamIdx", -1));
        }

    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Log.d(null, Integer.toString(id));
        Intent i;
        switch(id){

            case R.id.roster:
                i = new Intent(this, RosterScreen.class);
                startActivity(i);
                break;
            case R.id.event:
                i = new Intent(this, EventScreen.class);
                startActivity(i);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void findTeam1(View v){
        getTeam1(-1);
    }

    public void getTeam1(int tNum){
        int teamNum;
        if(tNum < 0) {
            try {
                teamNum = Integer.parseInt(teamIn.getText().toString());
            } catch (NumberFormatException j) {
                return;
            }
        } else {
            teamNum = tNum;
        }
        int cnt = 0;
        System.out.println(teams);
        for(Team idx : teams){
            if(idx.getTeamNum() == teamNum){

                getTeam.setText(idx.getTeamName());
                teamIdx = cnt;

                freps[0].setText("");
                freps[1].setText("");
                freps[2].setText("");

                switches[0].setChecked(false);
                switches[1].setChecked(false);
                switches[2].setChecked(false);

                teleSwitch.setText("SAVE AUTO, GO TO TELE-OP");
                ptsCat.setText("AUTO");

                pts.setText("--");

                List<Boolean> boolStats = idx.getAutoBoolStats().get(0);
                if(boolStats != null){
                    switches[0].setChecked(boolStats.get(0));
                    switches[1].setChecked(boolStats.get(1));
                    switches[2].setChecked(boolStats.get(2));
                    quarry.setChecked(boolStats.get(3));
                }
                List<Integer> intStats = idx.getAutoIntStats().get(0);


                if(intStats.get(0) != -1){
                    freps[0].setText(Integer.toString(intStats.get(0)));
                }
                if(intStats.get(1) != -1){
                    freps[1].setText(Integer.toString(intStats.get(0)));
                }
                if(intStats.get(2) != -1){
                    freps[2].setText(Integer.toString(intStats.get(0)));
                }


                if(idx.getAutoScore(0) != -1){
                    pts.setText(Integer.toString(idx.getAutoScore(0)) + " PTS");
                }

                return;
            }
            cnt++;
        }
        getTeam.setText("Team not found!");
    }



    public void push1(View v) {


        if(teamIdx!= -1){
            if(teleSwitch.getText().toString().equals("SAVE AUTO, GO TO TELE-OP")) {


                    List<Integer> intStatsPush = new ArrayList<>();
                    if (freps[0].getText().toString().length() != 0) {
                        intStatsPush.add(Integer.parseInt(freps[0].getText().toString()));
                    } else {
                        intStatsPush.add(-1);
                    }
                    if (freps[1].getText().toString().length() != 0) {
                        intStatsPush.add(Integer.parseInt(freps[1].getText().toString()));
                    } else {
                        intStatsPush.add(-1);
                    }
                    if (freps[2].getText().toString().length() != 0) {
                        intStatsPush.add(Integer.parseInt(freps[2].getText().toString()));
                    } else {
                        intStatsPush.add(-1);
                    }

                    List<Boolean> boolStatsPush = new ArrayList<>();
                    boolStatsPush.add(switches[0].isChecked());
                    boolStatsPush.add(switches[1].isChecked());
                    boolStatsPush.add(switches[2].isChecked());
                    boolStatsPush.add(quarry.isChecked());

                    teams.get(teamIdx).updateAutoInfo(0, boolStatsPush, intStatsPush);

                    dbRef.child(Integer.toString(teamIdx)).child("autoBoolStats").setValue(teams.get(teamIdx).getAutoBoolStats());
                    dbRef.child(Integer.toString(teamIdx)).child("autoIntStats").setValue(teams.get(teamIdx).getAutoIntStats());



                ptsCat.setText("TELE-OP");
                teleSwitch.setText("SAVE TELE-OP, GO TO AUTO");
                switches[0].setText("CAPPING");

                frepLbl[0].setText("#Foundation Placements");
                frepLbl[1].setText("Tallest Skyscraper Height");
                frepLbl[2].setText("#of Stones transported");

                freps[0].setHint("Enter #Foundation");
                freps[1].setHint("Enter Tallest Height");
                freps[2].setHint("Enter #Stones");

                freps[0].setText("");
                freps[1].setText("");
                freps[2].setText("");

                switches[0].setChecked(false);
                switches[1].setChecked(false);
                switches[2].setChecked(false);
                quarry.setVisibility(View.INVISIBLE);

                List<Boolean> boolStats = teams.get(teamIdx).getTeleBoolStats().get(0);
                if(boolStats != null){
                    switches[0].setChecked(boolStats.get(0));
                    switches[1].setChecked(boolStats.get(1));
                    switches[2].setChecked(boolStats.get(2));
                }
                List<Integer> intStats = teams.get(teamIdx).getTeleIntStats().get(0);


                if(intStats.get(0) != -1){
                    freps[0].setText(Integer.toString(intStats.get(0)));
                }
                if(intStats.get(1) != -1){
                    freps[1].setText(Integer.toString(intStats.get(1)));
                }
                if(intStats.get(2) != -1){
                    freps[2].setText(Integer.toString(intStats.get(2)));
                }

                if (teams.get(teamIdx).getTeleScore(0) != -1) {
                    pts.setText(Integer.toString(teams.get(teamIdx).getTeleScore(0)) + " PTS");
                } else {
                    pts.setText("--");
                }

                return;

            } else {

                List<Integer> intStatsPush = new ArrayList<>();
                if (freps[0].getText().toString().length() != 0) {
                    intStatsPush.add(Integer.parseInt(freps[0].getText().toString()));
                } else {
                    intStatsPush.add(-1);
                }
                if (freps[1].getText().toString().length() != 0) {
                    intStatsPush.add(Integer.parseInt(freps[1].getText().toString()));
                } else {
                    intStatsPush.add(-1);
                }
                if (freps[2].getText().toString().length() != 0) {
                    intStatsPush.add(Integer.parseInt(freps[2].getText().toString()));
                } else {
                    intStatsPush.add(-1);
                }

                List<Boolean> boolStatsPush = new ArrayList<>();
                boolStatsPush.add(switches[0].isChecked());
                boolStatsPush.add(switches[1].isChecked());
                boolStatsPush.add(switches[2].isChecked());

                teams.get(teamIdx).updateTeleInfo(0, boolStatsPush, intStatsPush);

                dbRef.child(Integer.toString(teamIdx)).child("teleBoolStats").setValue(teams.get(teamIdx).getTeleBoolStats());
                dbRef.child(Integer.toString(teamIdx)).child("teleIntStats").setValue(teams.get(teamIdx).getTeleIntStats());



                ptsCat.setText("AUTO");
                teleSwitch.setText("SAVE AUTO, GO TO TELE-OP");
                switches[0].setText("SKYBRIDGE");

                freps[0].setHint("Enter #Skystones");
                freps[1].setHint("Enter #Stones");
                freps[2].setHint("Enter #Found");

                frepLbl[0].setText("#Skystones transported");
                frepLbl[1].setText("#Normal stones transported");
                frepLbl[2].setText("#of stones placed on foundation");

                freps[0].setText("");
                freps[1].setText("");
                freps[2].setText("");

                switches[0].setChecked(false);
                switches[1].setChecked(false);
                switches[2].setChecked(false);
                quarry.setVisibility(View.VISIBLE);
                quarry.setChecked(false);

                List<Boolean> boolStats = teams.get(teamIdx).getAutoBoolStats().get(0);
                if(boolStats != null){
                    switches[0].setChecked(boolStats.get(0));
                    switches[1].setChecked(boolStats.get(1));
                    switches[2].setChecked(boolStats.get(2));
                    quarry.setChecked(boolStats.get(3));
                }


                List<Integer> intStats = teams.get(teamIdx).getAutoIntStats().get(0);

                if(intStats.get(0) != -1){
                    freps[0].setText(Integer.toString(intStats.get(0)));
                }
                if(intStats.get(1) != -1){
                    freps[1].setText(Integer.toString(intStats.get(1)));
                }
                if(intStats.get(2) != -1){
                    freps[2].setText(Integer.toString(intStats.get(2)));
                }

                if (teams.get(teamIdx).getAutoScore(0) != -1) {
                    pts.setText(Integer.toString(teams.get(teamIdx).getAutoScore(0)) + " PTS");
                } else {
                    pts.setText("--");
                }

                return;

            }
        }

    }

}
