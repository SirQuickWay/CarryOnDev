package com.project.carryon.carryon;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //button to go to Signup
        TextView goToSignup = (TextView)findViewById(R.id.button_newSignUp);

        goToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();
        Button loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButtonOnClick();
            }
        });

    }
    //to exit from the activity and the app
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        /*if(currentUser != null)
            Toast.makeText(LoginActivity.this, "User authenticated!",Toast.LENGTH_SHORT).show();*/

    }
    private void loginButtonOnClick()
    {
        EditText emailEditText = findViewById(R.id.editText_email);
        EditText passwordEditText = findViewById(R.id.editText_password);
        loginUser(emailEditText.getText().toString(), passwordEditText.getText().toString());
    }
    private void loginUser(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, get signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "User authenticated!",Toast.LENGTH_SHORT).show();
                            String currentID = mAuth.getCurrentUser().getUid();

                            //pass the currentID to HomeTabActivity
                            Intent i = new Intent(getApplicationContext(), HomeTabFinal.class);
                            i.putExtra("currentID", currentID);
                            startActivity(i);

                        } else {
                            // If sign in fails,
                            Toast.makeText(LoginActivity.this, "Email or password incorrect!",Toast.LENGTH_SHORT).show();
                            EditText passwordEditText = findViewById(R.id.editText_password);
                            passwordEditText.setText("");
                        }


                    }
                });
    }
}

