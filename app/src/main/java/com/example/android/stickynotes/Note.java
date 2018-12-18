package com.example.android.stickynotes;

import android.util.Log;

public class Note {
    private int id;
    private String note;
    private String dateTime;
    private long absoluteTime;

    public Note(String note, String dateTime, long absoluteTime) {
        setId(0);
        setNote(note);
        setDateTime(dateTime);
        setAbsoluteTime(absoluteTime);
    }

    public Note(int id, String note, String dateTime, long absoluteTime) {
        setId(id);
        setNote(note);
        setDateTime(dateTime);
        setAbsoluteTime(absoluteTime);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public long getAbsoluteTime() {
        return absoluteTime;
    }

    public void setAbsoluteTime(long absoluteTime) {
        this.absoluteTime = absoluteTime;
    }

    public String getHeader(){
        String text = note;
        if (text.indexOf('\n') == 0)
            do {
            text = text.substring(1,text.length());
            } while (text.indexOf('\n') == 0);
        int position = text.indexOf('\n');
        if (position > 0) return text.substring(0, position);
        else return text;
    }

}
