package com.app.remortapp.Model;

public class Recording {
    private String name;
    private String date;
    private String fileName;

    public Recording() {
        // No-argument constructor required for Firebase
    }

    public Recording(String name, String date, String fileName) {
        this.name = name;
        this.date = date;
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getFileName() {
        return fileName;
    }
}
