package com.example.d308;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.d308.database.AppDatabase;
import com.example.d308.entities.Excursion;
import com.example.d308.entities.Vacation;

public class ExcursionDetailActivity extends AppCompatActivity {

    private EditText editExcursionTitle;
    private EditText editExcursionDate;
    private Button buttonSaveExcursion;
    private AppDatabase db;
    private int vacationId;
    private Button buttonDeleteExcursion;
    private Button buttonExcursionAlert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_detail);

        editExcursionTitle = findViewById(R.id.editExcursionTitle);
        editExcursionDate = findViewById(R.id.editExcursionDate);
        buttonSaveExcursion = findViewById(R.id.buttonSaveExcursion);
        buttonDeleteExcursion = findViewById(R.id.buttonDeleteExcursion);
        buttonExcursionAlert = findViewById(R.id.buttonExcursionAlert);

        vacationId = getIntent().getIntExtra("vacationId", -1);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "vacation_database")
                .allowMainThreadQueries()
                .build();

        //excursion data
        int excursionId = getIntent().getIntExtra("excursionId", -1);
        String title = getIntent().getStringExtra("title");
        String date = getIntent().getStringExtra("date");

        if (excursionId != -1) {
            editExcursionTitle.setText(title);
            editExcursionDate.setText(date);
        }

        buttonSaveExcursion.setOnClickListener(v -> {
            String updatedTitle = editExcursionTitle.getText().toString().trim();
            String updatedDate = editExcursionDate.getText().toString().trim();

            if (updatedTitle.isEmpty() || updatedDate.isEmpty()) {
                Toast.makeText(ExcursionDetailActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Vacation vacation = db.vacationDAO().getVacationById(vacationId);

            if (vacation == null) {
                Toast.makeText(ExcursionDetailActivity.this, "Associated vacation not found", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isDateWithinVacation(updatedDate, vacation.getStartDate(), vacation.getEndDate())) {
                Toast.makeText(ExcursionDetailActivity.this,
                        "Excursion date must be within the vacation dates (MM/dd/yyyy)",
                        Toast.LENGTH_LONG).show();
                return;
            }

            if (excursionId == -1) {
                Excursion excursion = new Excursion(updatedTitle, updatedDate, vacationId);
                db.excursionDAO().insert(excursion);
                Toast.makeText(ExcursionDetailActivity.this, "Excursion saved", Toast.LENGTH_SHORT).show();
            } else {
                Excursion excursion = new Excursion(updatedTitle, updatedDate, vacationId);
                excursion.setId(excursionId);
                db.excursionDAO().update(excursion);
                Toast.makeText(ExcursionDetailActivity.this, "Excursion updated", Toast.LENGTH_SHORT).show();
            }

            finish();
        });

        buttonDeleteExcursion.setOnClickListener(v -> {
            if (excursionId != -1) {
                Excursion excursion = new Excursion(
                        editExcursionTitle.getText().toString().trim(),
                        editExcursionDate.getText().toString().trim(),
                        vacationId
                );
                excursion.setId(excursionId);

                db.excursionDAO().delete(excursion);

                Toast.makeText(ExcursionDetailActivity.this, "Excursion deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //excursion button logic
        buttonExcursionAlert.setOnClickListener(v -> {
            String titleText = editExcursionTitle.getText().toString().trim();
            String dateText = editExcursionDate.getText().toString().trim();

            int requestCode = excursionId != -1 ? excursionId + 3000 : vacationId + 3000;

            scheduleExcursionAlert(dateText, titleText + " is happening today", requestCode);
        });
    }

    private boolean isDateWithinVacation(String excursionDate, String vacationStart, String vacationEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        sdf.setLenient(false);

        try {
            Date excursion = sdf.parse(excursionDate);
            Date start = sdf.parse(vacationStart);
            Date end = sdf.parse(vacationEnd);

            return excursion != null && start != null && end != null &&
                    !excursion.before(start) && !excursion.after(end);

        } catch (ParseException e) {
            return false;
        }
    }

    //excursion alert helper
    private void scheduleExcursionAlert(String dateText, String message, int requestCode) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        sdf.setLenient(false);

        try {
            Date alertDate = sdf.parse(dateText);
            if (alertDate == null) return;

            Intent intent = new Intent(ExcursionDetailActivity.this, MyReceiver.class);
            intent.putExtra("message", message);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    ExcursionDetailActivity.this,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, alertDate.getTime(), pendingIntent);
                Toast.makeText(this, "Excursion alert set", Toast.LENGTH_SHORT).show();
            }

        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date format. Use MM/dd/yyyy", Toast.LENGTH_LONG).show();
        }
    }

}