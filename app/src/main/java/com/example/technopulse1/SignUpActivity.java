package com.example.technopulse1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    EditText editUsername, editPassword, editConfirmPassword, editEmail;
    Button btnSignUp;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // UI elements
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        editEmail = findViewById(R.id.editEmail);
        btnSignUp = findViewById(R.id.btnSignUp);
        TextView linkLogin = findViewById(R.id.linkLogin);

        // Sign up logic
        btnSignUp.setOnClickListener(view -> {
            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            String confirmPassword = editConfirmPassword.getText().toString().trim();
            String email = editEmail.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    String uid = user.getUid();
                                    DatabaseReference ref = FirebaseDatabase.getInstance()
                                            .getReference("users");

                                    // Save username
                                    ref.child(uid).child("username").setValue(username)
                                            .addOnCompleteListener(saveTask -> {
                                                if (saveTask.isSuccessful()) {
                                                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                                    finish(); // go back to login
                                                } else {
                                                    Toast.makeText(this, "Username save failed: " + saveTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                            } else {
                                Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        // Go back to login
        linkLogin.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            finish();
        });
    }
}
