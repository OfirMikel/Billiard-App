package com.example.fratelo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddPlayerToTable {
    EditText notes;
    Button button;
    Spinner tableSpinner;
    String spinnerTable;
    ArrayList<String> availableTables = new ArrayList<>();

    public void showDialog(Activity activity, boolean[] tables , Player player,final AddPlayerToTable.OnGetDataListener listener){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_add_to_table);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        Configuration configuration = activity.getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale("en"));
        activity.getResources().updateConfiguration(configuration, activity.getResources().getDisplayMetrics());

        tableSpinner = dialog.findViewById(R.id.spinner);
        button = dialog.findViewById(R.id.buttonAddToTable);
        notes = dialog.findViewById(R.id.notes);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerTable = tableSpinner.getSelectedItem().toString();
                User newTableUser = playerToUser(player);
                FireBase fireBase = new FireBase();
                fireBase.writeTables(newTableUser);
                fireBase.moveToOldPlayers(player);
                String toast = "שולחן - " + newTableUser.getTableNumber() + " נפתח";
                Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show();
                listener.onSuccess();
                dialog.dismiss();
            }
        });


        AvailableTables(tables);
        ArrayAdapter ad = new ArrayAdapter(activity, R.layout.spinner_item_black,availableTables );
        ad.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        tableSpinner.setAdapter(ad);


        dialog.show();
    }

    private void AvailableTables(boolean[] tables){
        for (int i = 0 ; i< tables.length ;i++ ){
            if (!tables[i]){
                int tableNum = i+1;
                availableTables.add( "שולחן - " + tableNum) ;
            }
        }
    }
    private User playerToUser(Player player){
        String intValueTable = spinnerTable.replaceAll("[^0-9]", ""); //return only numbers
        String pathId = player.getId() + " - "+ player.getName() + " - " + player.getDate();
        return new User(Integer.parseInt(intValueTable),true,getCurrentTime(),getEstimatedEndingTime(),getCurrentDate(), pathId);
    }

    @SuppressLint("SimpleDateFormat")
    private String getCurrentDate(){
        java.text.SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        return sdf.format(c.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    private String getCurrentTime(){
        java.text.SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar c = Calendar.getInstance();
        return sdf.format(c.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    private String getEstimatedEndingTime(){
        java.text.SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar c = Calendar.getInstance();
        long timeInSecs = c.getTimeInMillis();
        Date afterAdding2Hours = new Date(timeInSecs + (2 * 60 * 60 * 1000));
        return sdf.format(afterAdding2Hours);
    }

    public interface OnGetDataListener {
        //this is for callbacks
        void onSuccess();
    }
}
