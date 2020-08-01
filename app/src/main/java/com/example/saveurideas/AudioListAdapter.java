package com.example.saveurideas;

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
    public AudioListAdapter(File[] allFiles) {
        this.allFiles=allFiles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ideaName.setText(allFiles[position].getName());
    }

    @Override
    public int getItemCount() {
        return allFiles.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView playIconInEachIdea;
        TextView ideaName;
        TextView timeOfCreation;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            playIconInEachIdea= itemView.findViewById(R.id.IdPlayIconInEachItem);
            ideaName=itemView.findViewById(R.id.IdIdeaName);
            timeOfCreation=itemView.findViewById(R.id.IdTimeOfCreation);
        }
    }
}
