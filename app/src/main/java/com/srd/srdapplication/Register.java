package com.srd.srdapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.adi;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    //    FirebaseDatabase database;
    FirebaseFirestore database;
    String userID;
    TextView mCreateBtn;

    DatabaseReference mDatabase;
    private static final String USER = "user";
    private User user;
    private String email;
    private String password;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEmail = findViewById(R.id.emailAddress);
        mPassword = findViewById(R.id.password);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mLoginBtn = findViewById(R.id.createText);
        fAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        mCreateBtn=findViewById(R.id.createText);

//        mDatabase = database.getReference(USER);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEmail.getText().toString() != null && mPassword.getText().toString() != null) {
                    email = mEmail.getText().toString();
                    password = mPassword.getText().toString();
                    url = null;

                    user = new User(email,url);
                    registerUser();
//                String email = mEmail.getText().toString().trim();
//                String password = mPassword.getText().toString().trim();
//                String url = null;
//
//                if (TextUtils.isEmpty(email)) {
//                    mEmail.setError("Email is required");
//                    return;
//                }
//                if (TextUtils.isEmpty(password)) {
//                    mPassword.setError("passeord is required");
//                    return;
//                }
//                if (password.length() < 6) {
//                    mPassword.setError("Password must contain at least 6 characters");
//                    return;
//                }
                    //register to firebase
                }
            }
        });
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

    }


    public void registerUser () {
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //user = new User(email, password, url);
                    userID=fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference=database.collection("user").document(userID);
                    Map<String,Object> user =new HashMap<>();
                    user.put("email",email);
                    user.put("url",url);

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("TAG","succ");
                        }
                    });
                    //FirebaseUser user = fAuth.getCurrentUser();
                    //updateUI(user);
                    Toast.makeText(Register.this, "User created", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), Dashborad.class));

                } else {
                    Toast.makeText(Register.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


}