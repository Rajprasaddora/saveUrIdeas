package com.example.saveurideas;

import android.media.MediaPlayer;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;

public class AudioListFragment extends Fragment implements OnItemSelectedListener {


    ConstraintLayout player_sheet;
    File[] allFileNames;
    BottomSheetBehavior myBottomSheetBehavior;
    AudioListAdapter myListAdapter;
    RecyclerView myList;
    boolean isRecording = false;
    MediaPlayer myMediaPlayer;
    File fileToPlay;
    TextView playSheetStatus, fileNameInPlayerSheet;
    ImageView playButtonInPlayerSheet, skipNxtInPlayerSheet, skipPrevInPlayerSheet;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myList = view.findViewById(R.id.IdMyAudioList);
        player_sheet = view.findViewById(R.id.IdPlayerSheet);
        playSheetStatus = view.findViewById(R.id.IdPlayerSheetStatus);
        fileNameInPlayerSheet = view.findViewById(R.id.IdFileNameInPlayerSheet);
        playButtonInPlayerSheet = view.findViewById(R.id.IdPlayButtonInPlayerSheet);
        skipNxtInPlayerSheet = view.findViewById(R.id.IdSkipNxtButtonInPlayerSheet);
        skipPrevInPlayerSheet = view.findViewById(R.id.IdSkipPrevInPlayerSheet);


        myBottomSheetBehavior = BottomSheetBehavior.from(player_sheet);
        String path = getActivity().getExternalFilesDir("/").getAbsolutePath();
        File directory = new File(path);
        allFileNames = directory.listFiles();
        myListAdapter = new AudioListAdapter(allFileNames, this);
        myList.setLayoutManager(new LinearLayoutManager(getActivity()));
        myList.setAdapter(myListAdapter);
    }

    @Override
    public void onItemSelected(File file, int adapterPosition) {
        fileToPlay = file;
        if (isRecording) {
            stopAudio();
            playAudio(file);
        } else {
            playAudio(file);
        }
    }

    public void playAudio(File file) {
        myMediaPlayer = new MediaPlayer();
        myBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        fileToPlay = file;
        try {
            myMediaPlayer.setDataSource(fileToPlay.getAbsolutePath());
            myMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        playButtonInPlayerSheet.setImageResource(R.drawable.pause_arrow);
        playSheetStatus.setText("playing...");
        fileNameInPlayerSheet.setText(file.getName());
        myMediaPlayer.start();
        isRecording = true;
        myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playSheetStatus.setText("Finished");
                playButtonInPlayerSheet.setImageResource(R.drawable.play_arrow);
            }
        });
    }

    public void stopAudio() {
        isRecording = false;
        playButtonInPlayerSheet.setImageResource(R.drawable.play_arrow);
        playSheetStatus.setText("stopped");
        myMediaPlayer.stop();
        myMediaPlayer.release();
    }
}