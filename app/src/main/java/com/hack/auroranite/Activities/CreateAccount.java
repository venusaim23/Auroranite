package com.hack.auroranite.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hack.auroranite.Models.User;
import com.hack.auroranite.R;
import com.hack.auroranite.databinding.ActivityCreateAccountBinding;

public class CreateAccount extends AppCompatActivity {

    private ActivityCreateAccountBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference dbRef;

    private String[] modes;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if (mUser != null)
            finish();

        dbRef = FirebaseDatabase.getInstance().getReference("Users");

        modes = getResources().getStringArray(R.array.work_mode);

        binding.createBtnSu.setOnClickListener(v -> getDetails());
    }

    private void getDetails() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.signUpLayout.getWindowToken(), 0);

        binding.progressBarSignUp.setVisibility(View.VISIBLE);

        String name = binding.nameEtSu.getText().toString().trim();
        String email = binding.emailEtSu.getText().toString().trim();
        String password = binding.passwordEtSu.getText().toString();
        String repPassword = binding.repPasswordEtSu.getText().toString();
        String education = binding.educationEtSu.getText().toString().trim();
        String major = binding.majorEtSu.getText().toString().trim();
        String gradYear = binding.gradYearEtSu.getText().toString().trim();
        String country = binding.countryEtSu.getText().toString().trim();
        String skills = binding.skillsEtSu.getText().toString().trim();
        String experience = binding.expEtSu.getText().toString().trim();
        String salary = binding.salaryEtSu.getText().toString().trim();
        String mode = modes[binding.spinnerPreferredModeSu.getSelectedItemPosition()];

        user = new User(mUser.getUid(), name, email, education, major, gradYear, country,
                skills, experience, salary, mode);

        if (password.equals(repPassword))
            createAccount(email, password);
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CreateAccount.this, "Account created", Toast.LENGTH_SHORT).show();
                        mUser = mAuth.getCurrentUser();
                        updateDetails();
                    } else {
                        Toast.makeText(CreateAccount.this, "Account could not be created: "
                                + task.getException(), Toast.LENGTH_LONG).show();
                        binding.progressBarSignUp.setVisibility(View.GONE);
                    }
                });
    }

    private void updateDetails() {
        dbRef.child(user.getUID()).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Details saved", Toast.LENGTH_SHORT).show();
            } else {
                //handle error - prompt user to re-upload details after logging
                Toast.makeText(this, "Details could not be saved: "
                        + task.getException(), Toast.LENGTH_LONG).show();
            }
            binding.progressBarSignUp.setVisibility(View.GONE);
            startActivity(new Intent(this, HomeScreen.class));
            finish();
        });
    }
}