package com.example.xj.pomocyniemamcorobic;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class ActivityDetailsEntryActivity extends AppCompatActivity {

    EditText excuseField;
    EditText odField;
    EditText doField;
    EditText dayField;
    EditText gdzieField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_details_entry);

        excuseField = (EditText) findViewById(R.id.excuseField);
        odField = (EditText) findViewById(R.id.odField);
        doField = (EditText) findViewById(R.id.doField);
        gdzieField = (EditText) findViewById(R.id.gdzieField);
        dayField = (EditText) findViewById(R.id.dayField);

        odField.setInputType(InputType.TYPE_NULL);
        doField.setInputType(InputType.TYPE_NULL);

    }

    public void smileyButton_onClick(View view) {

        boolean cancel = false;

        if (odField.getText().toString().isEmpty()) {
            odField.setError("Proszę daj coś tu.");
            odField.requestFocus();
            cancel = true;
        }
        if (doField.getText().toString().isEmpty()) {
            doField.setError("Proszę daj coś tu.");
            doField.requestFocus();
            cancel = true;
        }
        if (dayField.getText().toString().isEmpty()) {
            dayField.setError("prosze daj coś tu");
            dayField.requestFocus();
            cancel = true;
        }

        if (cancel) return;

        Excuse excuse = new Excuse();

        excuse.location = gdzieField.getText().toString();
        excuse.description = excuseField.getText().toString();

        String vals[] = dayField.getText().toString().split("-");
        int intvals[] = new int[vals.length];
        for (int i = 0; i < vals.length; i++)
            intvals[i] = Integer.parseInt(vals[i]);

        Date beginDate = new Date(intvals[0], intvals[1], intvals[2], Integer.parseInt(odField.getText().toString().split(":")[0]),
                Integer.parseInt(odField.getText().toString().split(":")[1]));

        Date endDate = new Date(intvals[0], intvals[1], intvals[2], Integer.parseInt(doField.getText().toString().split(":")[0]),
                Integer.parseInt(doField.getText().toString().split(":")[1]));

        if (beginDate.after(endDate)) {
            doField.setError("Błąd: koniec nie może być przed początkiem");
        } else {


            excuse.begin = beginDate;
            excuse.end = endDate;

            MyCalendar.content.add(excuse);

            Snackbar.make(view, "Dodano! :))))", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            SendTimetableTask task = new SendTimetableTask();
            task.execute(MyCalendar.nickname);
            int result = 0;
            try {
                result = task.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (result == 1)
                Toast.makeText(this, "Kalendarz zaktualizowany", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Coś poszło nie tak w trakcie aktualizacji", Toast.LENGTH_SHORT).show();

            super.onBackPressed();
            return;
        }
    }

    public void wrucButton_onClick(View view) {

        super.onBackPressed();
        return;
    }

    public void timeField_onClick(View view) {
        TimePickerFragment timePickerFragment;
        EditText editText = (EditText) view;
        timePickerFragment = new TimePickerFragment();
        timePickerFragment.bind(editText);
        timePickerFragment.show(this.getSupportFragmentManager(), "timePickers");
    }

    public void dayField_onClick(View view) {
        DatePickerFragment datePickerFragment;
        EditText editText = (EditText) view;
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.bind(editText);
        datePickerFragment.show(this.getSupportFragmentManager(), "datePicker");
    }
}


