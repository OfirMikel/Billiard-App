package com.example.fratelo;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class FireBase {
    String TAG = "FireBaseClass";
    String TABLES = "Tables";
    String FuturePLAYERS = "FUTURE Players";
    String OldPLAYERS = "OLD Players";

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://fratelo-5ae67-default-rtdb.firebaseio.com");

    public void writeTables(User user){
        String path = "Table" +  user.getTableNumber();
        DatabaseReference myRef = database.getReference(TABLES).child(path);
        myRef.setValue(user);
    }

    public void addPlayer(Player player,  String id, final WritingSucceed listener){
        DatabaseReference myRef = database.getReference(FuturePLAYERS).child(id);
        myRef.setValue(player).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onSuccess(task);
            }
        });
    }

    public void moveToOldPlayers(Player player){
        String pathId = player.getId() + " - "+ player.getName() + " - " + player.getDate();
        DatabaseReference OldPlayers = database.getReference(OldPLAYERS).child(pathId);
        DatabaseReference FuturePlayers = database.getReference(FuturePLAYERS).child(pathId);
        FuturePlayers.removeValue();
        OldPlayers.setValue(player);

    }
    public void getNextID(final OnGetIDListener listener){
        DatabaseReference myRef = database.getReference(FuturePLAYERS);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listener.onSuccess((int) (snapshot.getChildrenCount() + 1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public void singleReadData(int tableNum ,final OnGetDataListener listener) {
        String path = "Table" +  tableNum;
        DatabaseReference myRef = database.getReference(TABLES).child(path);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getFuturePlayers(final OnGetDataFromFuturePlayersListener listener) {
        DatabaseReference myRef = database.getReference(FuturePLAYERS);
        ArrayList<Player> players = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                    Player player = snapshot.getValue(Player.class);
                    players.add(player);
                }
                listener.onSuccess(players);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public interface OnGetDataListener {
        //this is for callbacks
        void onSuccess(User dataSnapshotValue);
    }
    public interface OnGetIDListener {
        //this is for callbacks
        void onSuccess(int id);
    }
    public interface WritingSucceed{
        //this is for callbacks
        void onSuccess(Task<Void> dataSnapshotValue);
    }

    public interface OnGetDataFromFuturePlayersListener{
        //this is for callbacks
        void onSuccess(ArrayList<Player> arrayList);

    }




}
