package net.ddns.techworm.smart___mechanic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class driver_main_page extends AppCompatActivity {
    TextView notify,locate;
    Spinner spin;
    int currentItem =0;
    ListView listView;
    ArrayList<MechanicUser> list;
    FirebaseFirestore db;
    Toolbar toolbar;
    SearchView searchView;
    private  static  final  int REQUEST_CALL = 1;
    MyMechanicsListAdapter myMechanicsListAdapter;
    Animation blink;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    String Useridd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main_page);
        notify = findViewById(R.id.notify);
        searchView = findViewById(R.id.search_mech);
        spin = (Spinner) findViewById(R.id.spinner);
        listView = (ListView) findViewById(R.id.mechanics_list_view);
        locate = findViewById(R.id.locate);
        db = FirebaseFirestore.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        Useridd = firebaseAuth.getCurrentUser().getUid();
        blink = AnimationUtils.loadAnimation(this,R.anim.blink);
//        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        toolbar = findViewById(R.id.toolbar_driver_main_page);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Driver Main page");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ///go to google map
        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),locate_user.class));
                finish();
            }
        });
        locate.setAnimation(blink);



        ///SEARCHING
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String t) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String t) {
                tafta(t);
                return true;
            }
        });
        ///END OF SEARCHING
        ////this will be used to send a notification to the mech whose profile is clicked,,,so onclick has to first work
//        EditText driver_name = findViewById(R.id.fullName);
//        EditText githaa = findViewById(R.id.timepicker);
//        DocumentReference documentReference = db.collection("driver dial").document();
//        Map <String,Object> comm = new HashMap<>();
//        comm.put("name",driver_name);
//        comm.put("time",githaa);
//        ////if this dosent work ,,well have to fetch the name and time from firebase
//        documentReference.set(comm).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(driver_main_page.this, "notification sent succesfully", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(driver_main_page.this, "Notification not sent,sth went wrong", Toast.LENGTH_SHORT).show();
//            }
//        });
//        /////end of sending notification
        //end of trial
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (currentItem == position){
                    return;
                }
                else if (position == 1){
                    db.collection("Mechanics")
                            .whereEqualTo("Location", "meru")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        list = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            // getting details of one user (document)----------------------------------------
                                            String phoneNumber = document.get("Phone number").toString();
                                            String location = document.get("Location").toString();
                                            String name = document.get("Name").toString();
                                            String image = document.get("image Uri").toString();

                                            // adding the user to our ArrayList--------------------------------------------
                                            list.add(new MechanicUser(phoneNumber, location, name,image));
//                                            search.setVisibility(View.VISIBLE);

                                            Log.d("hey","this are"+ document.getData());

                                        }
                                        // Here am setting the listView in MainActivity to use the list i was creating above and the adapter to show items in the listView
                                        listView.setAdapter(new MyMechanicsListAdapter(getApplicationContext(), list));
                                        searchView.setVisibility(View.VISIBLE);
                                    } else {
                                        Toast.makeText(driver_main_page.this, "error" + task.getException(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    //////end of trial
//                    trial worked

                }
                else if (position == 2){
                    db.collection("Mechanics")
                            .whereEqualTo("Location", "ndagani")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        list = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            // getting details of one user (document)----------------------------------------
                                            String phoneNumber = document.get("Phone number").toString();
                                            String location = document.get("Location").toString();
                                            String name = document.get("Name").toString();
                                            String image = document.get("image Uri").toString();

                                            // adding the user to our ArrayList--------------------------------------------
                                            list.add(new MechanicUser(phoneNumber, location, name,image));

                                            Log.d("hey","this are"+ document.getData());

                                        }
                                        // Here am setting the listView in MainActivity to use the list i was creating above and the adapter to show items in the listView
                                        listView.setAdapter(new MyMechanicsListAdapter(getApplicationContext(), list));
                                        searchView.setVisibility(View.VISIBLE);
                                    } else {
                                        Toast.makeText(driver_main_page.this, "errpr" + task.getException(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                else if (position == 3){
                    db.collection("Mechanics")
                            .whereEqualTo("Location", "mitunguu")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        list = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            // getting details of one user (document)----------------------------------------
                                            String phoneNumber = document.get("Phone number").toString();
                                            String location = document.get("Location").toString();
                                            String name = document.get("Name").toString();
                                            String image = document.get("image Uri").toString();

                                            // adding the user to our ArrayList--------------------------------------------
                                            list.add(new MechanicUser(phoneNumber, location, name,image));

                                            Log.d("hey","this are"+ document.getData());

                                        }
                                        // Here am setting the listView in MainActivity to use the list i was creating above and the adapter to show items in the listView
                                        listView.setAdapter(new MyMechanicsListAdapter(getApplicationContext(), list));
                                        searchView.setVisibility(View.VISIBLE);
                                    } else {
                                        Toast.makeText(driver_main_page.this, "error" + task.getException(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                else if (position == 4){
                    db.collection("Mechanics")
                            .whereEqualTo("Location", "mitheru")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        list = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            // getting details of one user (document)----------------------------------------
                                            String phoneNumber = document.get("Phone number").toString();
                                            String location = document.get("Location").toString();
                                            String name = document.get("Name").toString();
                                            String image = document.get("image Uri").toString();

                                            // adding the user to our ArrayList--------------------------------------------
                                            list.add(new MechanicUser(phoneNumber, location, name,image));

                                            Log.d("hey","this are"+ document.getData());

                                        }
                                        // Here am setting the listView in MainActivity to use the list i was creating above and the adapter to show items in the listView
                                        listView.setAdapter(new MyMechanicsListAdapter(getApplicationContext(), list));
                                        searchView.setVisibility(View.VISIBLE);
                                    } else {
                                        Toast.makeText(driver_main_page.this, "errpr" + task.getException(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                else if (position == 5){
                    db.collection("Mechanics")
                            .whereEqualTo("Location", "litmus")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        list = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            // getting details of one user (document)----------------------------------------
                                            String phoneNumber = document.get("Phone number").toString();
                                            String location = document.get("Location").toString();
                                            String name = document.get("Name").toString();
                                            String image = document.get("image Uri").toString();

                                            // adding the user to our ArrayList--------------------------------------------
                                            list.add(new MechanicUser(phoneNumber, location, name,image));

                                            Log.d("hey","this are"+ document.getData());

                                        }
                                        // Here am setting the listView in MainActivity to use the list i was creating above and the adapter to show items in the listView
                                        listView.setAdapter(new MyMechanicsListAdapter(getApplicationContext(), list));
                                        searchView.setVisibility(View.VISIBLE);
                                    } else {
                                        Toast.makeText(driver_main_page.this, "error" + task.getException(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                else if (position == 6){
                    db.collection("Mechanics")
                            .whereEqualTo("Location", "queen bee")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        list = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            // getting details of one user (document)----------------------------------------
                                            String phoneNumber = document.get("Phone number").toString();
                                            String location = document.get("Location").toString();
                                            String name = document.get("Name").toString();
                                            String image = document.get("image Uri").toString();

                                            // adding the user to our ArrayList--------------------------------------------
                                            list.add(new MechanicUser(phoneNumber, location, name,image));

                                            Log.d("hey","this are"+ document.getData());

                                        }
                                        // Here am setting the listView in MainActivity to use the list i was creating above and the adapter to show items in the listView
                                        listView.setAdapter(new MyMechanicsListAdapter(getApplicationContext(), list));
                                        searchView.setVisibility(View.VISIBLE);
                                    } else {
                                        Toast.makeText(driver_main_page.this, "error" + task.getException(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void tafta(String feedback) {
      ArrayList<MechanicUser> feedme = new ArrayList<>();
      for (MechanicUser object : list){
          if (object.getName().toLowerCase().contains(feedback.toLowerCase()) || object.getLocation().contains(feedback.toLowerCase())){
              feedme.add(object);
          }
      }
      myMechanicsListAdapter= new MyMechanicsListAdapter(driver_main_page.this,feedme);
      listView.setAdapter(myMechanicsListAdapter);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    ///////ADAPTER CLASS
    class MyMechanicsListAdapter extends ArrayAdapter<MechanicUser> {

        List<MechanicUser> mechanicUsers;
        public MyMechanicsListAdapter myMechanicsListAdapter;

        MyMechanicsListAdapter(@NonNull Context context, @NonNull List<MechanicUser> objects) {
            super(context, R.layout.single_mechanic_details_template, objects);
            this.mechanicUsers = objects;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.single_mechanic_details_template, parent, false);

            final TextView mName = view.findViewById(R.id.mechanic_name);
            TextView mLocation = view.findViewById(R.id.mechanic_location);
            final TextView mPhone = view.findViewById(R.id.mechanic_phone_number);
            ImageView mech_image = view.findViewById(R.id.smart_mech_image);

            mName.setText(mechanicUsers.get(position).getName());
            mLocation.setText(mechanicUsers.get(position).getLocation());
            mPhone.setText(mechanicUsers.get(position).getPhoneNumber());
            Picasso.get().load(mechanicUsers.get(position).getImage()).into(mech_image);

            ///trials
            //this piece of code is golden
            final Button mybtn = view.findViewById(R.id.callbtn);
            mybtn.setFocusable(false);
            //calling mech
            mybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String nangos = (String) mPhone.getText();
//                    Toast.makeText(driver_main_page.this, "oyaaaa" + nangos, Toast.LENGTH_SHORT).show();
                    if(ContextCompat.checkSelfPermission(driver_main_page.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(driver_main_page.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
                    }
                    else {
                        /////////////////ADDING MECHPICKED FIELD
                        final String mech_picked = mName.getText().toString().trim();
                        Map<String , Object> dataa = new HashMap<>();
                        dataa.put("driver picked",mech_picked);
                        db.collection("Drivers").document(firebaseAuth.getCurrentUser().getUid())
                                .set(dataa, SetOptions.merge());
                        /////END OF ADDING FIELD
                        String Dial ="tel:" + nangos;
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(Dial)));
                    }
                }
            });
            //end of call mech

            //end of working part
            return view;
        }
    }
    /////////////////////////end of adapter class


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.driver_logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profilito){
//            firebaseAuth.signOut();
            startActivity(new Intent(getApplicationContext(),driver_Profile.class));
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        return true;
//        switch (item.getItemId()){
//            case R.id.profiler:
//                startActivity(new Intent(getApplicationContext(),driver_Profile.class));
//                finish();
//                break;
//            case R.id.profilito:
//                startActivity(new Intent(getApplicationContext(),driver_main.class));
//                finish();
//
//        }
//        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makecall();
            }
            else {
                Toast.makeText(driver_main_page.this,"unable to complete this call", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void makecall() {

    }
}
