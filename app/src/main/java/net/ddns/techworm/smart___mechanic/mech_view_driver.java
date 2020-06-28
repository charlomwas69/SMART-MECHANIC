package net.ddns.techworm.smart___mechanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class mech_view_driver extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Driver_User> drivers;
    FirebaseFirestore fstore;
    D_adapter d_adapter;
    SearchView searchView;
    StorageReference storageReference;
    FirebaseAuth fauth;
    String imageuri;
    private  static  final  int REQUEST_CALL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mech_view_driver);

        //story na adapter
        recyclerView =(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        drivers = new ArrayList<>();
        ///story na adapter
        fstore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        fauth = FirebaseAuth.getInstance();


        searchView = findViewById(R.id.sach);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search(s);
                return true;
            }
        });

        //getting drivers data from firebase
        //////////////GETTING NAME FROM MECH PROFILE
        Intent myintent = getIntent();
        String jinaaa = myintent.getStringExtra("myname");
//        Toast.makeText(this, "wewe ni " + jinaaa , Toast.LENGTH_LONG).show();
//        SharedPreferences sharedPreferences = getSharedPreferences("mykey",MODE_PRIVATE);
//        String jina = sharedPreferences.getString("jina","");
//        Log.d("check","wewe ni "+ jina);
//        Toast.makeText(this, "Youre" + jina, Toast.LENGTH_LONG).show();
        //////////////END OF GETTING NAME FROM MECH PROFILE

        fstore.collection("Drivers")
                .whereEqualTo("driver picked",jinaaa)
                /////LOOK FOR A WAY TO SORT
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                ///////IMAGE TRIALS
                                final StorageReference set_Dp = storageReference.child("mechanics/"+fauth.getCurrentUser().getUid()+"profile_pic");
                                set_Dp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).into(profile_pic);
                                        set_Dp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                imageuri = uri.toString();
//                                                Toast.makeText(mech_view_driver.this, "uri is" + "" + imageuri, Toast.LENGTH_SHORT).show();
//                                                Log.d("charlo","this are"+ imageuri);
//                                                Toast.makeText(mech_view_driver.this, "ALMOST THERE", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                                //////END OF IMAGE TRIALS
                                String dname = document.get("Name").toString();
                                String dphone = document.get("Phone number").toString();
                                String dtime = document.get("Registration Time").toString();
                                String pichaa = document.get("image Uri").toString();


                                Log.d("hey","this are"+ document.getData());
//                                drivers.add(new Driver_User(dname,dphone,dtime));
                                drivers.add(new Driver_User(dname,dphone,dtime,pichaa));

                            }

                            d_adapter =  new D_adapter(mech_view_driver.this,drivers);
                            recyclerView.setAdapter(d_adapter);
//                            recyclerView.setAdapter(new D_adapter(getApplicationContext(),drivers));
                        }
                        else {
                            Toast.makeText(mech_view_driver.this, "error" + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        //end of getting data

    }

    private void search(String str) {
        ArrayList<Driver_User> mylist = new ArrayList<>();
        for (Driver_User object : drivers){
            if (object.getName().toLowerCase().contains(str.toLowerCase())){
                mylist.add(object);
            }
        }
        D_adapter d_adapter = new D_adapter(mech_view_driver.this,mylist);
        recyclerView.setAdapter(d_adapter);

    }

    ///////ADAPTER
public class D_adapter extends RecyclerView.Adapter<D_adapter.ViewHolder>{

    private ArrayList<Driver_User> data;
    private LayoutInflater layoutInflater;
//    private mech_view_driver mech_view_drivers;

    D_adapter(Context context , ArrayList<Driver_User> data ){
        this.layoutInflater = LayoutInflater.from(context);
            this.data = data;
        }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(mech_view_drivers.getBaseContext());
        View view = layoutInflater.inflate(R.layout.single_driver_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //binding textview with data received
        holder.driver_name.setText(data.get(position).getName());
        holder.driver_time.setText(data.get(position).getDatee());
        holder.driver_phone_Number.setText(data.get(position).getPhone());
        Picasso.get().load(data.get(position).getPicha()).into(holder.dp_deree);



        //u can set image for each card as well
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView driver_name,driver_time,driver_phone_Number;
        ImageView dp_deree;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                driver_name = itemView.findViewById(R.id.driver_name);
                driver_time = itemView.findViewById(R.id.driver_time);
                driver_phone_Number = itemView.findViewById(R.id.driver_phone_number);
                dp_deree = itemView.findViewById(R.id.driver_dp);
                final Button call_driver = itemView.findViewById(R.id.call_driver);
                call_driver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nangos = (String) driver_phone_Number.getText();
//                    Toast.makeText(driver_main_page.this, "oyaaaa" + nangos, Toast.LENGTH_SHORT).show();
                        if(ContextCompat.checkSelfPermission(mech_view_driver.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(mech_view_driver.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
                        }
                        else {
                            String Dial ="tel:" + nangos;
                            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(Dial)));
                        }
                    }
                });
            }
        }

}
///////END OF ADAPTER

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),mech_main_page.class));
        finish();
    }
}
