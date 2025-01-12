package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class SigninActivity extends AppCompatActivity {

    EditText userEmail, userPassword;
    TextView signinBtn, signupBtn;
    String email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);
        userEmail = findViewById(R.id.emailText);
        userPassword = findViewById(R.id.password);
        signinBtn = findViewById(R.id.login);
        signupBtn = findViewById(R.id.signup);

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = userEmail.getText().toString().trim();
                password = userPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    userEmail.setError("Please enter your email");
                    userEmail.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    userPassword.setError("Please enter your password");
                    userPassword.requestFocus();
                    return;
                }
                Signin();
            }
        });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(SigninActivity.this, MainActivity.class));
            finish();
        }
    }

    private void Signin() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.trim(), password.trim())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                        intent.putExtra("name", username);
                        startActivity(intent);
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(e instanceof FirebaseAuthInvalidUserException){
                            Toast.makeText(SigninActivity.this, "User does not exist", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        else{
                            Toast.makeText(SigninActivity.this, "Authentication failed", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }
}