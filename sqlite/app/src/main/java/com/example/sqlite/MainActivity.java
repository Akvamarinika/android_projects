package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotes; /*ссылка на RecyclerView*/
    public static final List<Note> notes = new ArrayList<>(); /*лист с заметками*/
    private NotesDBHelper notesDBHelper; /*из helper получим DB*/
    private TextView textViewCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        notesDBHelper = new NotesDBHelper(this);
        SQLiteDatabase database = notesDBHelper.getWritableDatabase(); /*писать в DB*/

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        try {
            /*Нужно убрать после создания БД, т.к. повторно добавяться эти заметки*/
            Date date = formatter.parse("09-12-2021");
            notes.add(new Note(date,"Парикмахер", "1", "Пн", 3));

            Date date2 = formatter.parse("05-12-2021");
            notes.add(new Note(date2,"Магазин", "Купить хлеб", "Пт", 2));

            Date date3 = formatter.parse("13-12-2021");
            notes.add(new Note(date3,"Собеседование", "Пройти собеседование", "", 1));

            Date date4 = formatter.parse("23-12-2021");
            notes.add(new Note(date4,"Экзамен", "Сдать экзамен", "Чт", 1));

            Date date5 = formatter.parse("30-12-2021");
            notes.add(new Note(date5,"Зачет", "Сдать зачет", "Чт", 1));

        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }

        /*взять все заметки из Листа notes и вставить в ДБ*/
        /*key == header_table */
        for (Note note : notes){
            ContentValues contentValues = new ContentValues(); //вставить нов, запись в ДБ
            contentValues.put(NotesContract.NotesEntry.COLUMN_TITLE, note.getTitle());
            contentValues.put(NotesContract.NotesEntry.COLUMN_DATE, formatter.format(note.getDate()));
            contentValues.put(NotesContract.NotesEntry.COLUMN_DESCRIPTION, note.getDescription());
            contentValues.put(NotesContract.NotesEntry.COLUMN_DAY_OF_WEEK, note.getDayOfWeek());
            contentValues.put(NotesContract.NotesEntry.COLUMN_PRIORITY, note.getPriority());

            /*вставка в DB*/
            database.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValues);
        }

        /*читать из DB и вставить в List*/
        List<Note> notesFromDB = new ArrayList<>();
        Cursor cursor = database.query(NotesContract.NotesEntry.TABLE_NAME, null, null, null, null, null, null);

        int count = 0;
        textViewCount = findViewById(R.id.textViewCount);

        while (cursor.moveToNext()){
            String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_TITLE));
            String dateStr = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_DATE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_DESCRIPTION));
            String dayOfWeek = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_DAY_OF_WEEK));
            int priority = cursor.getInt(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_PRIORITY));

            Date date = null;
            try {
                date = formatter.parse(dateStr);
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }

            Note note = new Note(date, title, description, dayOfWeek, priority);
            notesFromDB.add(note);
            count += 1;
        }
        cursor.close();
        textViewCount.setText(String.format("%s %s", "Кол-во заметок: ", count));

        NotesAdapter adapter = new NotesAdapter(notesFromDB);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewNotes.setAdapter(adapter);


    }

    public void onClickAddNote(View view) {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivity(intent);
    }
}