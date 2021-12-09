package com.example.sqlite;

import java.util.Date;

/*инфа о заметке for Recycler*/
public class Note {
    private Date date;
    private String title;
    private String description;
    private String dayOfWeek;
    private int priority;

    public Note(Date date, String title, String description, String dayOfWeek, int priority) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.dayOfWeek = dayOfWeek;
        this.priority = priority;
    }

    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public int getPriority() {
        return priority;
    }
}
