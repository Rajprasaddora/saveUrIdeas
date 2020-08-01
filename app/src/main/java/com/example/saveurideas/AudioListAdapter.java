package com.example.saveurideas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.MyViewHolder> {
    File[] allFiles;
    TimeAgo timeAgo;
    OnItemSelectedListener myListener;
    Context context;
    public AudioListAdapter(File[] allFiles, OnItemSelectedListener myListener) {
        this.allFiles=allFiles;
        this.myListener=myListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        timeAgo=new TimeAgo();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ideaName.setText(allFiles[position].getName());
        holder.timeOfCreation.setText(timeAgo.getTimeAgo(allFiles[position].lastModified()));
    }

    @Override
    public int getItemCount() {
        return allFiles.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView playIconInEachIdea;
        TextView ideaName;
        TextView timeOfCreation;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            playIconInEachIdea= itemView.findViewById(R.id.IdPlayIconInEachItem);
            ideaName=itemView.findViewById(R.id.IdIdeaName);
            timeOfCreation=itemView.findViewById(R.id.IdTimeOfCreation);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myListener.onItemSelected(allFiles[getAdapterPosition()],getAdapterPosition());
        }
    }
}
