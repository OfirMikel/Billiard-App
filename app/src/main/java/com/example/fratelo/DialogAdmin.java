package com.example.fratelo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

public class DialogAdmin {
    EditText password;
    Button save, cancel;
    String passwordToProceed = "aknv2057";
    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_password);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        Configuration configuration = activity.getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale("en"));
        activity.getResources().updateConfiguration(configuration, activity.getResources().getDisplayMetrics());

        password = dialog.findViewById(R.id.password);
        save = dialog.findViewById(R.id.saveButton);
        cancel = dialog.findViewById(R.id.cancelButton);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordText = password.getText().toString();
                if (passwordText.equals(passwordToProceed)){
                    activity.startActivity(new Intent(activity,adminUse.class));
                    activity.overridePendingTransition(0, 0);
                }else{
                    password.setText("");
                    password.setHint("Wrong Password");
                    password.setHintTextColor(Color.RED);
                }
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
}
