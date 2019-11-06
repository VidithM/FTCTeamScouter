package com.brokenaxles.ftcteamscouter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventScreen extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private LinearLayout btnLayout;
    private List<Button> btns;
    private Intent[] screenSwitch;
    private View.OnClickListener allBtnListen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_screen);

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference().child("Events");
        btns = new ArrayList<>();
        btnLayout = findViewById(R.id.btnLayoutEvents);
        screenSwitch = new Intent[2];
        screenSwitch[0] = new Intent(this, RosterScreen.class);
        screenSwitch[1] = new Intent(this, AddTeam.class);

        allBtnListen = new View.OnClickListener(){
            public void onClick(View v){

                int idx = btns.indexOf(v);

                if(idx > 0) {
                    Sharables.currEvent = idx - 1;
                    startActivity(screenSwitch[0]);
                } else {
                    screenSwitch[1].putExtra("evOteam", "event");
                    startActivity(screenSwitch[1]);
                }
            }
        };

        final Button addBtn = new Button(this);
        addBtn.setText("ADD MORE EVENTS");
        addBtn.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        addBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        addBtn.setTextColor(getResources().getColor(R.color.yellow));
        addBtn.setOnClickListener(allBtnListen);
        btnLayout.addView(addBtn);
        btns.add(addBtn);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {

            //IMPORTANT!! EVERYTHING HERE IS TO SYNC LOCAL LISTS WITH THE DB! LOCAL LISTS ARE USED FOR READING PURPOSES
            // THROUGHOUT THE APP. BOTH READ/WRITE NEED TO BE DONE AT WHATEVER LEVEL YOU ARE CHANGING. THERE IS NO TRICKLE DOWN WRITING/
            // SUCK IT UP READING! ONE LEVEL AT A TIME!!!.

            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {
                Sharables.allEvents = new ArrayList<>();
                GenericTypeIndicator<ArrayList<Team>> genTeams = new GenericTypeIndicator<ArrayList<Team>>() {};
                GenericTypeIndicator<ArrayList<Event>> genEvent = new GenericTypeIndicator<ArrayList<Event>>() {};
                GenericTypeIndicator<ArrayList<ArrayList<Integer>>> genInt = new GenericTypeIndicator<ArrayList<ArrayList<Integer>>>() {};
                GenericTypeIndicator<ArrayList<ArrayList<Boolean>>> genBool = new GenericTypeIndicator<ArrayList<ArrayList<Boolean>>>() {};

                if(snap.hasChild("list")){
                    Sharables.allEvents = snap.child("list").getValue(genEvent);

                    // Has all of the event/team info stored in this - nothing is missing, only issue is it can't get it all through the top level! (Due to embedded lists)
                }
                int eCnt = 0;
                for(Event idx : Sharables.allEvents){
                    if(snap.child("list").child(Integer.toString(eCnt)).hasChild("teams")) {
                        idx.setTeams(snap.child("list").child(Integer.toString(eCnt)).child("teams").getValue(genTeams));
                        //Sets local team list
                        DataSnapshot closeUp = snap.child("list").child(Integer.toString(eCnt)).child("teams");

                            int tCnt = 0;
                            for(Team t : idx.getTeams()){
                                List<ArrayList<Integer>> teleInt, autoInt;
                                List<ArrayList<Boolean>> teleBool, autoBool;

                                teleInt = closeUp.child(Integer.toString(tCnt)).child("teleIntStats").getValue(genInt);
                                autoInt = closeUp.child(Integer.toString(tCnt)).child("autoIntStats").getValue(genInt);
                                autoBool = closeUp.child(Integer.toString(tCnt)).child("autoBoolStats").getValue(genBool);
                                teleBool = closeUp.child(Integer.toString(tCnt)).child("teleBoolStats").getValue(genBool);

                                for(int stg = 0; stg < teleInt.size(); stg++){
                                    t.updateAutoInfo(stg, autoBool.get(stg), autoInt.get(stg));
                                    t.updateTeleInfo(stg, teleBool.get(stg), teleInt.get(stg));
                                }
                                tCnt++;

                            }
                    }
                    eCnt++;
                    addBtn(idx);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });



    }

    public void addBtn(Event idx){

        Button temp = new Button(this);
        temp.setText(" " + idx.getName() + " -  " + idx.getLocation().toUpperCase());
        temp.setTextColor(getResources().getColor(R.color.textColor));
        temp.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        temp.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        temp.setOnClickListener(allBtnListen);
        btnLayout.addView(temp);
        btns.add(temp);

    }
}
