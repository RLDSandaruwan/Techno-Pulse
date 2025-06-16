package com.example.technopulse1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class UserInfoActivity extends AppCompatActivity {

    TextView userName, userEmail;
    ImageView iconBack;
    MaterialButton btnEdit, btnSignOut;

    // 🔒 Sign Out Popup
    FrameLayout signOutDialog;
    MaterialButton btnConfirmSignOut, btnCancelSignOut;

    // ✏️ Edit Info Popup
    FrameLayout editPopup;
    EditText editUsername, editEmail;
    MaterialButton btnConfirmEdit, btnCancelEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // 🔗 Basic UI
        iconBack = findViewById(R.id.iconBack);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        btnEdit = findViewById(R.id.btnEdit);
        btnSignOut = findViewById(R.id.btnSignOut);

        // 🔒 Sign Out Popup
        signOutDialog = findViewById(R.id.signOutDialog);
        btnConfirmSignOut = findViewById(R.id.btnConfirmSignOut);
        btnCancelSignOut = findViewById(R.id.btnCancelSignOut);

        // ✏️ Edit Info Popup
        editPopup = findViewById(R.id.editPopup);
        editUsername = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editEmail);
        btnConfirmEdit = findViewById(R.id.btnConfirmEdit);
        btnCancelEdit = findViewById(R.id.btnCancelEdit);

        // 🔄 Load User Info
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail.setText("Email: " + user.getEmail());

            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("users").child(user.getUid()).child("username");

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.exists() ? snapshot.getValue(String.class) : "User";
                    userName.setText("User name: " + name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    userName.setText("User name: Unknown");
                }
            });
        }

        // 🔙 Back
        iconBack.setOnClickListener(v -> finish());

        // ✏️ Show Edit Info Popup
        btnEdit.setOnClickListener(v -> {
            editPopup.setVisibility(View.VISIBLE);
            editUsername.setText(userName.getText().toString().replace("User name: ", ""));
            editEmail.setText(userEmail.getText().toString().replace("Email: ", ""));
        });

        // ✏️ Cancel Edit Info
        btnCancelEdit.setOnClickListener(v -> editPopup.setVisibility(View.GONE));

        // ✏️ Confirm Edit Info
        btnConfirmEdit.setOnClickListener(v -> {
            String newName = editUsername.getText().toString().trim();
            String newEmail = editEmail.getText().toString().trim();

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReference("users").child(currentUser.getUid());

                ref.child("username").setValue(newName);
                userName.setText("User name: " + newName);
                userEmail.setText("Email: " + newEmail); // for UI only
                editPopup.setVisibility(View.GONE);
            }
        });

        // 🚪 Show Sign Out Popup
        btnSignOut.setOnClickListener(v -> signOutDialog.setVisibility(View.VISIBLE));

        // 🚪 Cancel Sign Out
        btnCancelSignOut.setOnClickListener(v -> signOutDialog.setVisibility(View.GONE));

        // ✅ Confirm Sign Out
        btnConfirmSignOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(UserInfoActivity.this, SignInActivity.class));
            finish();
        });
    }
}

