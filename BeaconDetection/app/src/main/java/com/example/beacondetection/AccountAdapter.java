package com.example.beacondetection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder> {

    Context context;
    ArrayList<Student> list;

    //Adapter to display students in a recycler view
    public AccountAdapter(Context context, ArrayList<Student> list) {
        this.context = context;
        this.list = list;
    }

    //MyViewHolder is a RecyclerView class that describes how to display items in a recyclerview
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.account_items,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Student student = list.get(position);
        holder.username.setText(student.getUsername());
        holder.name.setText(student.getName());
        holder.surname.setText(student.getSurname());
        holder.age.setText(student.getAge());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView username, name, surname, age;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            name = itemView.findViewById(R.id.name);
            surname = itemView.findViewById(R.id.surname);
            age = itemView.findViewById(R.id.age);

        }
    }

}
