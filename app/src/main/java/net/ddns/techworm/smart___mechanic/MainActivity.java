package net.ddns.techworm.smart___mechanic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_INVITE =1 ;
    Spinner spinner;
    int currentItem =0;
//    Button lotto;
//    Button share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (Spinner) findViewById(R.id.dropdown);
//        share = findViewById(R.id.share);
//        lotto = findViewById(R.id.lotto);
//        lotto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                startActivity(new Intent(getApplicationContext(),lottie_start.class));
////                finish();
//                onInviteClicked();
//            }
//        });
//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ApplicationInfo applicationInfo = getApplicationContext().getApplicationInfo();
//                String apppath = applicationInfo.sourceDir;
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("application/vnd.android.package-archive");
//                intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(new File(apppath)));
//                startActivity(Intent.createChooser(intent,"Shre Thro"));
//            }
//        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (currentItem == position){
                    return;
                }else if (position == 1){
                    Intent intent = new Intent(getApplicationContext(), new_driver.class);
//                    Intent intent = new Intent(getApplicationContext(), driver_registration.class);
                    startActivity(intent);
                }else {
                    /////CHANGE TO NEW_MECHANIC
                    Intent intent = new Intent(getApplicationContext(), new_mechanic.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

//    private void onInviteClicked() {
////        Intent intent = new .IntentBuilder("BAZUU")
////                .setMessage("CHARLO")
////                .setDeepLink(Uri.parse("SHASHA"))
////                .setCustomImage(R.drawable.charlo_gate_a)
////                .setCallToActionText("WELCOME MEHN")
////                .build();
////        startActivityForResult(intent, REQUEST_INVITE);
//    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("EXIT");
        builder.setIcon(R.drawable.ic_exit_to_app_black_24dp);
        builder.setMessage("Are you sure you want to exit?");

        builder.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(getApplicationContext(),first.class));
//                finish();
//                Intent intent = new Intent(getApplicationContext(),first.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("EXIT",true);
//                startActivity(intent);
                //closing app without going to first activity
                moveTaskToBack(true);
                finish();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                dialog.dismiss();
            }
        });

//        AlertDialog dialog = builder.show();
        builder.show();
    }
}
////BA:2B:50:77:6D:93:E5:D6:39:65:8B:74:4B:51:8F:90:F0:EF:A2:1F-----SHA
