package net.ddns.techworm.smart___mechanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class mechanic_registration extends AppCompatActivity {
    Button reg;
    ProgressBar prog;
    EditText name,phone,identity;
    AutoCompleteTextView location;
    FirebaseFirestore fstore;
    FirebaseAuth fauth;
    String Userid;
    String[] area = {"meru","ndagani","mitunguu","mitheru","litmus","queen bee"};
    Toolbar toolbar;
    String def_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_registration);

        reg = findViewById(R.id.LoginBtn);
        prog = findViewById(R.id.progressBar_mech);
        name = findViewById(R.id.fullName);
        phone = findViewById(R.id.phone);
        identity = findViewById(R.id.identi);

        def_image = "https://firebasestorage.googleapis.com/v0/b/the-smart-mechanic.appspot.com/o/Default%2Fsmart_mechanic_logo.jpg?alt=media&token=9b573c20-3ae8-4cf9-8e11-428af4a61803";

        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mechanic");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        location = findViewById(R.id.location);
        //autocomplete
        location = (AutoCompleteTextView)findViewById(R.id.location);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,area);

        location.setAdapter(arrayAdapter);
        location.setThreshold(1);

        ////firestore
        fstore = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();
        Userid = fauth.getCurrentUser().getUid();


        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String num = phone.getText().toString().trim();
                String loc = location.getText().toString().trim();  //used
                final String nam = name.getText().toString().trim(); //used
                final String natl = identity.getText().toString().trim();  //useed
                ////name
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
                //location
                if(TextUtils.isEmpty(loc)){
                    location.setError("Location is required");
                    return;
                }

                else {
                    prog.setVisibility(View.VISIBLE);

                    DocumentReference documentReference = fstore.collection("Mechanics").document(Userid);
                    Map<String , Object> user = new HashMap<>();
                    user.put("Name" ,nam);
                    user.put("Phone number" , num);
                    user.put("ID number",natl);
                    user.put("Location",loc);
                    user.put("image Uri",def_image);
                    //adding user to db
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(mechanic_registration.this, "inserted successful", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(),mech_main_page.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mechanic_registration.this, "data not inserted" +e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
        }
}
