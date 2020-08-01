package com.example.saveurideas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;

public class AudioListFragment extends Fragment {


    ConstraintLayout player_sheet;
    File[] allFileNames;
    AudioListAdapter myListAdapter;
    RecyclerView myList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myList=view.findViewById(R.id.IdMyAudioList);
        String path=getActivity().getExternalFilesDir("/").getAbsolutePath();
        File directory= new File(path);
        allFileNames=directory.listFiles();
        myListAdapter=new AudioListAdapter(allFileNames);
        myList.setLayoutManager(new LinearLayoutManager(getActivity()));
        myList.setAdapter(myListAdapter);
    }
}