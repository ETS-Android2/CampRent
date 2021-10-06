package com.test.camprent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


public class detailActivity extends AppCompatActivity {
    private Button mUpdate_btn;
    private Button mDelete_btn;
    private TextView NameTxt,phoneTxt,priceTxt,fromTxt,toTxt,locationTxt,keytxt,mail_verif,mail_pub,url;
    private ImageView imagetxt;
    private static final int REQUEST_CALL = 1;
    private String phone;
    private ImageButton fvrt_btn;
    int fvrtchecker ;
    private Button backbtnfavorit;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //retour au search
        backbtnfavorit = findViewById(R.id.backdetail);
        //image favorite
        fvrt_btn=findViewById(R.id.fvrt_f2_item_jdida);


        //L'appel
        ImageView imageCall = findViewById(R.id.image_call);
        //Pour l'affichage
        NameTxt = findViewById(R.id.name_detail);
        phoneTxt = findViewById(R.id.phone_detail);
        priceTxt = findViewById(R.id.price_detail);
        fromTxt = findViewById(R.id.from_detail);
        toTxt = findViewById(R.id.to_detail);
        locationTxt = findViewById(R.id.location_detail);
        imagetxt = findViewById(R.id.image_detail);
        keytxt = findViewById(R.id.key_detail);
        mail_pub=findViewById(R.id.mail_publication);
        mail_verif=findViewById(R.id.mail_verif1);
        url=findViewById(R.id.detail_url);



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
        mail_pub.setText(mail_publish);
        mail_verif.setText(mail_verfication);

        mUpdate_btn = (Button) findViewById(R.id.update_btn);
        mDelete_btn = (Button) findViewById(R.id.delete_btn);
        backbtnfavorit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),interface3.class));

            }
        });
        String finalKeeey = keeey;
        fvrt_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                // if (fvrtchecker.equals(false)){
                switch (fvrtchecker){
                    case 0:
                        Upload uploaded= new Upload();
                        uploaded.setName(NameTxt.getText().toString());
                        uploaded.setPhone(phoneTxt.getText().toString());
                        uploaded.setPrice(priceTxt.getText().toString());
                        uploaded.setFrom(fromTxt.getText().toString());
                        uploaded.setTo(toTxt.getText().toString());
                        uploaded.setLocation1(locationTxt.getText().toString());
                        uploaded.setKey(keytxt.getText().toString());
                        uploaded.setmail(mail_verif.getText().toString());
                        uploaded.setImageUrl(url.getText().toString());

                        new interface3().add_favorites(finalKeeey,uploaded);
                        Toast.makeText(getApplicationContext(),"Item added to favorites",Toast.LENGTH_SHORT).show();
                        fvrtchecker=1;






                        fvrt_btn.setImageResource(R.drawable.favourits_24);
                        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                        ft1.replace(R.id.layout,new ProfileFragment());
                        ft1.commit();
                        break;
                    case 1:
                        new interface3().deletedata_fvrt(finalKeeey);


                        fvrtchecker=0;
                        fvrt_btn.setImageResource(R.drawable.favouriteoff_24);
                        Toast.makeText(getApplicationContext(),"deleted",Toast.LENGTH_SHORT).show();
                        break;




                }
                fvrtchecker=1;
                fvrt_btn.setImageResource(R.drawable.favourits_24);

            }

        });

        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });




        String test1=mail_pub.getText().toString();
        String test2 =mail_verif.getText().toString();
        String testk="test";
        if(test1!=test2)  mDelete_btn.setVisibility(mDelete_btn.INVISIBLE);
        if(test1.equals(test2)) mDelete_btn.setVisibility(mDelete_btn.VISIBLE) ;

        if(test1!=test2) mUpdate_btn.setVisibility(mUpdate_btn.INVISIBLE);
        if(test1.equals(test2)) mUpdate_btn.setVisibility(mDelete_btn.VISIBLE) ;

        mDelete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new interface3().deletedata(finalKeeey);
                Toast.makeText(getApplicationContext(),"Post deleted",Toast.LENGTH_SHORT).show();

                openActivity_interface3();

            }
        });



        mUpdate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityEdit();
            }


        });
    }

    private void makePhoneCall() {
        String number = phone;
        if (number.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(detailActivity.this,
                    Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(detailActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);

            }else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        } else {
            Toast.makeText(detailActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
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

    private void openActivity_interface3() {
        Intent i =new Intent(this,interface3.class);
        startActivity(i);
    }


    private void openActivityEdit() {
        Intent intent =new Intent(this,EditActivity.class);
        intent.putExtra("name",NameTxt.getText().toString());
        intent.putExtra("phone",phoneTxt.getText().toString());
        intent.putExtra("price",priceTxt.getText().toString());
        intent.putExtra("to",toTxt.getText().toString());
        intent.putExtra("image",url.getText().toString());

        intent.putExtra("from",fromTxt.getText().toString());
        intent.putExtra("location",locationTxt.getText().toString());
        intent.putExtra("mail_pub",mail_pub.getText().toString());
        intent.putExtra("mail_verif",mail_verif.getText().toString());


        intent.putExtra("key",keytxt.getText().toString());
        startActivity(intent);

    }









}

