package com.brokenaxles.ftcteamscouter;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.*;

//Anything that can be shared between activities without changes

public class Sharables {
    public static int currEvent;
    public static List<Event> allEvents;
}
