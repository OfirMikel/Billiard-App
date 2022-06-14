package com.example.fratelo;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String timeOfStart;
    private String timeOfFinish;
    private boolean playingStatus;
    private int tableNumber;
    private String date;
    private String Name;

    public User(){
    }

    public User(int tableNumber,boolean playingStatus ,String timeOfStart, String timeOfFinish, String date, String name) {
        this.timeOfStart = timeOfStart;
        this.timeOfFinish = timeOfFinish;
        this.playingStatus = playingStatus;
        this.tableNumber = tableNumber;
        this.date = date;
        Name = name;
    }

    protected User(Parcel in) {
        timeOfStart = in.readString();
        timeOfFinish = in.readString();
        playingStatus = in.readByte() != 0;
        tableNumber = in.readInt();
        date = in.readString();
        Name = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getTimeOfStart() {
        return timeOfStart;
    }

    public void setTimeOfStart(String timeOfStart) {
        this.timeOfStart = timeOfStart;
    }

    public String getTimeOfFinish() {
        return timeOfFinish;
    }

    public void setTimeOfFinish(String timeOfFinish) {
        this.timeOfFinish = timeOfFinish;
    }

    public boolean isPlayingStatus() {
        return playingStatus;
    }

    public void setPlayingStatus(boolean playingStatus) {
        this.playingStatus = playingStatus;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(timeOfStart);
        parcel.writeString(timeOfFinish);
        parcel.writeByte((byte) (playingStatus ? 1 : 0));
        parcel.writeInt(tableNumber);
        parcel.writeString(date);
        parcel.writeString(Name);
    }
}
