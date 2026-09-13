package com.example.d308.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "excursions")

public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String date;
    private int vacationId;

    public Excursion(String title, String date, int vacationId) {
        this.title = title;
        this.date = date;
        this.vacationId = vacationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}