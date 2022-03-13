package com.hack.auroranite.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hack.auroranite.Models.Job;
import com.hack.auroranite.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    private DatabaseReference dbRef;

    private final String TAG = "JobAdapter";
    private Context context;
    private List<Job> jobs;

    public JobAdapter(Context context, List<Job> jobs) {
        this.context = context;
        this.jobs = jobs;

        Log.d(TAG, "JobAdapter: called");

        dbRef = FirebaseDatabase.getInstance().getReference("Jobs");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.job_card_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Job job = jobs.get(position);

        holder.jobTitleTV.setText(job.title);
        Log.d(TAG, "Job Title: " + job.title);

        holder.companyTV.setText(job.company.display_name);

        String salary = (job.salary_is_predicted.equals("1"))? job.salary_min + " - " + job.salary_max:"";
//        holder.salaryTV.setText(salary);
//        holder.jobTypeTV.setText(job.contract_time);

        String description = job.description;
        holder.descriptionTV.setText(description);
        holder.locationTV.setText(job.location.display_name);

//        DateTimeFormatter inp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'", Locale.ENGLISH);
//        DateTimeFormatter out = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault());
//        LocalDate date = LocalDate.parse(job.created.toString(), inp);
//        String formattedDate = out.format(date);
//        holder.dateTV.setText(formattedDate);

        String[] list = context.getResources().getStringArray(R.array.tags);
        String[] tags = new String[3];
        tags[0] = (job.category.label.contains("General"))?"General":job.category.label;
        int count = 1;

        for (String s: list) {
            if (count >= 3)
                break;

            if (description.toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT)))
                tags[count++] = s.trim();
        }

        holder.tagTV1.setText(tags[0]);

        if (tags[1] != null && !tags[1].equals(""))
            holder.tagTV2.setText(tags[1]);
        else holder.tagCard2.setVisibility(View.GONE);

        if (tags[2] != null && !tags[2].equals(""))
            holder.tagTV3.setText(tags[2]);
        else holder.tagCard3.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView jobTitleTV;
        public TextView companyTV;
//        public TextView salaryTV;
//        public TextView jobTypeTV;

        public CardView tagCard1;
        public TextView tagTV1;
        public CardView tagCard2;
        public TextView tagTV2;
        public CardView tagCard3;
        public TextView tagTV3;

        public TextView descriptionTV;
        public ImageView favImg;
        public Button applyBtn;
        public TextView locationTV;
        public TextView dateTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            jobTitleTV = itemView.findViewById(R.id.job_title_tv);
            companyTV = itemView.findViewById(R.id.company_tv);
//            salaryTV = itemView.findViewById(R.id.salary_tv);
//            jobTypeTV = itemView.findViewById(R.id.job_type_tv);

            tagCard1 = itemView.findViewById(R.id.tag_card1);
            tagTV1 = itemView.findViewById(R.id.tag_tv1);
            tagCard2 = itemView.findViewById(R.id.tag_card2);
            tagTV2 = itemView.findViewById(R.id.tag_tv2);
            tagCard3 = itemView.findViewById(R.id.tag_card3);
            tagTV3 = itemView.findViewById(R.id.tag_tv3);

            descriptionTV = itemView.findViewById(R.id.description_tv);
            favImg = itemView.findViewById(R.id.favourite_img);
            applyBtn = itemView.findViewById(R.id.apply_btn);
            locationTV = itemView.findViewById(R.id.location_tv);
            dateTV = itemView.findViewById(R.id.date_tv);

            applyBtn.setOnClickListener(v -> {
                Job job = jobs.get(getAdapterPosition());
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(job.redirect_url)));
            });

            favImg.setOnClickListener(v -> {
                Job job = jobs.get(getAdapterPosition());
                if (job.favourite)
                    favImg.setImageResource(R.drawable.ic_star_border);
                else
                    favImg.setImageResource(R.drawable.ic_star_filled);

                job.favourite = !job.favourite;
                notifyDataSetChanged();
                updateDB(job);
            });
        }
    }

    private void updateDB(Job job) {
        DatabaseReference ref = dbRef.child(job.id);
        if (job.favourite) {
            ref.setValue(job).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Log.d(TAG, "updateDB: Favourite added");
                else
                    Toast.makeText(context, "Couldn't update job", Toast.LENGTH_SHORT).show();
            });
        } else {
            ref.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Log.d(TAG, "updateDB: Favourite removed");
                else
                    Toast.makeText(context, "Couldn't update job", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
