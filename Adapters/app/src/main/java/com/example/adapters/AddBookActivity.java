package com.example.adapters;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

    }

    public void addItemInList(View view) {
        Intent intent = new Intent(AddBookActivity.this, MainActivity.class);
        EditText inputName = findViewById(R.id.inputName);
        String nameTxt = inputName.getText().toString();

        EditText inputAuthor = findViewById(R.id.inputAuthor);
        String authorTxt = inputAuthor.getText().toString();

        if (!nameTxt.isEmpty()){
            intent.putExtra("name", nameTxt);
            inputName.setText("");
        }

        if (!authorTxt.isEmpty()){
            intent.putExtra("author", authorTxt);
            inputAuthor.setText("");
        }

        startActivity(intent);
    }

    public void closeAddActivity(View view) {
        finish();
    }

}