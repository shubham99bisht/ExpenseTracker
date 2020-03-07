package com.example.shadrak.expensestracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText emailid, password;
    TextView register;
    Button login;
    private  FirebaseAuth mFirebaseAuth;

    private ProgressDialog LoadingBar;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_main);

        LoadingBar = new ProgressDialog(this);

        emailid = findViewById(R.id.emailid);
        password = findViewById(R.id.password);
        login = findViewById(R.id.btnlogin);

        register = findViewById(R.id.edittextregisterlink);

        mFirebaseAuth = FirebaseAuth.getInstance();



        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showProgressBar();
//                LoadingBar.setTitle("Loading...");
//                LoadingBar.setMessage("Few seconds to go...");
//                LoadingBar.show();
                String email = emailid.getText().toString();
                String pwd = password.getText().toString();

                if(email.isEmpty()) {
                    emailid.setError("Please enter your EmailId");
                    emailid.requestFocus();
                }
                else if(pwd.isEmpty()) {
                    password.setError("Please Enter your Password!!");
                    password.requestFocus();
                }
                else if(!email.isEmpty() && !pwd.isEmpty()) {
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(MainActivity.this,"Email/Password is incorrect",Toast.LENGTH_SHORT).show();
                            } else
                                {
                                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                    updateUI(user);

                            }
                        }

                        private void updateUI(FirebaseUser user)
                        {
                            if (user != null)
                            {
                                Intent inToHome = new Intent(MainActivity.this, Homeactivity.class);
                                //inToHome.putExtra("uid",uid);
                                startActivity(inToHome);
                                finish();
                                hideProgress();

                            } else
                                {
                                    Intent inToHome = new Intent(MainActivity.this, MainActivity.class);
                                    startActivity(inToHome);
                                    finish();
                            }
                        }
                    });
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Registeractivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void showProgressBar(){

        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Loading...");
        progressBar.show();
    }

    private void hideProgress(){
        if(progressBar!=null && progressBar.isShowing()){
            progressBar.hide();
        }
    }

}
