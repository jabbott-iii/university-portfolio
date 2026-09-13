package com.example.d308;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.d308.database.AppDatabase;
import com.example.d308.entities.Vacation;

import java.util.ArrayList;
import java.util.List;

public class VacationListActivity extends AppCompatActivity {

    private Button addButton;
    private ListView vacationListView;
    private AppDatabase db;
    private List<Vacation> vacationObjects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        addButton = findViewById(R.id.buttonAddVacation);
        vacationListView = findViewById(R.id.vacationListView);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "vacation_database")
                .allowMainThreadQueries()
                .build();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationListActivity.this, VacationDetailActivity.class);
                startActivity(intent);
            }
        });

        vacationListView.setOnItemClickListener((parent, view, position, id) -> {
            Vacation selectedVacation = vacationObjects.get(position);

            Intent intent = new Intent(VacationListActivity.this, VacationDetailActivity.class);
            intent.putExtra("vacationId", selectedVacation.getId());
            intent.putExtra("title", selectedVacation.getTitle());
            intent.putExtra("hotel", selectedVacation.getHotel());
            intent.putExtra("startDate", selectedVacation.getStartDate());
            intent.putExtra("endDate", selectedVacation.getEndDate());

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadVacations();
    }

    private void loadVacations() {
        vacationObjects = db.vacationDAO().getAllVacations();
        List<String> vacationDisplayList = new ArrayList<>();

        for (Vacation vacation : vacationObjects) {
            vacationDisplayList.add(vacation.getTitle() + " - " + vacation.getHotel());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                vacationDisplayList
        );

        vacationListView.setAdapter(adapter);
    }
}