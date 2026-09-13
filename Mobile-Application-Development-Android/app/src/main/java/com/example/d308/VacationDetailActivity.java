package com.example.d308;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import androidx.room.Room;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.d308.database.AppDatabase;
import com.example.d308.entities.Vacation;

public class VacationDetailActivity extends AppCompatActivity {

    private EditText editTitle;
    private EditText editHotel;
    private EditText editStartDate;
    private EditText editEndDate;
    private Button buttonSave;
    private AppDatabase db;
    private Button buttonDelete;
    private Button buttonViewExcursions;
    private Button buttonShare;
    private Button buttonStartAlert;
    private Button buttonEndAlert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_detail);

        editTitle = findViewById(R.id.editTitle);
        editHotel = findViewById(R.id.editHotel);
        editStartDate = findViewById(R.id.editStartDate);
        editEndDate = findViewById(R.id.editEndDate);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonViewExcursions = findViewById(R.id.buttonViewExcursions);
        buttonShare = findViewById(R.id.buttonShare);
        buttonStartAlert = findViewById(R.id.buttonStartAlert);
        buttonEndAlert = findViewById(R.id.buttonEndAlert);

        int vacationId = getIntent().getIntExtra("vacationId", -1);
        String title = getIntent().getStringExtra("title");
        String hotel = getIntent().getStringExtra("hotel");
        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");

        if (vacationId != -1) {
            editTitle.setText(title);
            editHotel.setText(hotel);
            editStartDate.setText(startDate);
            editEndDate.setText(endDate);
        }

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "vacation_database")
                .allowMainThreadQueries()
                .build();

        //Save button logic
        buttonSave.setOnClickListener(v -> {
            String updatedTitle = editTitle.getText().toString().trim();
            String updatedHotel = editHotel.getText().toString().trim();
            String updatedStartDate = editStartDate.getText().toString().trim();
            String updatedEndDate = editEndDate.getText().toString().trim();

            //vacation date validation
            if (updatedTitle.isEmpty() || updatedHotel.isEmpty() || updatedStartDate.isEmpty() || updatedEndDate.isEmpty()) {
                Toast.makeText(VacationDetailActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidDate(updatedStartDate) || !isValidDate(updatedEndDate)) {
                Toast.makeText(VacationDetailActivity.this,
                        "Dates must be in MM/dd/yyyy format",
                        Toast.LENGTH_LONG).show();
                return;
            }

            if (!isEndDateAfterOrEqualStartDate(updatedStartDate, updatedEndDate)) {
                Toast.makeText(VacationDetailActivity.this,
                        "End date must be after or equal to start date",
                        Toast.LENGTH_LONG).show();
                return;
            }

            if (vacationId == -1) {
                Vacation vacation = new Vacation(updatedTitle, updatedHotel, updatedStartDate, updatedEndDate);
                db.vacationDAO().insert(vacation);
                Toast.makeText(VacationDetailActivity.this, "Vacation saved", Toast.LENGTH_SHORT).show();
            } else {
                Vacation vacation = new Vacation(updatedTitle, updatedHotel, updatedStartDate, updatedEndDate);
                vacation.setId(vacationId);
                db.vacationDAO().update(vacation);
                Toast.makeText(VacationDetailActivity.this, "Vacation updated", Toast.LENGTH_SHORT).show();
            }

            finish();
        });

        //delete vacay logic
        buttonDelete.setOnClickListener(v -> {

            if (vacationId != -1) {
                int excursionCount = db.excursionDAO().getExcursionCountForVacation(vacationId);

                if (excursionCount > 0) {
                    Toast.makeText(VacationDetailActivity.this,
                            "Cannot delete vacation with associated excursions",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                Vacation vacation = new Vacation(
                        editTitle.getText().toString().trim(),
                        editHotel.getText().toString().trim(),
                        editStartDate.getText().toString().trim(),
                        editEndDate.getText().toString().trim()
                );
                vacation.setId(vacationId);

                db.vacationDAO().delete(vacation);

                Toast.makeText(VacationDetailActivity.this, "Vacation deleted", Toast.LENGTH_SHORT).show();
                finish();
            }


        });
        //excursion button logic
        buttonViewExcursions.setOnClickListener(v -> {
            if (vacationId != -1) {
                Intent intent = new Intent(VacationDetailActivity.this, ExcursionListActivity.class);
                intent.putExtra("vacationId", vacationId);
                startActivity(intent);
            } else {
                Toast.makeText(VacationDetailActivity.this,
                        "Save the vacation before adding excursions",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //share button logic
        buttonShare.setOnClickListener(v -> {
            String shareText =
                    "Vacation Title: " + editTitle.getText().toString().trim() + "\n" +
                            "Hotel: " + editHotel.getText().toString().trim() + "\n" +
                            "Start Date: " + editStartDate.getText().toString().trim() + "\n" +
                            "End Date: " + editEndDate.getText().toString().trim();

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

            startActivity(Intent.createChooser(shareIntent, "Share vacation details"));
        });

        //alert button logic
        buttonStartAlert.setOnClickListener(v -> {
            String titleText = editTitle.getText().toString().trim();
            String startDateText = editStartDate.getText().toString().trim();

            scheduleAlert(startDateText, titleText + " is starting today", vacationId + 1000);
        });

        buttonEndAlert.setOnClickListener(v -> {
            String titleText = editTitle.getText().toString().trim();
            String endDateText = editEndDate.getText().toString().trim();

            scheduleAlert(endDateText, titleText + " is ending today", vacationId + 2000);
        });
    }

    private boolean isValidDate(String dateText) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        sdf.setLenient(false);

        try {
            Date date = sdf.parse(dateText);
            return date != null;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isEndDateAfterOrEqualStartDate(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        sdf.setLenient(false);

        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            return start != null && end != null && !end.before(start);
        } catch (ParseException e) {
            return false;
        }
    }

    //alarm helper
    private void scheduleAlert(String dateText, String message, int requestCode) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        sdf.setLenient(false);

        try {
            Date alertDate = sdf.parse(dateText);
            if (alertDate == null) return;

            Intent intent = new Intent(VacationDetailActivity.this, MyReceiver.class);
            intent.putExtra("message", message);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    VacationDetailActivity.this,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, alertDate.getTime(), pendingIntent);
                Toast.makeText(this, "Alert set", Toast.LENGTH_SHORT).show();
            }

        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date format. Use MM/dd/yyyy", Toast.LENGTH_LONG).show();
        }
    }
}