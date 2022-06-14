package com.example.fratelo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class adminUse extends AppCompatActivity {
    RecyclerView recyclerViewFutureReservations;
    ImageView[] playingStatusTable = new ImageView[4];
    ArrayList<Player> players = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://fratelo-5ae67-default-rtdb.firebaseio.com");
    DatabaseReference reference = FirebaseDatabase.getInstance("https://fratelo-5ae67-default-rtdb.firebaseio.com").getReference();
    boolean[] tables = new boolean[4];
    FutureReservationsAdapter futureReservationsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_use);

        recyclerViewFutureReservations = findViewById(R.id.futureReservations);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewFutureReservations.setLayoutManager(layoutManager);

        futureReservationsAdapter = new FutureReservationsAdapter(players,this, tables);
        recyclerViewFutureReservations.setAdapter(futureReservationsAdapter);

        playingStatusTable[0] = findViewById(R.id.play_table_1);
        playingStatusTable[1] = findViewById(R.id.play_table_2);
        playingStatusTable[2] = findViewById(R.id.play_table_3);
        playingStatusTable[3] = findViewById(R.id.play_table_4);



        retrieveDataOfTables();
        sortAndGetArray();

    }

    public void retrieveDataOfTables(){

        reference.child("Tables").child("Table1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                tables[0] = user.isPlayingStatus();
                if (user.isPlayingStatus()){
                    playingStatusTable[0].setVisibility(View.VISIBLE);
                }else{
                    playingStatusTable[0].setVisibility(View.GONE);
                }
                playingStatusTable[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FireBase fireBase = new FireBase();
                        user.setPlayingStatus(false);
                        fireBase.writeTables(user);
                        Toast.makeText(adminUse.this, "click" , Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("Tables").child("Table2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                tables[1] = user.isPlayingStatus();
                if (tables[1]){
                    playingStatusTable[1].setVisibility(View.VISIBLE);
                }else{
                    playingStatusTable[1].setVisibility(View.GONE);
                }
                playingStatusTable[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FireBase fireBase = new FireBase();
                        user.setPlayingStatus(false);
                        fireBase.writeTables(user);
                        Toast.makeText(adminUse.this, "click" , Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("Tables").child("Table3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                tables[2] = user.isPlayingStatus();
                if (tables[2]){
                    playingStatusTable[2].setVisibility(View.VISIBLE);
                }else{
                    playingStatusTable[2].setVisibility(View.GONE);
                }
                playingStatusTable[2].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FireBase fireBase = new FireBase();
                        user.setPlayingStatus(false);
                        fireBase.writeTables(user);
                        Toast.makeText(adminUse.this, "click" , Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("Tables").child("Table4").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                tables[3] = user.isPlayingStatus();
                if (tables[3]){
                    playingStatusTable[3].setVisibility(View.VISIBLE);
                }else{
                    playingStatusTable[3].setVisibility(View.GONE);
                }
                playingStatusTable[3].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FireBase fireBase = new FireBase();
                        user.setPlayingStatus(false);
                        fireBase.writeTables(user);
                        Toast.makeText(adminUse.this, "click" , Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void sortAndGetArray(){
        DatabaseReference myRef = database.getReference("FUTURE Players");
        myRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                players.clear();
                for (DataSnapshot dataSnapshot :snapshot.getChildren()){
                    Player player = dataSnapshot.getValue(Player.class);
                    assert player != null;
                    players.add(player);
                }
                Collections.sort(players,CompareAndSort);
                futureReservationsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static Comparator<Player> CompareAndSort =new Comparator<Player>() {
        final DateFormat format = new SimpleDateFormat("dd-MM-yyyy/HH:mm", Locale.ENGLISH);
        Date date1 , date2;
        @Override
        public int compare(Player o1, Player o2) {
            try {
                date1 = format.parse(o1.getDate() +"/" +o1.getTimeOfStart());
                date2 = format.parse(o2.getDate() +"/" +o2.getTimeOfStart());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date1.compareTo(date2);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean compareDate(String d1){
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy/HH:mm", Locale.ENGLISH);
        Date date = null;
        Date todayDate = null;
        try {
            date = formatter.parse(d1);
            todayDate =  formatter.parse(getCurrentDate());;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date != null;
        assert todayDate != null;

        Log.d("testAdmin", "compareDate: " + date.compareTo(todayDate));
        return date.compareTo(todayDate) >= 0;
    }

    @SuppressLint("SimpleDateFormat")
    private String getCurrentDate(){
        java.text.SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy/HH:mm");
        Calendar c = Calendar.getInstance();
        return sdf.format(c.getTime());
    }





}