package com.example.locationapp;

public class Event {
    private String location;
    private String event;
    private String date;
    private int deleteButtonNumber;

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getEvent() {
        return event;
    }
    public void setEvent(int priority) {
        this.event = event;
    }

    public int getDeleteButtonNumber() {
        return deleteButtonNumber;
    }
    public void setDeleteButtonNumber(String location) { this.deleteButtonNumber = deleteButtonNumber; }

    public Event(String location, String date, String event, int deleteButtonNumber) {
        this.location = location;
        this.date = date;
        this.event = event;
        this.deleteButtonNumber = deleteButtonNumber;
    }

    public void remove(int position) {

    }
}
