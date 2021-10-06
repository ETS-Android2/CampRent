package com.test.camprent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import hotchemi.android.rate.AppRate;

public class Dashboard extends AppCompatActivity {

    //firebase auth
    FirebaseAuth firebaseAuth;
    ActionBar actionBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //rate
        AppRate.with(this).setInstallDays(0).setLaunchTimes(1).setRemindInterval(2).monitor();
        AppRate.showRateDialogIfMeetsConditions(this);


        //ActionBar and its title
        actionBar = getSupportActionBar();

        //init
        firebaseAuth = FirebaseAuth.getInstance();

        //bottom navigation
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);
//home fragment transaction (default, on star)
        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content, fragment1, "");
        ft1.commit();



    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            //handle item clicks
            switch (item.getItemId()){
                case R.id.nav_home:
                    //home fragment transaction
                    HomeFragment fragment1 = new HomeFragment();
                    FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                    ft1.replace(R.id.content, fragment1, "");
                    ft1.commit();
                    return true;
                case R.id.Profile:
                    //Profile fragment transaction
                    ProfileFragment fragment2 = new ProfileFragment();
                    FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                    ft2.replace(R.id.content, fragment2, "");
                    ft2.commit();
                    return true;

            }
            return false;
        }
    };


    private void checkUserStatus (){
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null ) {
            //user is signed in stay here
            //set email of logged in user
        }
        else {
            //user not signed in, go to main activity
            startActivity(new Intent(Dashboard.this, HomeActivity.class));

        }
    }
    @Override
   protected void onStart (){
        //check on start of app
        checkUserStatus();
        super.onStart();
    }
}