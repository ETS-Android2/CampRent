package com.test.camprent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class interface3 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    private RecyclerView mRecyclerView;
    private FirebaseAuth fAuth_int2;



    private ImageAdapter mAdapter;
    private DatabaseReference mDataBaeRef;
    private List<Upload> mUploads;
    private ImageAdapter.RecyclerViewClickListener listener;
    private TextView mail_ent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface3);


        /*-----------------Hooks------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        //fragment





        /*---Navigation Drawer Menu ----*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        EditText editText=findViewById(R.id.search_bar);
        mail_ent=findViewById(R.id.mail_entry);
        fAuth_int2=FirebaseAuth.getInstance();
        String mail_utlisatuer=fAuth_int2.getCurrentUser().getEmail();
        mail_ent.setText(mail_utlisatuer);



        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });
        //recycleview
        mRecyclerView = findViewById(R.id.recycleview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUploads = new ArrayList<>();

        mDataBaeRef = FirebaseDatabase.getInstance().getReference("Uploads");
        mDataBaeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapShot.getValue(Upload.class);
                    mUploads.add(upload);

                }
                setOnClickListener();

                mAdapter = new ImageAdapter(interface3.this, mUploads, listener);
                mRecyclerView.setAdapter(mAdapter);


            }

            private void setOnClickListener() {
                listener = new ImageAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View vi, int pos) {
                        Intent intent = new Intent(getApplicationContext(), detailActivity.class);
                        intent.putExtra("name", mUploads.get(pos).getName());
                        intent.putExtra("key", mUploads.get(pos).getKey());
                        intent.putExtra("phone", mUploads.get(pos).getPhone());
                        intent.putExtra("price", mUploads.get(pos).getPrice());
                        intent.putExtra("from", mUploads.get(pos).getFrom());
                        intent.putExtra("to", mUploads.get(pos).getTo());
                        intent.putExtra("location", mUploads.get(pos).getLocation1());
                        intent.putExtra("image", mUploads.get(pos).getImageUrl());
                        intent.putExtra("mail_publish",mUploads.get(pos).getmail());
                        intent.putExtra("mail_enty",mail_ent.getText().toString().trim());
                        startActivity(intent);
                    }
                };
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(interface3.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }

        });


    }


    private void filter(String text){
        ArrayList<Upload> filteredList=new ArrayList<>();
        for (Upload uploading:mUploads){
            if(uploading.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(uploading);
            }
        }
        mAdapter.filteredlist(filteredList);
    }

    public void updatedata(String key, Upload uploaded) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Uploads").child(key);
        databaseReference.setValue(uploaded);

    }
    public void deletedata(String key){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Uploads").child(key);
        databaseReference.setValue(null);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();


        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()){
            case R.id.nav_home:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.drawer_layout,new HomeFragment());
                ft.commit();
                break;

            case R.id.nav_share:

                break;

            case R.id.nav_logout:
                startActivity(new Intent(getApplicationContext(),Login.class));
                break;
            case R.id.nav_rate:
                goToFacebookPage("104822231606030");
                break;

            case R.id.nav_profile:
                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.drawer_layout,new ProfileFragment());
                ft1.commit();
                break;
            case R.id.nav_favorit:
                FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                ft3.replace(R.id.drawer_layout,new ProfileFragment());
                ft3.commit();
                break;



            case R.id.nav_info:
                startActivity(new Intent(getApplicationContext(),AboutUsActivity.class));
                break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToFacebookPage(String s) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + s));
            startActivity(intent);
        } catch (ActivityNotFoundException e ){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + s));
        }
    }
    public void add_favorites(String key,Upload uploaded){
        DatabaseReference database=FirebaseDatabase.getInstance().getReference("favourites");
        final String  uploadId = database.push().getKey();
        uploaded.setKey(uploadId);
        database.child(uploadId).setValue(uploaded);






    }
    public void deletedata_fvrt(String key){


        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("favourites").child(key);
        databaseReference.setValue(null);
    }

}