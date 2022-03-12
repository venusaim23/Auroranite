package com.hack.auroranite.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;
import com.hack.auroranite.Fragments.JobsFragment;
import com.hack.auroranite.Fragments.LearnFragment;
import com.hack.auroranite.R;
import com.hack.auroranite.databinding.ActivityHomeScreenBinding;

public class HomeScreen extends AppCompatActivity {

    private ActivityHomeScreenBinding binding;

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}