package com.hack.auroranite.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hack.auroranite.R;
import com.hack.auroranite.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}