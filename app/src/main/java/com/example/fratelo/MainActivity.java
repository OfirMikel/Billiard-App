package com.example.fratelo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Configuration configuration = getResources().getConfiguration();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#672324")));
        getWindow().setStatusBarColor(Color.parseColor("#672324"));
        SwipeButton swipeButton = findViewById(R.id.swipe);
        swipeButton.setOnStateChangeListener(new OnStateChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onStateChange(boolean active) {
               progressDialog();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.admin) {
            DialogAdmin dialogAdmin = new DialogAdmin();
            dialogAdmin.showDialog(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void progressDialog() {
        ProgressDialog progress = new ProgressDialog(this);    //ProgressDialog
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.show();
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                FireBase fireBase = new FireBase();
                fireBase.singleReadData(1, new FireBase.OnGetDataListener() {
                    @Override
                    public void onSuccess(User dataSnapshotValue) {
                        progress.dismiss();
                    }
                });
            }
        }).start();

        progress.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                startActivity(new Intent(MainActivity.this,HomeActivity.class));
                overridePendingTransition(0, 0);
            }
        });
    }


}