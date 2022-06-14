package com.example.fratelo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.INotificationSideChannel;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class DialogAdd {

    String TAG = "DialogAdd";
    ArrayList<Player> players = new ArrayList<>();

    EditText timeOfArrival,fullName,areaCodePhone,continuedPhone;
    Spinner tableSpinner;
    CheckBox readAndAccept;
    String hourShow , minuteShow , phone;
    Button save,cancel;
    Calendar currentTime = Calendar.getInstance();
    Player player;
    ArrayList<String> availableTables = new ArrayList<>();
    boolean[] tables;



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showDialog(Activity activity , boolean[] tables){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_add_user);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        Configuration configuration = activity.getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale("en"));
        activity.getResources().updateConfiguration(configuration, activity.getResources().getDisplayMetrics());
        this.tables = tables;
        availableTables = initializeAvailableTables();


        //Initialize the Ids
        timeOfArrival = dialog.findViewById(R.id.timeOfArrival);
        fullName = dialog.findViewById(R.id.fullName);
        areaCodePhone = dialog.findViewById(R.id.areaCodePhoneNum);
        continuedPhone = dialog.findViewById(R.id.continuedPhoneNum);
        tableSpinner = dialog.findViewById(R.id.spinner);
        readAndAccept = dialog.findViewById(R.id.checkBox);
        cancel = dialog.findViewById(R.id.cancel);
        save = dialog.findViewById(R.id.save);

        timeOfArrival.setOnClickListener(view -> {
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(activity, AlertDialog.THEME_HOLO_LIGHT, (timePicker, selectedHour, selectedMinute) -> {
                boolean testVar = false;
                if(selectedHour <= currentTime.getTime().getHours() && selectedMinute <= currentTime.getTime().getMinutes()){
                    selectedHour = currentTime.getTime().getHours();
                    selectedMinute = currentTime.getTime().getMinutes();
                    testVar = true;
                }
                hourShow = (selectedHour < 10 ) ? "0" +selectedHour : String.valueOf(selectedHour);
                minuteShow = (selectedMinute < 10 ) ? "0" +selectedMinute : String.valueOf(selectedMinute);
                String show = hourShow + ":" + minuteShow;
                AvailableTablesAtTime(show,currentDate(), activity);

                if (testVar){
                    timeOfArrival.setText("כעת");
                    Toast.makeText(activity, "ניתן לבחור זמן הגעה רק מהזמן הנוחכי בתאריך הנוכחי", Toast.LENGTH_SHORT).show();
                    return;
                }
                timeOfArrival.setText(show);



            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();


        });


        ArrayAdapter ad = new ArrayAdapter(activity, R.layout.spinner_item,availableTables);
        ad.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        tableSpinner.setAdapter(ad);




        save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SupportAnnotationUsage")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                setPlayer(activity,dialog);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });






        dialog.show();
    }

    private void AvailableTablesAtTime(String timeHS, String date , Activity activity) {

        FireBase fireBase = new FireBase();
        ProgressDialog progress = new ProgressDialog(activity);    //ProgressDialog
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.show();

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                fireBase.getFuturePlayers(new FireBase.OnGetDataFromFuturePlayersListener() {
                    @Override
                    public void onSuccess(ArrayList<Player> arrayList) {
                        players = arrayList;
                        progress.dismiss();
                    }
                });
            }
        }).start();

        progress.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Toast.makeText(activity, "מחשב שולחנות פנויים בשעה הנבחרה.... ", Toast.LENGTH_SHORT).show();
                ArrayList<Integer> temp = new ArrayList<>();
                String timeChosen = date + "/" + timeHS;
                for (Player player : players){
                    if(isDateInRange(player,timeChosen)){
                        temp.add(player.getTableNumber());
                    }
                }
                Log.d(TAG, "AvailableTablesAtTime: " + temp);
                Collections.sort(temp);
                boolean[] array = {false,false,false,false};
                for (int table : temp){
                    array[table-1] = true;
                }
                availableTables.clear();

                availableTables = AvailableTables(array) ;
                Log.d(TAG, "AvailableTablesAtTime: " + availableTables);
                ArrayAdapter ad = new ArrayAdapter(activity, R.layout.spinner_item2,availableTables);
                ad.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                tableSpinner.setAdapter(ad);


            }
        });

    }
    private boolean isDateInRange(Player player, String time) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy/HH:mm", Locale.ENGLISH);
        String timeOfStart = player.getDate()+"/" + player.getTimeOfStart();
        String timeOfFinish = player.getDate()+"/" + player.getTimeOfFinish();
        Date[] dates = new Date[0];
        try {
            dates = new Date[]{formatter.parse(time) , formatter.parse(timeOfStart), formatter.parse(timeOfFinish)};
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean t = dates[0].after(dates[1]) && dates[0].before(dates[2]);
        Log.d(TAG, "isDateInRange: " + Arrays.toString(dates) + " - " + t );
        return dates[0].after(dates[1]) && dates[0].before(dates[2]);
    }



    private ArrayList<String> initializeAvailableTables() {
        ArrayList<String> toReturn =new ArrayList<>();
        toReturn.add("בחר זמן הגעה");
        return toReturn;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String currentDate(){
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return dateObj.format(formatter);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setPlayer(Activity activity,Dialog dialogTable) {
        phone = areaCodePhone.getText().toString() + "-" +continuedPhone.getText().toString();
        String timeOfStart =hourShow + ":" + minuteShow;
        int hardCodeTime =2;
        String timeOfFinish = addHours(hardCodeTime,timeOfStart);
        String spinnerTable = tableSpinner.getSelectedItem().toString();

        String intValue = spinnerTable.replaceAll("[^0-9]", ""); //return only numbers


        ProgressDialog progress = new ProgressDialog(activity);    //ProgressDialog
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.show();
        final int[] ID = {0};
        new Thread(new Runnable() {
            @SuppressLint("SupportAnnotationUsage")
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                FireBase fireBase = new FireBase();
                fireBase.getNextID(new FireBase.OnGetIDListener() {
                    @Override
                    public void onSuccess(int id) {
                        ID[0] = id;
                        player = new Player(timeOfStart,timeOfFinish,  Integer.parseInt(intValue),currentDate(), fullName.getText().toString(),phone,ID[0]);
                        if (checkParams(timeOfStart,timeOfFinish)){
                            progressDialog(activity,player,dialogTable);
                        }
                        progress.dismiss();

                    }
                });
            }
        }).start();

        progress.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.d(TAG, "onDismiss: " + player.toString());
            }
        });

    }


    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String addHours(int hours , String time) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy/HH:mm", Locale.ENGLISH);

        String Date = currentDate() + "/" + time;
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        try {
            date = formatter.parse(Date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date != null;
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        java.text.SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Log.d(TAG, "addHours: " + Date + "  date 2 - " + calendar.getTime());
        return sdf.format(calendar.getTime());

    }

    private  boolean checkParams( String timeOfStart, String timeOfFinish ){

        if (!readAndAccept.isChecked()){
            readAndAccept.setTextColor(Color.RED);
            return false;
        }
        if (timeOfStart.length() < 5 || timeOfFinish.length() < 5){
            timeOfArrival.setTextColor(Color.RED);
            return false;
        }
        if (fullName.getText().length() < 3){
            fullName.setHintTextColor(Color.RED);
            fullName.setHint("UnValid full name");
            return false;
        }
        if (phone.length() != 11){
            areaCodePhone.setHintTextColor(Color.RED);
            continuedPhone.setHint("Must fill");
            continuedPhone.setHintTextColor(Color.RED);
            return false;
        }
        return true;
    }

    private ArrayList<String> AvailableTables(boolean[] tables){
        ArrayList<String> toReturn =new ArrayList<>();
        for (int i = 0 ; i< tables.length ;i++ ){
            if (!tables[i]){
                int tableNum = i+1;
                toReturn.add("שולחן - " + tableNum);
            }
        }
        return toReturn;
    }

    private void progressDialog(Activity activity , @NonNull Player player , Dialog dialogTable) {
        ProgressDialog progress = new ProgressDialog(activity);    //ProgressDialog
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        String pathId = player.getId() + " - "+ player.getName() + " - " + player.getDate();
        progress.show();
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                FireBase fireBase = new FireBase();
                fireBase.addPlayer(player,pathId , new FireBase.WritingSucceed() {
                    @Override
                    public void onSuccess(Task<Void> dataSnapshotValue) {
                        progress.dismiss();
                    }
                });
            }
        }).start();

        progress.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialogTable.dismiss();
                Toast.makeText(activity, "שולחן הוזמן", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
