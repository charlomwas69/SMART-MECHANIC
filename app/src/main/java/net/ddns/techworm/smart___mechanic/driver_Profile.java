package net.ddns.techworm.smart___mechanic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class driver_Profile extends AppCompatActivity {

    private static final int CAM_PERM = 100 ;
    private static final int CAM_REQ_CODE = 101;
    public static final int GALLERY_RE_CODE = 103;
    String currentPhotoPath;
    ImageView driver_profile_pic;
    Button update_dp;
    FirebaseFirestore firestore;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    TextView driver_name,driver_id,driver_phone;
    Toolbar toolbar;
    ProgressBar prog_pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver__profile);

        driver_id = findViewById(R.id.driver_id_number);
        driver_name =  findViewById(R.id.driver_name);
        driver_phone = findViewById(R.id.driver_phone_number);

        prog_pic = findViewById(R.id.prog_pic);

        toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        driver_profile_pic = findViewById(R.id.my_driver_dp);
        update_dp = findViewById(R.id.dp_change);
//        driver_profile_pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                askCameraperm();
//            }
//        });

        ////SETTING DP FROM FIREBASE STORAGE
        StorageReference set_Dp = storageReference.child("drivers/"+firebaseAuth.getCurrentUser().getUid()+"profile_pic");
//        proggg.setVisibility(View.VISIBLE);
        set_Dp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(driver_profile_pic);
                prog_pic.setVisibility(View.GONE);
//                Toast.makeText(mech_main_page.this, "ALMOST THERE CHARLO", Toast.LENGTH_SHORT).show();
//                proggg.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(driver_Profile.this, "CANT LOAD IMAGE" + e, Toast.LENGTH_LONG).show();
//                prog_pic.setVisibility(View.GONE);
            }
        });
        ///END OF SETTING DP

        update_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                askCameraperm();
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_RE_CODE);
            }
        });
        //////////////FILLING PROFILE
        final DocumentReference documentReference = firestore.collection("Drivers").document(firebaseAuth.getCurrentUser().getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.getString("Name");
                String phone_number = documentSnapshot.getString("Phone number");
                String id_number = documentSnapshot.getString("ID number");

                driver_name.setText(name);
                driver_phone.setText(phone_number);
                driver_id.setText(id_number);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(driver_Profile.this, "Operation Unsuccessfull" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //////////////END OF FILLING PROFILE
    }
    /////////////// DP TINGS
    private void askCameraperm() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA} , CAM_PERM);
        }else {
            dispatchTakePictureIntent();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAM_PERM){
            // IF THESE CONDITIONS ARE TRUE THEN CAMERA WILL OPEN
            if (grantResults.length > 0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else {
                Toast.makeText(this, "Cam permission is required ", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAM_REQ_CODE){
            if (resultCode == Activity.RESULT_OK){
                File f = new File(currentPhotoPath);
                driver_profile_pic.setImageURI(Uri.fromFile(f));
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
            }

        }
        if (requestCode == GALLERY_RE_CODE){
            if (resultCode == Activity.RESULT_OK){
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp +"."+getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " +  imageFileName);
//                profile_pic.setImageURI(contentUri);
                loadImageToFirebase(contentUri);
            }

        }
    }

    private void loadImageToFirebase(final Uri contentUri) {
//        final StorageReference fileRef = storageReference.child("drivers/"+firebaseAuth.getCurrentUser().getUid()+"profile_pic").child(contentUri.getLastPathSegment());
        final  StorageReference fileRef = storageReference.child("drivers/"+firebaseAuth.getCurrentUser().getUid()+"profile_pic");
        fileRef.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(mech_main_page.this, "DP uploaded successfully", Toast.LENGTH_LONG).show();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(driver_profile_pic);
//                        Log.d("upload","uploaded succ");
//                        Toast.makeText(driver_Profile.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                        String pichauri = uri.toString();
                        ////ADDING IMAGE URI FIELD
                        Map<String , Object> oyaa = new HashMap<>();
                        oyaa.put("image Uri",pichauri);
                        firestore.collection("Drivers").document(firebaseAuth.getCurrentUser().getUid())
                                .set(oyaa, SetOptions.merge());
                        ////END OF ADDING IMAGE URI FIELD
                        prog_pic.setVisibility(View.GONE);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(driver_Profile.this, "DP not uploaded", Toast.LENGTH_LONG).show();
                prog_pic.setVisibility(View.GONE);
            }
        });

    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAM_REQ_CODE);
            }
        }
    }
    ///////////DP TINGS

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_deree,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sign_out){
            firebaseAuth.signOut();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),driver_main_page.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
