package com.test.camprent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

public class IntroductoryActivity extends AppCompatActivity {
    ImageView logo,appName,spalchImg;
    LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);
        logo=findViewById(R.id.logo);
        appName=findViewById(R.id.app_name);
        spalchImg=findViewById(R.id.img);
        lottieAnimationView=findViewById(R.id.lottie);
        spalchImg.animate().translationY(-1600).setDuration(1000).setStartDelay(4000);
        logo.animate().translationY(-1400).setDuration(1000).setStartDelay(4000);
        appName.animate().translationY(-1600).setDuration(1000).setStartDelay(4000);
        lottieAnimationView.animate().translationY(-1400).setDuration(1000).setStartDelay(4000);

        final Intent i = new Intent(this,Register.class);
        Thread timer =new Thread(){
            public void run(){
                try {
                    sleep(5000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();

    }
}