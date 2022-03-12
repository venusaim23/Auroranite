package com.hack.auroranite.Activities;

import static com.hack.auroranite.Util.Utility.layoutErrorManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hack.auroranite.R;
import com.hack.auroranite.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.signInBtn.setOnClickListener(v -> login());

        binding.createAccountTv.setOnClickListener(v -> startActivity(new Intent(this, CreateAccount.class)));

        binding.passwordEtLogin.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                login();
                return true;
            }
            return false;
        });
    }

    private void login() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.loginLayout.getWindowToken(), 0);

        binding.progressBarLogin.setVisibility(View.VISIBLE);

        String email = binding.emailEtLogin.getText().toString().trim();
        String password = binding.passwordEtLogin.getText().toString().trim();

        if (email.isEmpty()) {
            layoutErrorManager(binding.emailTilLogin, binding.emailEtLogin, "Required");
            binding.progressBarLogin.setVisibility(View.GONE);
            return;
        }

        if (password.isEmpty()) {
            layoutErrorManager(binding.passwordTilLogin, binding.passwordEtLogin, "Required");
            binding.progressBarLogin.setVisibility(View.GONE);
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mUser = mAuth.getCurrentUser();
                displayHome();
            } else {
                Toast.makeText(LoginActivity.this, "Sign in failed: " +
                        task.getException().getMessage(), Toast.LENGTH_LONG).show();
                binding.progressBarLogin.setVisibility(View.GONE);
            }
        }).addOnFailureListener(e -> {
            binding.progressBarLogin.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, "Sign in failed: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mUser = mAuth.getCurrentUser();
        if (mUser != null)
            displayHome();
    }

    private void displayHome() {
        binding.progressBarLogin.setVisibility(View.GONE);
        Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, HomeScreen.class));
        finish();
    }
}