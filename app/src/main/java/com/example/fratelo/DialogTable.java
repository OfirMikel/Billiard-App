package com.example.fratelo;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.Locale;

public class DialogTable {

    TextView tableNum,timeOfStart,timeOfFinish,tableStatus;

    public void showDialog(Activity activity , User user){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        Configuration configuration = activity.getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale("en"));
        activity.getResources().updateConfiguration(configuration, activity.getResources().getDisplayMetrics());

        tableNum = dialog.findViewById(R.id.tableNum);
        timeOfStart = dialog.findViewById(R.id.timeOfStart);
        timeOfFinish = dialog.findViewById(R.id.timeOfFinish);
        tableStatus = dialog.findViewById(R.id.tableStatus);

        tableNum.setText(String.valueOf(user.getTableNumber()));
        timeOfStart.setText(user.getTimeOfStart());
        timeOfFinish.setText(user.getTimeOfFinish());
        tableStatus.setText(user.isPlayingStatus() ? "שולחן במשחק" : "שולחן פנוי");

        if (!user.isPlayingStatus()){
            timeOfStart.setVisibility(View.GONE);
            timeOfFinish.setVisibility(View.GONE);

        }
        dialog.show();
    }
}
