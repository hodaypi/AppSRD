package com.srd.srdapplication;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

//check the commit and push
public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
//        Map<String, Object> data = new HashMap<String, Object>();
//
//
//        Map<String, Object> user = new HashMap<>();
//        user.put("first", "Alan");
//        user.put("middle", "Mathison");
//        user.put("last", "Turing");
//        user.put("born", 1912);

// Add a new document with a generated ID
//        db.collection("users")
//                .add(user);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity.this, Intro.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }

}

//        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//        @Override
//        public void onSuccess(DocumentReference documentReference) {
//            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//        }
//    })
//            .addOnFailureListener(new OnFailureListener() {
//        @Override
//        public void onFailure(@NonNull Exception e) {
//            Log.w(TAG, "Error adding document", e);
//        }
//});
//}