package com.test.camprent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    EditText mFullName,mEmail,mpassword,mphone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth mAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);




        mFullName=findViewById(R.id.FullName);
        mEmail=findViewById(R.id.Email);
        mpassword=findViewById(R.id.password);
        mphone=findViewById(R.id.phone);

        mRegisterBtn=findViewById(R.id.registerBtn);
        mLoginBtn=findViewById(R.id.sign_in_here);
        mAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);
        //already login
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),Dashboard.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email =mEmail.getText().toString().trim();
                String password=mpassword.getText().toString().trim();
                //validate
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    //set error and focus to email edittext
                    mEmail.setError("Invalid Email");
                    mEmail.setFocusable(true);
                }

                else if(password.length()< 6){
                    mpassword.setError("Password length at least 6 characters ");
                    mpassword.setFocusable(true);
                }
                else {
                    registerUser(email,password); // register the user
                }
                progressBar.setVisibility(View.VISIBLE);





            }

        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

    }

    private void registerUser(String email, String password) {
    // email and password pattern is valid, show progress dialog and start registering user
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //Sign in success, update UI with the signed-in user's information

                    FirebaseUser user = mAuth.getCurrentUser();
                    //Get user email and uid from auth
                    String email = user.getEmail();
                    String uid = user.getUid();
                    String name = mFullName.getText().toString().trim();
                    String phone = mphone.getText().toString().trim();
                    //when user is registered store user info in firebase realtime database too
                    //using HashMap
                    HashMap<Object,String> hashMap = new HashMap<>();
                    // put info in HashMap
                    hashMap.put("email",email);
                    hashMap.put("uid",uid);
                    hashMap.put("name",name);
                    hashMap.put("phone",phone); // will add later example edit profile
                    hashMap.put("image",""); // will add later example edit profile
                    hashMap.put("cover",""); // will add later example edit profile
                    // firebase database instance
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    //path to store uer data named "Users"
                    DatabaseReference reference = database.getReference("Users");
                    //put data within hashmap in database
                    reference.child(uid).setValue(hashMap);


                    Toast.makeText(Register.this,"Welcome New Camper! Join the Team",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Dashboard.class));
                }
                else{
                    //if sign in fails, display a message to the user.


                    Toast.makeText(Register.this,"error!"+task.getException().getMessage(),Toast.LENGTH_LONG).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //error, dismiss progress dialog and get and show the error msg

                Toast.makeText(Register.this , ""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }
}