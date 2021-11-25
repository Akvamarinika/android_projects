package com.example.recyclerviewcolors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView rview;
    ArrayList<Integer> numbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setInitialData();

        rview = findViewById(R.id.rview);
        ColorAdapter adapter = new ColorAdapter(getLayoutInflater(), numbers);
        adapter.submitList(numbers);
        rview.setLayoutManager(new LinearLayoutManager(this));
        // задаём оформление
        rview.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rview.setAdapter(adapter);

    }

    private void setInitialData(){
        numbers.add(R.color.black);
        numbers.add(R.color.white);
        numbers.add(R.color.blue);
        numbers.add(R.color.purple_200);
        numbers.add(R.color.purple_500);
        numbers.add(R.color.purple_700);
        numbers.add(R.color.teal_200);
        numbers.add(R.color.teal_700);
    }
}