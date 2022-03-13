package com.hack.auroranite.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hack.auroranite.Models.User;
import com.hack.auroranite.R;
import com.hack.auroranite.databinding.ActivityUpdateUserBinding;

public class UpdateUser extends AppCompatActivity {

    private static final String TAG = "UpdateUser";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference dbRef;

    private ActivityUpdateUserBinding binding;

    private String[] modes;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String uid = getIntent().getStringExtra("User UID");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("Users");

        modes = getResources().getStringArray(R.array.work_mode);

        if (!(mUser != null && uid.equals(mUser.getUid()))) {
            Toast.makeText(this, "User ID doesn't match!", Toast.LENGTH_SHORT).show();
            finish();
        }

        getUserData(uid);

        binding.updateBtnUp.setOnClickListener(v -> getDetails());
    }

    private void getUserData(String uid) {
        dbRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (user != null)
                    updateDetails();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: DB error - " + error.getMessage());
            }
        });
    }

    private void updateDetails() {
        binding.nameEtUp.setText(user.getName());
        binding.emailEtUp.setText(user.getEmail());
        binding.educationEtUp.setText(user.getEducation());
        binding.majorEtUp.setText(user.getMajor());
        binding.gradYearEtUp.setText(user.getGradYear());
        binding.countryEtUp.setText(user.getCountry());
        binding.skillsEtUp.setText(user.getSkills());
        binding.expEtUp.setText(user.getExperience());
        binding.salaryEtUp.setText(user.getSalary());

        int position = (user.getWorkMode().equals(modes[0])) ? 0 : 1;
        binding.spinnerPreferredModeUp.setSelection(position);
    }

    private void getDetails() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.updateLayout.getWindowToken(), 0);

        binding.progressBarSignUp.setVisibility(View.VISIBLE);

        String name = binding.nameEtUp.getText().toString().trim();
        String email = binding.emailEtUp.getText().toString().trim();
        String education = binding.educationEtUp.getText().toString().trim();
        String major = binding.majorEtUp.getText().toString().trim();
        String gradYear = binding.gradYearEtUp.getText().toString().trim();
        String country = binding.countryEtUp.getText().toString().trim();
        String skills = binding.skillsEtUp.getText().toString().trim();
        String experience = binding.expEtUp.getText().toString().trim();
        String salary = binding.salaryEtUp.getText().toString().trim();
        String mode = modes[binding.spinnerPreferredModeUp.getSelectedItemPosition()];

        user.updateUser(name, email, education, major, gradYear, country, skills, experience, salary, mode);

        updateDB();
    }

    private void updateDB() {
        dbRef.child(user.getUID()).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Details updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Details could not be updated: "
                        + task.getException(), Toast.LENGTH_LONG).show();
            }
            binding.progressBarSignUp.setVisibility(View.GONE);
            startActivity(new Intent(this, HomeScreen.class));
            finish();
        });
    }
}