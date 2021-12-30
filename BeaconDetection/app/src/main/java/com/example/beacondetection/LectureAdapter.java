package com.example.beacondetection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.MyViewHolder> {

    Context context;
    ArrayList<Lecture> list;

    public LectureAdapter(Context context, ArrayList<Lecture> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.lectures_items,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Lecture lecture = list.get(position);
        holder.course.setText(lecture.getCourse());
        holder.lecture.setText(lecture.getLecture());
        holder.date.setText(lecture.getDate());
        holder.time.setText(lecture.getTime());
        holder.room.setText(lecture.getRoom());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView course, lecture, date, time, room;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            course = itemView.findViewById(R.id.course);
            lecture = itemView.findViewById(R.id.lecture);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            room = itemView.findViewById(R.id.room);

        }
    }

}
