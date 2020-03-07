package com.example.shadrak.expensestracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity
{

    private  ImageView logo;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_splash);



        mAuth = FirebaseAuth.getInstance();

        logo = findViewById(R.id.logo);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
        logo.startAnimation(animation);



        Thread timer = new Thread()
        {

            @Override
            public void run()
            {
                try
                {
                    sleep(2000);

                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    if(currentUser != null)
                    {
                        System.out.println("Current Userid "+currentUser.getUid());
                        Intent inToHome = new Intent(SplashActivity.this, Homeactivity.class);
                        startActivity(inToHome);
                        finish();
                    }
                    else{
                        System.out.println("errordsfkjhfkdah dhf");
                        Intent inToHome = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(inToHome);
                        finish();

                    }

                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this,"Your Message", Toast.LENGTH_LONG).show();
                }

            }
        };
        timer.start();
    }


}
