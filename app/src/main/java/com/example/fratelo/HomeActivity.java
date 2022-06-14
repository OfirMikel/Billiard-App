package com.example.fratelo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.UiAutomation;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {
    ImageView[] playingStatusTable = new ImageView[4];
    ImageView[] tablesImages = new ImageView[4];
    boolean[] tables = new boolean[4];
    Button buttonAdd;
    DatabaseReference database = FirebaseDatabase.getInstance("https://fratelo-5ae67-default-rtdb.firebaseio.com").getReference();

    String TAG = "Main";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#672324")));
        getWindow().setStatusBarColor(Color.parseColor("#672324"));
        Configuration configuration = getResources().getConfiguration();
        fontSizeAdjust(configuration);
        configuration.setLayoutDirection(new Locale("en"));
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());




        //Initializing The playingStatusTableOne with their XML equivalents
        playingStatusTable[0] = findViewById(R.id.play_table_1);
        playingStatusTable[1] = findViewById(R.id.play_table_2);
        playingStatusTable[2] = findViewById(R.id.play_table_3);
        playingStatusTable[3] = findViewById(R.id.play_table_4);

        tablesImages[0] = findViewById(R.id.table_1);
        tablesImages[1] = findViewById(R.id.table_2);
        tablesImages[2] = findViewById(R.id.table_3);
        tablesImages[3] = findViewById(R.id.table_4);

        buttonAdd = findViewById(R.id.buttonAdd);



        database.child("Tables").child("Table1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                tables[0] = user.isPlayingStatus();
                if (tables[0]){
                    playingStatusTable[0].setVisibility(View.VISIBLE);
                }else{
                    playingStatusTable[0].setVisibility(View.GONE);
                }

                tablesImages[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogTable dialogTable = new DialogTable();
                        dialogTable.showDialog(HomeActivity.this , user);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        database.child("Tables").child("Table2").addValueEventListener(new ValueEventListener() {
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

                tablesImages[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogTable dialogTable = new DialogTable();
                        dialogTable.showDialog(HomeActivity.this , user);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        database.child("Tables").child("Table3").addValueEventListener(new ValueEventListener() {
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

                tablesImages[2].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogTable dialogTable = new DialogTable();
                        dialogTable.showDialog(HomeActivity.this , user);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        database.child("Tables").child("Table4").addValueEventListener(new ValueEventListener() {
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

                tablesImages[3].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogTable dialogTable = new DialogTable();
                        dialogTable.showDialog(HomeActivity.this , user);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAdd dialogAdd = new DialogAdd();
                dialogAdd.showDialog(HomeActivity.this,tables);
            }
        });

    }



    public void fontSizeAdjust(Configuration configuration){
        configuration.fontScale = (float) 1.0;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale* metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration,metrics);
    }


}

