package com.brokenaxles.ftcteamscouter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class RosterScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    //private int teamCnt = 0;
    private LinearLayout btnLayout;
    private List<Button> btns;
    private Intent[] screenSwitch;
    private List<Team> teams;

    private View.OnClickListener allBtnListen;


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnLayout = findViewById(R.id.btnLayout);
        btns = new ArrayList<>();
        screenSwitch = new Intent[2];
        screenSwitch[0] = new Intent(this, AddTeam.class);
        screenSwitch[1] = new Intent(this, TeamInfoScreen.class);

        teams = Sharables.allEvents.get(Sharables.currEvent).getTeams();

        allBtnListen = new View.OnClickListener(){
            public void onClick(View v){

                int idx = btns.indexOf(v);

                if(idx > 0) {
                    screenSwitch[1].putExtra("teamIdx", teams.get(idx - 1).getTeamNum());
                    startActivity(screenSwitch[1]);
                } else {
                    screenSwitch[0].putExtra("evOteam", "team");
                    startActivity(screenSwitch[0]);
                }
            }
        };

        try {

            Button addBtn = new Button(this);
            addBtn.setText("ADD MORE TEAMS");
            addBtn.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            addBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            addBtn.setTextColor(getResources().getColor(R.color.yellow));
            addBtn.setOnClickListener(allBtnListen);
            btnLayout.addView(addBtn);
            btns.add(addBtn);

            if(teams.size() > 0) {
                for(Team idx : teams){
                    addBtn(idx);
                }
            }


         } catch (Exception j){j.printStackTrace();}


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    public void addBtn(Team idx){

        Button temp = new Button(this);
        temp.setText("#" + idx.getTeamNum() + " " + idx.getTeamName());
        temp.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        temp.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        if(idx.perfStatsIn() == 0) {
            temp.setTextColor(getResources().getColor(R.color.allPerfsMissing));
        } else if (idx.perfStatsIn() == 1){
            temp.setTextColor(getResources().getColor(R.color.somePerfsMissing));
        } else {
            temp.setTextColor(getResources().getColor(R.color.perfsIn));
        }

        temp.setOnClickListener(allBtnListen);
        btnLayout.addView(temp);
        btns.add(temp);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Log.d(null, Integer.toString(id));
        Intent i;
        switch(id){

            case R.id.team_info:
                i = new Intent(this, TeamInfoScreen.class);
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
}
