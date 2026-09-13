package com.example.d308.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;

import com.example.d308.entities.Vacation;

import java.util.List;

@Dao
public interface VacationDAO {

    @Insert
    void insert(Vacation vacation);

    @Update
    void update(Vacation vacation);

    @Delete
    void delete(Vacation vacation);

    @Query("SELECT * FROM vacations")
    List<Vacation> getAllVacations();

    @Query("SELECT * FROM vacations WHERE id = :vacationId")
    Vacation getVacationById(int vacationId);

}