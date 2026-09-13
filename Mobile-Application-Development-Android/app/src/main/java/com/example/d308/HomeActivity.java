package com.example.d308;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button button = findViewById(R.id.buttonGoToVacations);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, VacationListActivity.class);
            startActivity(intent);
        });
    }
}