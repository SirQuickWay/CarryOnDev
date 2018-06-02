package com.project.carryon.carryon;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.carryon.carryon.GeneralClasses.User;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String username;
    String surname;
    String name;
    String email;
    String password;
    String repeatPassword;
    String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        Button buttonSignUp = findViewById(R.id.button_signUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpButtonOnClick();
            }
        });
    }
    private void signUpButtonOnClick()
    {
        //localization of editText
        EditText usernameEd = findViewById(R.id.editText_username);
        EditText surnameEd = findViewById(R.id.editText_surname);
        EditText nameEd = findViewById(R.id.editText_name);
        EditText emailEd = findViewById(R.id.editText_email);
        EditText passwordEd = findViewById(R.id.editText_password);
        EditText repeatPasswordEd = findViewById(R.id.editText_repeatPassword);
        EditText phoneNumberEd = findViewById(R.id.editText_phoneNumber);

        //Get text from each editText
        username = usernameEd.getText().toString();
        surname = surnameEd.getText().toString();
        name = nameEd.getText().toString();
        email = emailEd.getText().toString();
        password = passwordEd.getText().toString();
        repeatPassword = repeatPasswordEd.getText().toString();
        phoneNumber = phoneNumberEd.getText().toString();
        //General errors checks
        if(username.equals(""))
            Toast.makeText(SignupActivity.this,"Username cannot be empty!", Toast.LENGTH_SHORT).show();
        else if(name.equals(""))
            Toast.makeText(SignupActivity.this,"Name cannot be empty!", Toast.LENGTH_SHORT).show();
        else if(surname.equals(""))
            Toast.makeText(SignupActivity.this,"Surname cannot be empty!", Toast.LENGTH_SHORT).show();
        else if(email.equals(""))
            Toast.makeText(SignupActivity.this,"Email cannot be empty!", Toast.LENGTH_SHORT).show();
        else if(password.equals(""))
            Toast.makeText(SignupActivity.this,"Password cannot be empty!", Toast.LENGTH_SHORT).show();
        else if(password.length() < 6)
            Toast.makeText(SignupActivity.this,"Password must be at least 6 characters!", Toast.LENGTH_SHORT).show();
        else if(!password.equals(repeatPassword))
            Toast.makeText(SignupActivity.this,"Password and Repeat password must be equal!", Toast.LENGTH_SHORT).show();
        else if(phoneNumber.equals(""))
            Toast.makeText(SignupActivity.this,"Phone Number cannot be empty!", Toast.LENGTH_SHORT).show();
        else {
            signUpUserWithFirebase(email, password);
        }

    }
    private void addUserOnDatabse()
    {
        User newUser = new User(name,surname,username,email,phoneNumber,"");
        newUser.setUserID(mAuth.getUid());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(mAuth.getUid()).set(newUser);
    }
    private void signUpUserWithFirebase(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(SignupActivity.this, "Signed up! You are now registered!", Toast.LENGTH_SHORT).show();
                            addUserOnDatabse();
                        }
                        else if(task.getException() instanceof FirebaseAuthUserCollisionException){ // If a user is already registered
                            Toast.makeText(SignupActivity.this, "User already registered!", Toast.LENGTH_SHORT).show();
                        }
                        else if(task.getException() instanceof FirebaseAuthWeakPasswordException){ // If the password is weak
                            Toast.makeText(SignupActivity.this, "Password too weak, please try with a stronger one!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

