package joslabs.kbssmsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



public class SplashScreen extends AppCompatActivity {

        SharedPreferences pref;
        private final static int SPLASH_TIME=3000;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    /** Called when the activity is first created. */
    Thread splashTread;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pref=getApplicationContext().getSharedPreferences("regd",0);
        Toast.makeText(this, ""+pref.getString("username",null), Toast.LENGTH_SHORT).show();
        StartAnimations();

    }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        RelativeLayout l=(RelativeLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();

        ImageView v = (ImageView) findViewById(R.id.splash2);
        v.clearAnimation();
        v.startAnimation(anim);
        TextView iv = (TextView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);
        TextView owner=(TextView) findViewById(R.id.tvOwner);
        owner.clearAnimation();
        owner.setAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 4500) {
                        sleep(100);
                        waited += 100;
                    }
                    String k=pref.getString("username",null);
                    Intent intent = new Intent(SplashScreen.this,
                            MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    SplashScreen.this.finish();
                    //TODO can delete above intent and uncomment this bolck
                    /*if(k==null) {
                        Intent intent = new Intent(SplashScreen.this,
                                RegisterActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        SplashScreen.this.finish();
                    }
                    else
                    {
                        SharedPreferences fungas=getApplicationContext().getSharedPreferences("funga",0);
                        String funga=fungas.getString("funga",null);
                        if(funga==null) {
                            Intent intent = new Intent(SplashScreen.this,
                                    MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            SplashScreen.this.finish();
                        }
                        else
                        {
                            Intent intent = new Intent(SplashScreen.this,
                                    Appmanager.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            SplashScreen.this.finish();
                        }
                    }*/
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                   SplashScreen.this.finish();
                }

            }
        };
        splashTread.start();

    }



}
