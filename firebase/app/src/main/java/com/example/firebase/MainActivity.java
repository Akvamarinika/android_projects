package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements ValueEventListener {

    private Place city = new Place("Irkutsk", 103.6, 53);
    private DatabaseReference dbRef;
    public static final String CHILD = "myplace";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbRef = FirebaseDatabase.getInstance().getReference(); // получаем ссылку на облачную БД
        dbRef.child(CHILD).addValueEventListener(this); //следим за изменениями в 'каталоге myplace'
        сhangePlace(city);
    }

    public void сhangePlace(Place place) {
       dbRef.child(CHILD).setValue(place); //аналог каталога
       dbRef.child(CHILD).push().setValue(new Place("Moscow", 110, 222));
       dbRef.child(CHILD).push().setValue(new Place("Volgograd", 33, 44));
       dbRef.child(CHILD).push().setValue(new Place("Novosibirsk", 12, 50));
       dbRef.child(CHILD).push().setValue(new Place("Sochi", 123, 321));

//       dbRef.child("myplace").child("otherplace").setValue(place);
//       dbRef.child("myplace").push().setValue(place); //push == положить в 'каталог myplace'
//
//        dbRef.push().setValue(place);
    }

    //когда данные поменялись делает снимок БД
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

//        Place place = snapshot.getValue(Place.class); // получить одно знач-е
//        Log.d("mytag","key: " + snapshot.getKey());
//        Log.d("mytag", "place: " + place);

        for (DataSnapshot ds: snapshot.getChildren() ) { // return Iterator  == snapshot.getChildren()
            String name = ds.child("name").getValue(String.class);
            Double lat = ds.child("lat").getValue(Double.class);
            Double lon = ds.child("lon").getValue(Double.class);

            if (name != null){
                Log.d("mytag", name + " / " + lat + " / " + lon);
            }



//            Log.d("mytag", "key: " + ds.getKey()); // print child ('каталог myplace')
//            System.out.println(ds.getKey());
//            Place place = ds.getValue(Place.class);
//            Log.d("mytag", "place: " + place); // print obj place
        }
    }
     

    @Override
    public void onCancelled(@NonNull DatabaseError error) {}
}