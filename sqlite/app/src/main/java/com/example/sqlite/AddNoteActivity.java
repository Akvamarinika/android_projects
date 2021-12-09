package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText date;
    private EditText editTextDescription;
    private Spinner editTextDaysOfWeek;
    private RadioGroup radioGroupPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextTitle = findViewById(R.id.editTextTitle);
        date = findViewById(R.id.editTextDate);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDaysOfWeek = findViewById(R.id.spinnerDayOfWeek);
        radioGroupPriority = findViewById(R.id.radioGroup);

    }

    public void onClickSaveNote(View view) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        String title = editTextTitle.getText().toString().trim();
        String dateStr = date.getText().toString().trim();
        Date date = null;
        try {
            date = formatter.parse(dateStr);
        } catch (ParseException e) {
            System.out.println(e.getMessage() + " Формат даты: dd-MM-yyyy");
        }
        String description = editTextDescription.getText().toString().trim();
        String dayOfWeek = editTextDaysOfWeek.getSelectedItem().toString();
        int radioButtonId = radioGroupPriority.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(radioButtonId);
        int priority = Integer.parseInt(radioButton.getText().toString());

        Note note = new Note(date,title,description,dayOfWeek,priority);
        MainActivity.notes.add(note);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }
}