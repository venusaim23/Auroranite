package com.hack.auroranite.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hack.auroranite.Models.Course;
import com.hack.auroranite.R;

import java.util.List;

public class LearnAdapter extends RecyclerView.Adapter<LearnAdapter.ViewHolder> {

    private Context context;
    private List<Course> courses;

    public LearnAdapter(Context context, List<Course> courses) {
        this.context = context;
        this.courses = courses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.learn_card_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courses.get(position);

        holder.titleTV.setText(course.getCourseName());
        String credit = context.getResources().getString(R.string.credit) + " " + course.getCredit();
        holder.creditTv.setText(credit);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTV;
        public TextView creditTv;
        public Button linkBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.learn_title_tv);
            creditTv = itemView.findViewById(R.id.credit_tv);
            linkBtn = itemView.findViewById(R.id.go_course_btn);

            linkBtn.setOnClickListener(v -> {
                Course course = courses.get(getAdapterPosition());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(course.getLink()));
                context.startActivity(intent);
            });
        }
    }
}
