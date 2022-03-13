package com.hack.auroranite.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.alan.alansdk.AlanCallback;
import com.alan.alansdk.AlanConfig;
import com.alan.alansdk.events.EventCommand;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hack.auroranite.Fragments.JobsFragment;
import com.hack.auroranite.Fragments.LearnFragment;
import com.hack.auroranite.R;
import com.hack.auroranite.Util.Credentials;
import com.hack.auroranite.databinding.ActivityHomeScreenBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeScreen extends AppCompatActivity {

    private ActivityHomeScreenBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private JobsFragment jobsFragment;
    private LearnFragment learnFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarHome);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.gradient_login);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        jobsFragment = new JobsFragment();
        learnFragment = new LearnFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, jobsFragment).commit();

        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                replaceFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                replaceFragment(tab.getPosition());
            }

            private void replaceFragment(int position) {
                if (position == 0) {
                    jobsFragment = new JobsFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame_layout, jobsFragment)
                            .commit();
                } else if (position == 1) {
                    learnFragment = new LearnFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame_layout, learnFragment)
                            .commit();
                }
            }
        });

        AlanConfig config = AlanConfig.builder().setProjectId(Credentials.SDK_KEY).build();
        binding.alanButton.initWithConfig(config);

        AlanCallback alanCallback = new AlanCallback() {
            @Override
            public void onCommand(EventCommand eventCommand) {
                try {
                    JSONObject command = eventCommand.getData();
                    //data object is the JSON Object returned in p.play() method
                    JSONObject data = command.getJSONObject("data");
                    String commandName = data.getString("command");
                    //based on commandName we can perform different tasks
                    executeCommand(commandName, data);
                } catch (JSONException e) {
                    Log.e("AlanButton", e.getMessage());
                }
            }
        };

        binding.alanButton.registerCallback(alanCallback);
    }

    private void executeCommand(String commandName, JSONObject data) {
        if (commandName.equals("go_back")) {
            onBackPressed();
        }

        if (commandName.equals("exit")) {
            binding.alanButton.deactivate();
            finish();
        }

        if (commandName.equals("log_out")) {
            signOut();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sign_out) {
            signOut();
            return true;
        } else if (item.getItemId() == R.id.profile_settings) {
            openSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openSettings() {
        binding.alanButton.deactivate();
        Intent intent = new Intent(this, UpdateUser.class);
        intent.putExtra("User UID", mUser.getUid());
        startActivity(intent);
    }

    private void signOut() {
        mAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
        binding.alanButton.deactivate();
        finish();
    }
}