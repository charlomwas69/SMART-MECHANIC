package net.ddns.techworm.smart___mechanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class driver_registration extends AppCompatActivity {
    Button reg;
    ProgressBar prog;
    EditText name,phone,identity;
    FirebaseFirestore fstore;
    FirebaseAuth fauth;
    EditText timee;
    private String format = "";
    String Useridd;
    String def_image;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);
        reg = findViewById(R.id.LoginBtn);
        prog = findViewById(R.id.progressBar_mech);
        name = findViewById(R.id.fullName);
        phone = findViewById(R.id.phone);
        identity = findViewById(R.id.identi);
        fstore = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();
        Useridd = fauth.getCurrentUser().getUid();

        def_image = "https://firebasestorage.googleapis.com/v0/b/the-smart-mechanic.appspot.com/o/Default%2Fsmart_mechanic_logo.jpg?alt=media&token=9b573c20-3ae8-4cf9-8e11-428af4a61803";

        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Driver");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //time
        timee =findViewById(R.id.timepicker);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        //setting time
        timee.setText(new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format));
        //end of time

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String num = phone.getText().toString().trim();
                final String nam = name.getText().toString().trim();
                final String natl = identity.getText().toString().trim();
                String now = timee.getText().toString().trim();
                if(TextUtils.isEmpty(nam)){
                    name.setError("Name is required");
                    return;
                }
                if(nam.length() < 3){
                    name.setError("Name should be > 3 xters");
                    return;
                }
                ///number
                if(TextUtils.isEmpty(num)){
                    phone.setError("Phone Number is required");
                    return;
                }
                if(num.length() < 9){
                    phone.setError("Phone number shld be >9");
                    return;
                }
                //id
                if(TextUtils.isEmpty(natl)){
                    identity.setError("ID is required");
                    return;
                }
                if(natl.length() < 8){
                    identity.setError("Name should be > 7 xters");
                    return;
                }

                else {
                    prog.setVisibility(View.VISIBLE);

                    DocumentReference documentReference = fstore.collection("Drivers").document(Useridd);
                    Map<String , Object> driver = new HashMap<>();
                    driver.put("Name" ,nam);
                    driver.put("Phone number" , num);
                    driver.put("ID number",natl);
                    driver.put("Registration Time",now);
                    driver.put("image Uri",def_image);
                    documentReference.set(driver).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(driver_registration.this, "Profile created successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(),driver_main_page.class);
                            startActivity(intent);
                            prog.setVisibility(View.GONE);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(driver_registration.this, "data not inserted" +e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });
    }
}
