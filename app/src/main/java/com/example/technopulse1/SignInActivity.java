package com.example.technopulse1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    EditText editUsername, editPassword;
    Button btnSignIn;
    TextView linkSignUp, linkForgotPassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // UI elements
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        linkSignUp = findViewById(R.id.linkSignUp);
//        linkForgotPassword = findViewById(R.id.linkForgotPassword);

        // Sign In logic
        btnSignIn.setOnClickListener(view -> {
            String email = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(this, "Login Success!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignInActivity.this, NewsActivity.class));
                                finish();
                            } else {
                                Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        // Link to Sign Up
        linkSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

//        // Forgot Password (not implemented yet)
//        linkForgotPassword.setOnClickListener(view ->
//                Toast.makeText(this, "Forgot password clicked", Toast.LENGTH_SHORT).show()
//        );

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


//        if (user != null) {
//            // ✅ User is signed in already
//            startActivity(new Intent(this, NewsActivity.class));
//            finish();
//        }

    }
}

//        daham123@gmail.com
//        123456
//        sandaruwan@gmail.com
