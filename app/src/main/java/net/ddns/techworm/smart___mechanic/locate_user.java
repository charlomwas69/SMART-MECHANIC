package net.ddns.techworm.smart___mechanic;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterActivity;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class locate_user extends FragmentActivity implements OnMapReadyCallback {
    private static final int PERMISSION_CODE = 101;
//    LocationManager locationManager;
//    LocationListener locationListener;
    Location currentLocation;
    LatLng userLatlng;
    private GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    public static final String TAG = "TAG";
//    public static final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_user);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_CODE);
            return;
        }
        final Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;
                    Toast.makeText(locate_user.this, "YOU ARE HERE" + currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_LONG).show();
                    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(locate_user.this);

                }
                else {
                    Toast.makeText(locate_user.this, "ERROR" + task.getException(), Toast.LENGTH_SHORT).show();
//                    Log.d("CANT GET UR LOCATION",task.getException());
                    Log.d(TAG, "ERROR"+ task.getException());
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        userLatlng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

//                mMap.clear(); ///clear old location
                MarkerOptions markerOptions = new MarkerOptions().position(userLatlng)
                        .title("You are here");
                mMap.animateCamera(CameraUpdateFactory.newLatLng(userLatlng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatlng,5));
                mMap.addMarker(markerOptions);
    }
//
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE:
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
//                break;
                else {
                    Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),driver_main_page.class));
        finish();
    }
}