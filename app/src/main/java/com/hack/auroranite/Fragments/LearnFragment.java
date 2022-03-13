package com.hack.auroranite.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hack.auroranite.Adapters.LearnAdapter;
import com.hack.auroranite.Models.Course;
import com.hack.auroranite.R;
import com.hack.auroranite.databinding.FragmentLearnBinding;

import java.util.ArrayList;
import java.util.List;

public class LearnFragment extends Fragment {

    private FragmentLearnBinding binding;

    private Context context;

    private String[] courseNames;
    private String[] credits;
    private String[] links;
    private List<Course> courses;

    private LearnAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public LearnFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLearnBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerViewLearn.setHasFixedSize(true);
        binding.recyclerViewLearn.setLayoutManager(new LinearLayoutManager(context));

        courseNames = context.getResources().getStringArray(R.array.tags);
        credits = context.getResources().getStringArray(R.array.credits);
        links = context.getResources().getStringArray(R.array.playlist_links);
        courses = new ArrayList<>();

        for (int i = 0; i < courseNames.length; i++) {
            Course course = new Course(courseNames[i], credits[i], links[i]);
            courses.add(course);
        }

        adapter = new LearnAdapter(context, courses);
        binding.recyclerViewLearn.setAdapter(adapter);
    }
}