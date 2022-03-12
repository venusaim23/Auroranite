package com.hack.auroranite.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hack.auroranite.Adapters.JobAdapter;
import com.hack.auroranite.Models.Job;
import com.hack.auroranite.R;
import com.hack.auroranite.Util.Credentials;
import com.hack.auroranite.databinding.FragmentJobsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JobsFragment extends Fragment {

    private static final String TAG = "JobsFragment";
    private FragmentJobsBinding binding;

    private Context context;

    private List<Job> jobs;
    private JobAdapter adapter;
    private RequestQueue queue;

    public static final String BASE_URL = "https://api.adzuna.com/v1/api/jobs/in/search/1?";
    public static final String QUES = "?";
    public static final String AND = "&";
    public static final String APP_ID = "app_id=" + Credentials.APP_ID;
    public static final String APP_KEY = "app_key=" + Credentials.APP_KEY;
    public static final String RESULTS_PER_PAGE = "results_per_page=20";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public JobsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentJobsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerViewJobs.setHasFixedSize(true);
        binding.recyclerViewJobs.setLayoutManager(new LinearLayoutManager(context));
        jobs = new ArrayList<>();
        adapter = new JobAdapter(context, jobs);
        binding.recyclerViewJobs.setAdapter(adapter);

        queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                BASE_URL + APP_ID + AND + APP_KEY + AND + RESULTS_PER_PAGE, null,
                this::extractData, error -> {
            Toast.makeText(context, "Could not retrieve data. Error: " + error.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.d(TAG, "Could not retrieve access token. Error: " + error.getMessage());
        });

        queue.add(request);
    }

    private void extractData(JSONObject response) {
        Gson gson = new Gson();

        try {
            String count = response.getString("count");
            Log.d(TAG, "Count: " + count);
            Toast.makeText(context, "Count: " + count, Toast.LENGTH_SHORT).show();

            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < response.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                Type type = new TypeToken<Job>() {
                }.getType();
                Job job = gson.fromJson(result.toString(), type);
                Log.d(TAG, "Job title: " + job.title);

                jobs.add(job);
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            Toast.makeText(context, "Could not retrieve data. Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.d(TAG, "Could not retrieve access token. Error: " + e.getMessage());
        }
    }
}