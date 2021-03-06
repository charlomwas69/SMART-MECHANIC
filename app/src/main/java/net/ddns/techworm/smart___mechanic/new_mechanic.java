package net.ddns.techworm.smart___mechanic;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;


public class new_mechanic extends AppCompatActivity {
    public static final String TAG = "TAG";
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;
    String verificationId;
    PhoneAuthProvider.ForceResendingToken token;
    TextView state,resend;
    EditText otp,number;
    ProgressBar progressbar;
    Button nxt;
    Boolean verificationInProgress = false;
    PhoneAuthCredential credential;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mechanic);
        progressbar = findViewById(R.id.progressBar);
        otp = findViewById(R.id.codeEnter);
        number = findViewById(R.id.phone);
        nxt = findViewById(R.id.nextBtn);
        state =findViewById(R.id.state);
        resend = findViewById(R.id.resendOtpBtn);
        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mechanic");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo:: resend OTP
            }
        });

        ////CHECKING NET
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() == true){

            View parent_info = findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(parent_info,"Internet connectivity exists",Snackbar.LENGTH_LONG);
            snackbar.setDuration(5000);
            snackbar.show();
        }else {
            View parent_info = findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(parent_info,"You're offline,Check your Internet connection",Snackbar.LENGTH_LONG);
            snackbar.setDuration(5000);
            snackbar.show();
        }

        ///CHECKING NET


        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!number.getText().toString().isEmpty() && number.getText().toString().length() == 13) {
                    if(!verificationInProgress){
                        nxt.setEnabled(false);
                        String phoneNum = number.getText().toString();
//                        Log.d(TAG,"Number is " + phoneNum);
                        state.setVisibility(View.VISIBLE);
                        progressbar.setVisibility(View.VISIBLE);
                        requestOTP(phoneNum);
                    }else {
                        //getting OTP from user
                        String userOTP = otp.getText().toString();
                        ///checking if OTP is entered
                        if (userOTP.isEmpty()){
                            otp.setError("OTP is invalid");
                        }
                        ///if otp field is not empty
                        else {
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,userOTP);
                            verifyAuth(credential);
                        }
                        nxt.setEnabled(false);
                        otp.setVisibility(View.GONE);
                        progressbar.setVisibility(View.VISIBLE);
                        state.setText("Logging in...");
                        state.setVisibility(View.VISIBLE);


                    }

                }else {
                    number.setError("Valid Phone Required");
                }
            }
        });
    }

    private void requestOTP(String phoneNum) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNum, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                progressbar.setVisibility(View.GONE);
                state.setVisibility(View.GONE);
                otp.setVisibility(View.VISIBLE);
                verificationId = s;
                token = forceResendingToken;
                nxt.setText("VERIFY");
                nxt.setEnabled(true);
                verificationInProgress = true;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Toast.makeText(new_mechanic.this, "OTP has expired  *CHECK YOUR NETWORK PLEASE*", Toast.LENGTH_SHORT).show();
                resend.setVisibility(View.VISIBLE);
                state.setVisibility(View.GONE);
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                verifyAuth(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(new_mechanic.this, "Phone number not verified"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
                state.setVisibility(View.INVISIBLE);
                nxt.setEnabled(true);
            }
        });
    }                    ///////END OF REQUEST OTP

            //////////VERIFY AUTH
    private void verifyAuth(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(new_mechanic.this, "Phone Number Verified", Toast.LENGTH_SHORT).show();
                    ////firest check if user exists in the db
                    checkUserProfile();

                }else {
                    progressbar.setVisibility(View.GONE);
                    state.setVisibility(View.GONE);
                    Toast.makeText(new_mechanic.this, "Phone Number Not Verified *Invalid OTP*", Toast.LENGTH_LONG).show();
                    otp.setVisibility(View.VISIBLE);
                    nxt.setText("REENTER OTP");
                    nxt.setEnabled(true);

//                    Snackbar snackbar = Snackbar.make(findViewById(R.id.mech),"Wrong OPT/Network related problem",Snackbar.LENGTH_LONG);
//                    state.setVisibility(View.VISIBLE);

                }
            }
        });
    }            ///END OF VERIFY AUTH

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null){
            progressbar.setVisibility(View.VISIBLE);
            state.setText("CHECKING .....");
            state.setVisibility(View.VISIBLE);
            checkUserProfile();
        }
    }


    //////////////////////// CHECKING IF USER HAS ALREADY RECEIVED THE OTP,,IF SO THE USER WILL GO DIRECTLY TO MAIN PAGE
    private void checkUserProfile() {
        DocumentReference docref = fstore.collection("Mechanics").document(firebaseAuth.getCurrentUser().getUid());
        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    startActivity(new Intent(getApplicationContext(),mech_main_page.class));
                    finish();
//                    startActivity(new Intent(getApplicationContext(),mechanic_registration.class));
//                    finish();

                }else {
                    startActivity(new Intent(getApplicationContext(),mechanic_registration.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(new_mechanic.this, "Profile does not exist", Toast.LENGTH_SHORT).show();
                state.setVisibility(View.GONE);
                progressbar.setVisibility(View.GONE);
            }
        });
    }

}