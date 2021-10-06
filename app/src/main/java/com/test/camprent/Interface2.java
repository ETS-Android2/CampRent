package com.test.camprent;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Interface2 extends AppCompatActivity {


    // Pic From Galery
    ImageView mImageView;
    Button mChooseBtn;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    FirebaseAuth fAuth;
    TextView mail;
    private Button backhomeBtn;








    private Button mDatePickerBtnfrom; // button from
    private Button mDatePickerBtnto; // button to
    private TextView mSelectedDateText1;
    private TextView mSelectedDateText2;
    Spinner NameEquipment;

    EditText Price,Phone;
    EditText location1;
    Button publishbtn;


    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    private ProgressBar mProgressBar;


// instantiation de la base

    FirebaseDatabase rootNode;
    DatabaseReference reference;


    //Choose Image

    private ProgressDialog progressDialog;
    private Uri mImageUri;


    //    String ID= fAuth.getUid();








    // pour le calendrier
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final String TAG = "Interface2";

    // for location
    Button btLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface2);

        // Views
        mImageView = findViewById(R.id.picture);
        mChooseBtn = findViewById(R.id.choosebtn);
        mProgressBar=findViewById(R.id.progressBar2);
        mail=findViewById(R.id.token);
        //retour au home
        backhomeBtn = findViewById(R.id.backhome);
        backhomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.layout_interface2,new HomeFragment());
                ft1.commit();
            }
        });


        mStorageRef=FirebaseStorage.getInstance().getReference("Uploads");
        mDatabaseRef=FirebaseDatabase.getInstance().getReference("Uploads");
        fAuth=FirebaseAuth.getInstance();

        String mail_S=fAuth.getCurrentUser().getEmail();


        mail.setText(mail_S);
        String test="test";

        // handle button click
        mChooseBtn.setOnClickListener(new View.OnClickListener() {
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
        publishbtn=findViewById(R.id.publish);


        NameEquipment=findViewById(R.id.name_equipment);
        mSelectedDateText1 = findViewById(R.id.from_date);
        Price = findViewById(R.id.Price);
        btLocation = findViewById(R.id.bt_location);
        location1=findViewById(R.id.textView1);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        btLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check permission
                if (ActivityCompat.checkSelfPermission(Interface2.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //When permission granted
                    getLocation();

                } else {
                    // when permission denied
                    ActivityCompat.requestPermissions(Interface2.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

                }
            }
        });


        //Spinner
        Spinner mySpinner = (Spinner) findViewById(R.id.name_equipment);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Interface2.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        String text=mySpinner.getSelectedItem().toString();




        Phone = findViewById(R.id.phone_number);
        //debut date
        mSelectedDateText2 = findViewById(R.id.to_date);
        mDatePickerBtnfrom = findViewById(R.id.from_date);
        mDatePickerBtnto = findViewById(R.id.to_date);
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
        //fin date

        publishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
                // OpenActivity_search();
            }
        });

    }

    private void OpenActivity_search() {
        Intent inten =new Intent(this,interface3.class);
        inten.putExtra("mail_fromhome",mail.getText().toString().trim());

        startActivity(inten);
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
            mImageView.setImageURI(data.getData());
            mImageUri=data.getData();
        }
    }
    //firebase
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));}
    private  void uploadFile(){
        if (mImageUri!=null  ){
            Log.i(TAG, "uploadFile: etape1");

            StorageReference fileReference= mStorageRef.child(System.currentTimeMillis()
                    + "."+getFileExtension(mImageUri));
            Log.i(TAG, "uploadFile: etape2");
            final StorageTask<UploadTask.TaskSnapshot> upload_successful = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(Interface2.this, "Upload successful", Toast.LENGTH_LONG).show();
                            OpenActivity_search();
                            /*Upload upload = new Upload( Price.getText().toString().trim(), location1.getText().toString().trim(), location2.getText().toString().trim(), location3.getText().toString().trim(), location4.getText().toString().trim(), location5.getText().toString().trim(), Phone.getText().toString().trim(), mSelectedDateText1.getText().toString().trim(), mSelectedDateText2.getText().toString().trim(), taskSnapshot.getUploadSessionUri().toString());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload)
                            ;

                             */
                            Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl=urlTask.getResult();
                            String uploadId = mDatabaseRef.push().getKey();
                            Upload upload = new Upload(downloadUrl.toString(),NameEquipment.getSelectedItem().toString(),mSelectedDateText1.getText().toString().trim(), mSelectedDateText2.getText().toString().trim(),Price.getText().toString().trim(),Phone.getText().toString().trim(), location1.getText().toString().trim(),uploadId,mail.getText().toString().trim());

                            mDatabaseRef.child(uploadId).setValue(upload);



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Interface2.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });


        }
        else{
            Toast.makeText(this,"No file selected",Toast.LENGTH_SHORT).show();
        }

    }
    //location azza
    private void getLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //Initialize location
                Location location = task.getResult();
                if (location != null) {
                    try {
                        //Initialize geoCoder
                        Geocoder geocoder = new Geocoder(Interface2.this, Locale.getDefault());
                        //Initialize address list
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );

                        location1.setText(Html.fromHtml(
                             "<font color = #FFFFFF></font>" + addresses.get(0).getAddressLine(0)
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}