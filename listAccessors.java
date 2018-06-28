package com.example.mark.journalapplication;

public class listAccessors {
    int id;
    String thought, Day;

    public listAccessors(int id, String thought, String Day) {
        this.id = id;
        this.thought = thought;
        this.Day = Day;
    }

    public int getId() {
        return id;
    }

    public String getThought() {
        return thought;
    }

    public String getDay() {
        return Day;
    }

}
