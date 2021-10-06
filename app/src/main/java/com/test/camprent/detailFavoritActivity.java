package com.test.camprent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class detailFavoritActivity extends AppCompatActivity {
    private TextView NameTxt,phoneTxt,priceTxt,fromTxt,toTxt,locationTxt,keytxt,url;
    private ImageView imagetxt;
    private static final int REQUEST_CALL = 1;
    private String phone;
    private Button backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);
        //L'appel
        ImageView imageCall = findViewById(R.id.image_callfavorit);
        //retour au profile
        backbtn = findViewById(R.id.back);
        //Pour l'affichage
        NameTxt = findViewById(R.id.name_detailfavorit);
        phoneTxt = findViewById(R.id.phone_detailfavorit);
        priceTxt = findViewById(R.id.price_detailfavorit);
        fromTxt = findViewById(R.id.from_detailfavorit);
        toTxt = findViewById(R.id.to_detailfavorit);
        locationTxt = findViewById(R.id.location_detailfavorit);
        imagetxt = findViewById(R.id.image_detailfavorit);
        keytxt = findViewById(R.id.key_detailfavorit);
        url=findViewById(R.id.detail_urlfavorit);
        String name = "phone not set at first";
        phone = "phone not set at first";
        String price = "phone not set at first";
        String from = "phone not set at first";
        String to = "phone not set at first";
        String location = "phone not set at first";
        String image = "image";
        String keeey = "key";
        String mail_publish="mail not set";
        String mail_verfication="mail not set";
        String url_s="notset";


        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            name = extras.getString("name");
            phone = extras.getString("phone");
            price = extras.getString("price");
            from = extras.getString("from");
            to = extras.getString("to");
            location = extras.getString("location");
            image = extras.getString("image");
            keeey = extras.getString("key");
            mail_publish=extras.getString("mail_publish");
            mail_verfication=extras.getString("mail_enty");
            url_s=extras.getString("image");

        }

        NameTxt.setText(name);
        phoneTxt.setText(phone);
        priceTxt.setText(price);
        fromTxt.setText(from);
        toTxt.setText(to);
        locationTxt.setText(location);
        keytxt.setText(keeey);
        url.setText(url_s);
        Glide.with(this).load(image)
                .fitCenter()
                .centerCrop()
                .apply(new RequestOptions().override(200, 200))
                .into(imagetxt);
        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.layoutfavorit,new ProfileFragment());
                ft1.commit();

            }
        });

    }
    private void makePhoneCall() {
        String number = phone;
        if (number.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(detailFavoritActivity.this,
                    Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(detailFavoritActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);

            }else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        } else {
            Toast.makeText(detailFavoritActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            } else {
                Toast.makeText(this,"Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

}