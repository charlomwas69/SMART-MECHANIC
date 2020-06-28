package net.ddns.techworm.smart___mechanic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class mech_main_page extends AppCompatActivity {
    public static final String TAG = "TAG";
    //cam
    public static final int CAM_PERM = 100;
    public static final int CAM_REQ_CODE = 101;
    public static final int GALLERY_RE_CODE = 103;
    String currentPhotoPath;
    //cam
    FirebaseFirestore fStore;
    FirebaseAuth fauth;
    TextView fullName,Id,phone,locat,dri_time,dri_name;
    String mName,mId,mPhone,mLocation;
    Toolbar toolbar;
    Button btn,change_dp;
    int notification_id = 1;
    ImageView profile_pic;
    StorageReference storageReference;
    ProgressBar proggg;

    int m = (int) ((new Date().getTime()/1000L)% Integer.MAX_VALUE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mech_main_page);
        fStore = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
//        btn = findViewById(R.id.btn);
        change_dp = findViewById(R.id.change_dp);
        profile_pic = findViewById(R.id.profile_picture);
//        final FirebaseFirestore db = FirebaseFirestore.getInstance();
//        final CollectionReference user = db.collection("users");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mechanic Profile");


        fullName = findViewById(R.id.profileFullName);
        Id = findViewById(R.id.profileId);
        phone = findViewById(R.id.profilePhone);
        locat = findViewById(R.id.profileLocation);

        proggg = findViewById(R.id.progressBar_pic);

        ////SETTING DP FROM FIREBASE STORAGE
        StorageReference set_Dp = storageReference.child("mechanics/"+fauth.getCurrentUser().getUid()+"profile_pic");
//        proggg.setVisibility(View.VISIBLE);
        set_Dp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profile_pic);
//                Toast.makeText(mech_main_page.this, "YOUR URI IS" + uri, Toast.LENGTH_SHORT).show();
                proggg.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(mech_main_page.this, "CANT LOAD IMAGE " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                proggg.setVisibility(View.GONE);
            }
        });
        ///END OF SETTING DP


        //OPEN CAM
//        profile_pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                askCameraperm();
//            }
//        });

        //END OF OPEN CAM

        //////CHANGE DP
        change_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_RE_CODE);
            }
        });
        //////CHANGE DP
        /////////////////GETTING MECHANIC DATA
        final DocumentReference docRef =fStore.collection("Mechanics").document(fauth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    //fetching data
                    mName = documentSnapshot.getString("Name");
//                    mPhone = firebaseAuth.getCurrentUser().getPhoneNumber();
                    mPhone = documentSnapshot.getString("Phone number");
                    mId = documentSnapshot.getString("ID number");
                    mLocation = documentSnapshot.getString("Location");
                    //filling the fields
                    fullName.setText(mName);
                    phone.setText(mPhone);
                    Id.setText(mId);
                    locat.setText(mLocation);
                    //notification
                    String mech_name  = (String) fullName.getText();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                    builder.setSmallIcon(R.drawable.smart_mechanic_logo)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.charlo_study))
                            .setContentTitle("Hello" + " " + mech_name )
//                                                .setContentText(d_name +" "+ "checked ur profile at"+" " + d_time)
                            .setAutoCancel(true)
                            .setContentText("You have new Notification/s");

                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                    inboxStyle.setBigContentTitle("The following drivers checked out your profile");
                    inboxStyle.addLine("Press below to see drivers who viewed ur profile");
                    ///adding onclick
                    String jina = fullName.getText().toString();
                    Intent intent = new Intent(getApplicationContext(),mech_view_driver.class);
                    intent.putExtra("myname",jina);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);
                    builder.addAction(android.R.drawable.menu_frame,"View Drivers",pendingIntent );
//        Intent intent = new Intent(Intent.ACTION_SHUTDOWN);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
//        builder.addAction(android.R.drawable.menu_frame,"View Drivers",pendingIntent);
                    //end of onclick

                    builder.setStyle(inboxStyle);
                    Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    builder.setSound(path);

                    NotificationManager notificationManager =(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        String channelId = "YOUR_CHANNEL_ID";
                        NotificationChannel channel = new NotificationChannel(channelId,
                                "Channel human readable title"
                                ,NotificationManager.IMPORTANCE_DEFAULT);
                        notificationManager .createNotificationChannel(channel);
                        builder.setChannelId(channelId);
                    }
                    assert notificationManager != null;
                    notificationManager.notify(notification_id,builder.build());
                    ///////////////////////////////////////END OF NOTIFICATION

                }else {
//                    Log.d(TAG, "Retrieving Data: Profile Data Not Found ");
                    Toast.makeText(mech_main_page.this, "Data not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ///////NOTIFICATION BUTTON
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(),mech_view_driver.class));
//                finish();
//                ///////CARRYING MECH'S NAME
////                String jina = fullName.getText().toString();
////                Intent intenta = new Intent(getApplicationContext(),mech_view_driver.class);
////                intenta.putExtra("myname",jina);
////                startActivity(intenta);
//                //////END OF CARRYING MECH'S NAME
//            }
//        });
    }
    /////CAMERA STUFF
    ////////////FUNC TO REQUEST PERMISSION TO USE CAMERA
    private void askCameraperm(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA} , CAM_PERM);
        }else {
            dispatchTakePictureIntent();
        }
    }
    ////OVERDIDING ILE METHOD YA KUREQUEST RUHUSA YA CAMERA,,JUST WRITE PERMISSSION....

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
                profile_pic.setImageURI(Uri.fromFile(f));
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
        proggg.setVisibility(View.VISIBLE);
//        final StorageReference fileRef = storageReference.child("mechanics/"+fauth.getCurrentUser().getUid()+"profile_pic").child(contentUri.getLastPathSegment());
        final  StorageReference fileRef = storageReference.child("mechanics/"+fauth.getCurrentUser().getUid()+"profile_pic");
        fileRef.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(mech_main_page.this, "DP uploaded successfully", Toast.LENGTH_LONG).show();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profile_pic);
                        Log.d("upload","uploaded succ");
                        String pichauri = uri.toString();
                        ////ADDING IMAGE URI FIELD
                        Map<String , Object> oyaa = new HashMap<>();
                        oyaa.put("image Uri",pichauri);
                        fStore.collection("Mechanics").document(fauth.getCurrentUser().getUid())
                                .set(oyaa, SetOptions.merge());
                        ////END OF ADDING IMAGE URI FIELD
                        proggg.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mech_main_page.this, "DP not uploaded", Toast.LENGTH_LONG).show();
                proggg.setVisibility(View.INVISIBLE);
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
    ///THIS IS USED FOR DISPLAYING THE IMAGE IN THE IMAGEVIEW WE HAVE
    ////END OF CAMERA
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        ///if they are many use switch case
//        switch (item.getItemId()){
//            case  R.id.///id of item;
//                //what to do
//                return true;
//            case  R.id.///id of item;
//                //what to do
//                return true;
//        }
        return true;
    }
}
