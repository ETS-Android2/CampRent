package com.test.camprent;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;


public class EditActivity extends AppCompatActivity {
    private Button mDatePickerBtnfrom; // button from
    private Button mDatePickerBtnto; // button to
    private TextView mSelectedDateText1;
    private TextView mSelectedDateText2;
    Button edit_btn;
    Button btn_return;
    Button image_edit_btn;
    FirebaseAuth firebaseAuth;
    private Uri mImageUri;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    ImageView imagetxt;
    private ProgressBar mProgressBar;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //image
        mStorageRef= FirebaseStorage.getInstance().getReference("Uploads");
        mProgressBar=findViewById(R.id.progressBar3);
        //
        setContentView(R.layout.activity_edit);

        Spinner NameTxt=(Spinner) findViewById(R.id.name_edit);
        EditText phoneTxt=findViewById(R.id.phone_edit);
        EditText priceTxt=findViewById(R.id.price_edit);
        EditText locationTxt=findViewById(R.id.location_edit);
        TextView mail_pub_3=findViewById(R.id.pub_edit);
        TextView mail_verif_3=findViewById(R.id.verif_edit);
        mSelectedDateText2 = findViewById(R.id.to_date);
        mDatePickerBtnfrom = findViewById(R.id.from_date);
        mDatePickerBtnto = findViewById(R.id.to_date);
        mSelectedDateText2 = findViewById(R.id.to_date);
        mSelectedDateText1 = findViewById(R.id.from_date);
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = builder.build();
        final MaterialDatePicker materialDatePicker1 = builder.build();
        mDatePickerBtnto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker1.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });
        mDatePickerBtnfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });


        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                mSelectedDateText1.setText("From : " + materialDatePicker.getHeaderText());


            }

        });
        materialDatePicker1.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                mSelectedDateText2.setText("To : " + materialDatePicker1.getHeaderText());


            }
        });




        imagetxt=findViewById(R.id.image_edit);
        EditText keytxt=findViewById(R.id.key_key);
        edit_btn=findViewById(R.id.edit);
        btn_return=findViewById(R.id.back_btn_edit);
        image_edit_btn=findViewById(R.id.edit_btn);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        NameTxt.setAdapter(myAdapter);
        String text_selected=NameTxt.getSelectedItem().toString();

        image_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check runtime permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

                        requestPermissions(permissions, PERMISSION_CODE);


                    } else {
                        //permission already granted
                        pickImageFromGallery();
                    }
                } else {
                    //system os is less then marshmallow
                    pickImageFromGallery();
                }

            }
        });
        String name="phone not set at first";
        String phone="phone not set at first";
        String price="phone not set at first";
        String from="phone not set at first";
        String to="phone not set at first";
        String location="phone not set at first";
        String image="image";
        String keeey="key";
        String mail_pub3="mail";
        String mail_verif3="verif";


        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            name=extras.getString("name");
            phone=extras.getString("phone");
            price=extras.getString("price");
            from=extras.getString("from");
            to=extras.getString("to");
            location=extras.getString("location");
            image=extras.getString("image");
            keeey=extras.getString("key");
            mail_pub3=extras.getString("mail_pub");
            mail_verif3=extras.getString("mail_verif");
        }

        phoneTxt.setText(phone);
        priceTxt.setText(price);
        mSelectedDateText1.setText(from);
        mSelectedDateText2.setText(to);
        locationTxt.setText(location);
        keytxt.setText(keeey);
        mail_pub_3.setText(mail_pub3);
        mail_verif_3.setText(mail_verif3);
        Glide.with(this).load(image)
                .fitCenter()
                .centerCrop()
                .apply(new RequestOptions().override(200, 200))
                .into(imagetxt);

        String finalKeeey = keeey;


        String finalImage = image;
        String finalImage1 = image;
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Upload uploaded= new Upload();
                uploaded.setName(NameTxt.getSelectedItem().toString());
                uploaded.setPhone(phoneTxt.getText().toString());
                uploaded.setPrice(priceTxt.getText().toString());
                uploaded.setFrom(mSelectedDateText1.getText().toString());
                uploaded.setTo(mSelectedDateText2.getText().toString());
                uploaded.setLocation1(locationTxt.getText().toString());
                uploaded.setKey(keytxt.getText().toString());
                uploaded.setmail(mail_pub_3.getText().toString());

                //image
                if(mImageUri!=null) {


                    StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                            + "." + getFileExtension(mImageUri));

                    final StorageTask<UploadTask.TaskSnapshot> upload_successful = fileReference.putFile(mImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!urlTask.isSuccessful()) ;
                                    Uri downloadUrl = urlTask.getResult();
                                    uploaded.setImageUrl(downloadUrl.toString());
                                    new interface3().updatedata(finalKeeey, uploaded);
                                    Toast.makeText(getApplicationContext()," Edited",Toast.LENGTH_SHORT).show();

                                    openActivity_interface3();


                                }
                            });


                }
                else{
                    uploaded.setImageUrl(finalImage1);
                    new interface3().updatedata(finalKeeey, uploaded);
                    Toast.makeText(getApplicationContext()," Edited",Toast.LENGTH_SHORT).show();



                    openActivity_interface3();


                }

            }


        });
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });




    }


    //pick Image

    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }
    //handle result of runtime permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    pickImageFromGallery();
                } else {
                    //permission was denied
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    //handle result of picked image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imagetxt.setImageURI(data.getData());
            mImageUri=data.getData();
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));}
    /////////////////////////////////////

    private void openActivity_interface3() {
        Intent ih =new Intent(this,interface3.class);
//          ih.putExtra("mail_fromhome",firebaseAuth.getCurrentUser().getEmail());
        String s="tesdt";
        startActivity(ih);
    }





}
