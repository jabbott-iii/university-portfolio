package com.example.d308;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.d308.database.AppDatabase;
import com.example.d308.entities.Excursion;

import java.util.ArrayList;
import java.util.List;

public class ExcursionListActivity extends AppCompatActivity {

    private Button buttonAddExcursion;
    private ListView excursionListView;
    private AppDatabase db;
    private int vacationId;
    private List<Excursion> excursionObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_list);

        buttonAddExcursion = findViewById(R.id.buttonAddExcursion);
        excursionListView = findViewById(R.id.excursionListView);

        vacationId = getIntent().getIntExtra("vacationId", -1);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "vacation_database")
                .allowMainThreadQueries()
                .build();

        buttonAddExcursion.setOnClickListener(v -> {
            Intent intent = new Intent(ExcursionListActivity.this, ExcursionDetailActivity.class);
            intent.putExtra("vacationId", vacationId);
            startActivity(intent);
        });

        //Make excursion items clickable
        excursionListView.setOnItemClickListener((parent, view, position, id) -> {
            Excursion selectedExcursion = excursionObjects.get(position);

            Intent intent = new Intent(ExcursionListActivity.this, ExcursionDetailActivity.class);
            intent.putExtra("excursionId", selectedExcursion.getId());
            intent.putExtra("title", selectedExcursion.getTitle());
            intent.putExtra("date", selectedExcursion.getDate());
            intent.putExtra("vacationId", selectedExcursion.getVacationId());

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExcursions();
    }

    private void loadExcursions() {
        excursionObjects = db.excursionDAO().getExcursionsForVacation(vacationId);
        List<String> excursionDisplayList = new ArrayList<>();

        for (Excursion excursion : excursionObjects) {
            excursionDisplayList.add(excursion.getTitle() + " - " + excursion.getDate());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                excursionDisplayList
        );

        excursionListView.setAdapter(adapter);
    }
}