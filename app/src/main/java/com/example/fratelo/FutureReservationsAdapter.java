package com.example.fratelo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FutureReservationsAdapter extends RecyclerView.Adapter<FutureReservationsAdapter.FutureReservationsViewHolder> {
    boolean[] tables = new boolean[4];
    ArrayList<Player> list = new ArrayList<>();
    Activity activity;
    String TAG = "AdapterClassLog";


    public FutureReservationsAdapter(ArrayList<Player> list, Activity activity , boolean[] tables) {
        this.list = list;
        this.activity = activity;
        this.tables = tables;
    }

    @NonNull
    @Override
    public FutureReservationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewittem,parent,false);
        return new FutureReservationsViewHolder(view);
    }

    @Override
    @SuppressLint("RecyclerView")
    public void onBindViewHolder(@NonNull FutureReservationsViewHolder holder, int position) {
        Player player = list.get(position);
        holder.phoneNum.setText(String.valueOf(player.getPhone()));
        holder.ending.setText(String.valueOf(player.getTimeOfFinish()));
        holder.beginning.setText(String.valueOf(player.getTimeOfStart()));
        holder.fullName.setText(String.valueOf(player.getName()));
        holder.serialNum.setText(String.valueOf(position+1));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPlayerToTable addPlayerToTable = new AddPlayerToTable();
                addPlayerToTable.showDialog(activity, tables, player, new AddPlayerToTable.OnGetDataListener() {
                    @Override
                    public void onSuccess() {
                        list.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, list.size());
                    }
                });
            }
        });



        if (isDateInRange(player)){
            holder.itemView.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.background_stroke_light_green));
            if (isTimePassed(player)) {
                holder.itemView.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.background_stroke_light_red));
            }
        }else{
            holder.itemView.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.background_stroke_white));
        }




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class FutureReservationsViewHolder extends RecyclerView.ViewHolder {
        public TextView fullName,beginning,ending,phoneNum,serialNum;
        public FutureReservationsViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullNameRView);
            beginning = itemView.findViewById(R.id.beginning);
            ending =itemView.findViewById(R.id.ending);
            phoneNum = itemView.findViewById(R.id.phoneNumber);
            serialNum = itemView.findViewById(R.id.serialNumber);

        }
    }
    private boolean isTimePassed(Player player){
        Calendar now = Calendar.getInstance();
        System.out.println("Current Date and TIme : " + now.getTime());
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy/HH:mm", Locale.ENGLISH);

        Date date = null;
        try {
            date = formatter.parse(player.getDate() +"/" +player.getTimeOfStart());


        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeInSecs = now.getTimeInMillis();
        Date afterRemoving5Mins = new Date(timeInSecs - (5 * 60 * 1000));
        assert date != null;

        return date.compareTo(afterRemoving5Mins) <= 0;
    }
    private boolean isDateInRange(Player player) {
        Calendar now = Calendar.getInstance();
        System.out.println("Current Date and TIme : " + now.getTime());
        long timeInSecs = now.getTimeInMillis();

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy/HH:mm", Locale.ENGLISH);

        Date date = null;
        try {
            date = formatter.parse(player.getDate() +"/" +player.getTimeOfStart());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date afterAdding5Mins = new Date(timeInSecs + (5 * 60 * 1000));

        assert date != null;
        Log.d("testAdmin", "compareDate: " + date.compareTo(afterAdding5Mins));

        return date.compareTo(afterAdding5Mins) <= 0;

    }

    @SuppressLint("SimpleDateFormat")
    private String getCurrentDate(){
        java.text.SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy/HH:mm");
        Calendar c = Calendar.getInstance();
        return sdf.format(c.getTime());
    }


}
