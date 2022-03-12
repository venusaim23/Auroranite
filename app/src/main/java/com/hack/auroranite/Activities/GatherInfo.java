package com.hack.auroranite.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hack.auroranite.R;
import com.hack.auroranite.databinding.ActivityGatherInfoBinding;

public class GatherInfo extends AppCompatActivity {

    private ActivityGatherInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGatherInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}