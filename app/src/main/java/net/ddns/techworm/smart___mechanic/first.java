package net.ddns.techworm.smart___mechanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class first extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3500;
    ImageView logo;
    TextView smart_way,smart_mechanic;
    Animation top,bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_first);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        },SPLASH_TIME_OUT);


        logo = findViewById(R.id.logo);
        smart_mechanic = findViewById(R.id.smart_mechanic);
        smart_way = findViewById(R.id.smart_way);
        top = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        logo.setAnimation(top);
        smart_way.setAnimation(bottom);
        smart_mechanic.setAnimation(bottom);
//        if (getIntent().getBooleanExtra("EXIT",false)){
//            finish();
//        }
    }

}
