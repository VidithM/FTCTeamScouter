package com.brokenaxles.ftcteamscouter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddTeam extends AppCompatActivity {

    private EditText numIn, nameIn;
    private TextView title;
    private String evOTeam;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);
        Intent extras = getIntent();
        if(extras.hasExtra("evOteam")){
            evOTeam = extras.getStringExtra("evOteam");
        }

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();

        numIn = findViewById(R.id.teamNumIn);
        nameIn = findViewById(R.id.teamNameIn);
        title = findViewById(R.id.title);

        if(evOTeam.equals("event")){
            title.setText("ADD EVENT");
            nameIn.setHint("Enter Event Name Here");
            numIn.setHint("Enter Event Location Here");
            numIn.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            title.setText("ADD TEAM");
            nameIn.setHint("Enter Team Name Here");
            numIn.setHint("Enter Team Number Here");
            numIn.setInputType(InputType.TYPE_CLASS_NUMBER);
        }


    }

    public void add(View v) throws IOException {

        if(evOTeam.equals("event")){
            if(nameIn.getText().length() > 0 && numIn.getText().length() > 0) {
                Event temp = new Event();
                temp.setName(nameIn.getText().toString().toUpperCase());
                temp.setLocation(numIn.getText().toString().toUpperCase());

                Sharables.allEvents.add(temp);

                dbRef.child("Events").child("list").setValue(Sharables.allEvents);
                startActivity(new Intent(this, EventScreen.class));
            }
        } else {
            if(nameIn.getText().length() > 0 && numIn.getText().length() > 0) {
                Team temp = new Team();
                temp.updateBasicInfo(nameIn.getText().toString(), Integer.parseInt(numIn.getText().toString()));

                System.out.println(Sharables.allEvents);
                Event update = Sharables.allEvents.get(Sharables.currEvent);
                update.addTeam(temp);

                dbRef.child("Events").child("list").child(Integer.toString(Sharables.allEvents.indexOf(update))).child("teams").setValue(update.getTeams());

                startActivity(new Intent(this, RosterScreen.class));
            }

        }

    }
}
