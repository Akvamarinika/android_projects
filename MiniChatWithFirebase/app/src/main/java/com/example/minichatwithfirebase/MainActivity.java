package com.example.minichatwithfirebase;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int SIGN_IN_REQUEST_CODE = 201;

    private RecyclerView recyclerViewMessages;
    private MessagesAdapter messagesAdapter;

    private EditText editTextMessage;
    private ImageView imageViewSendMessage;

    //private List<Message> messages;
    //private String author;

    private SharedPreferences preferences;

    private FirebaseFirestore firebaseDB;
    private FirebaseAuth mAuth;
    private ActivityResultLauncher<Intent> signInLauncher;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemSignOut){
            mAuth.signOut();
            signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage);

        messagesAdapter = new MessagesAdapter(this);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(messagesAdapter);

        //messages = new ArrayList<>();

        //author = "Akvamarin";
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        firebaseDB = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //проверка зарегистрован ли пользователь
        if (mAuth.getCurrentUser() != null){
            preferences.edit().putString("author", mAuth.getCurrentUser().getEmail()).apply();
        } else {
            Toast.makeText(this, "Not register!", Toast.LENGTH_SHORT).show();
            signOut();
        }

        //listenForChangesInDB();
        //readDataFromDB();
    }

    private void sendMessage(){
        String textOfMessage = editTextMessage.getText().toString().trim();
        String author = preferences.getString("author", "Anonymous");

        if (!textOfMessage.isEmpty()){
//            messages.add(new Message(author, textOfMessage));
//            messagesAdapter.setMessages(messages);

            // прокручивать RecyclerView до последнего ссобщения:
            recyclerViewMessages.scrollToPosition(messagesAdapter.getItemCount() - 1);

            /* "messages" == коллекция в которую будем сохранять*/
            firebaseDB.collection("messages")
                    .add(new Message(author, textOfMessage, System.currentTimeMillis()))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        // в случае успешного добавления в БД
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            editTextMessage.setText("");
                            Toast.makeText(MainActivity.this, "Запись успешно добавлена в БД", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                // в случае НЕуспешного добавления в БД
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Сообщение не отправлено! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }

//    private void readDataFromDB(){
//        firebaseDB.collection("messages").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(MainActivity.this, "Запись успешно прочитана из БД", Toast.LENGTH_SHORT).show();
//
//                            QuerySnapshot querySnapshot = task.getResult(); // содержит список док-ов
//                            if (querySnapshot == null) return;
//
//                            iterateOverListOfUsers(querySnapshot);
//
//                        } else {
//                            Toast.makeText(MainActivity.this, "Ошибка при чтении из БД: " + task.getException(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

    private void listenForChangesInDB(){
        firebaseDB.collection("messages").orderBy("date").addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {

                // подгрузка сообщений к приложению:
                if (queryDocumentSnapshots != null){
                    List<Message> messages = queryDocumentSnapshots.toObjects(Message.class);
                    messagesAdapter.setMessages(messages);
                    Log.d("debug", messages.toString());
                    //iterateOverListOfUsers(queryDocumentSnapshots);

                    //прокрутка для сообщений:
                    recyclerViewMessages.scrollToPosition(messagesAdapter.getItemCount() - 1);

                } else {
                    Toast.makeText(MainActivity.this, "queryDocumentSnapshots == null ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == Activity.RESULT_OK) {
                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null){
                    // вместо ника в чате, отобразим email автора
                    preferences.edit().putString("author", user.getEmail()).apply();
                    Toast.makeText(this, user.getEmail(), Toast.LENGTH_SHORT).show();
                }

            } else {

                if (response != null) {
                    Toast.makeText(this, "Error: " + response.getError(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }


    private void signOut() {
        // Intent intent = new Intent(this, RegisterActivity.class);
        // startActivity(intent);

        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){   // отправить польз-ля на стр. регистрации

                    // Провайдеры аутентификации:
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build());

                    // Создать и запустить интент входа:
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build(),
                            SIGN_IN_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //чтобы сообщения обновлялись, сразу при добавлении
        listenForChangesInDB();
    }
}

