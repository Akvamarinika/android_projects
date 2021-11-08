package com.example.adapters;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView bookList;
    List<Book> myBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bookList = findViewById(R.id.listview);

        //1- подготовка list book
        myBooks = new ArrayList<>();
        myBooks.add(new Book("А. Азимов", "Основание", R.drawable.osnovanie));
        myBooks.add(new Book("Н. Гоголь", "Шинель", R.drawable.shinel));
        myBooks.add(new Book("Е. Гаглоев", "Зерцалия", R.drawable.zertsalia));
        myBooks.add(new Book("В. Штерн", "Ледяная скорлупа", 0));
        myBooks.add(new Book("Р. Хайнлайн", "Гражданин галактики", 0));

        Intent intent = getIntent();
        if (intent != null){
            if (intent.hasExtra("name") && intent.hasExtra("author")){
                String nameBook = intent.getExtras().getString("name");
                String authorBook = intent.getExtras().getString("author");
                myBooks.add(new Book(authorBook, nameBook, 0));
                Toast toast = Toast.makeText(this, "Новая книга добавлена.",Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(this, "Книга не будет добавлена! Не введен автор или название книги.",Toast.LENGTH_LONG);
                toast.show();
            }
        }


        //2- преобразование для Simple adapter
        List<HashMap<String, Object>> library = new ArrayList<>();
        for (int i = 0; i< myBooks.size(); i++) {
            HashMap<String, Object> book = new HashMap<>();
            book.put("author", myBooks.get(i).author);
            book.put("name", myBooks.get(i).name);
            book.put("cover", myBooks.get(i).cover);
            library.add(book);
        }

        //3 - подготовка массивов from и to
        String[] from = {"author", "name", "cover"};
        int[] to = {R.id.author, R.id.name, R.id.cover};

        //4 - создаем adapter
        SimpleAdapter adapter = new SimpleAdapter(this, library, R.layout.list_item, from, to);

        //5- применяем adapter
        bookList.setAdapter(adapter);
    }


    public void addNewBook(View view){
        Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
        startActivity(intent);
    }


}