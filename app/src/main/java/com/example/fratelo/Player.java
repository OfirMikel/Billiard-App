package com.example.fratelo;

public class Player {
    private String timeOfStart;
    private String timeOfFinish;
    private int tableNumber;
    private String date;
    private String name;
    private String phone;
    private int id;

    public Player(){

    }

    @Override
    public String toString() {
        return "Player{" +
                "timeOfStart='" + timeOfStart + '\'' +
                ", timeOfFinish='" + timeOfFinish + '\'' +
                ", tableNumber=" + tableNumber +
                ", date='" + date + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", id=" + id +
                '}';
    }

    public Player(String timeOfStart, String timeOfFinish, int tableNumber, String date, String name, String phone, int id) {
        this.timeOfStart = timeOfStart;
        this.timeOfFinish = timeOfFinish;
        this.tableNumber = tableNumber;
        this.date = date;
        this.name = name;
        this.phone = phone;
        this.id = id;
    }

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
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }










}
