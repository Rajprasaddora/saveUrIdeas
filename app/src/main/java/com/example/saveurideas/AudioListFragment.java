package com.example.saveurideas;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;

public class AudioListFragment extends Fragment implements OnItemSelectedListener, View.OnClickListener {


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
    SeekBar seekbar;
    Handler myHandler;
    Runnable runnable;


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
        seekbar=view.findViewById(R.id.IdSeekBar);
        playButtonInPlayerSheet.setOnClickListener(this);
        skipNxtInPlayerSheet.setOnClickListener(this);
        skipPrevInPlayerSheet.setOnClickListener(this);


        myBottomSheetBehavior = BottomSheetBehavior.from(player_sheet);
        String path = getActivity().getExternalFilesDir("/").getAbsolutePath();
        File directory = new File(path);
        allFileNames = directory.listFiles();
        myListAdapter = new AudioListAdapter(allFileNames, this);
        myList.setLayoutManager(new LinearLayoutManager(getActivity()));
        myList.setAdapter(myListAdapter);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(myMediaPlayer==null){
                    Toast.makeText(getContext(),"select a item to play ",Toast.LENGTH_SHORT).show();
                    seekBar.setProgress(0);
                    return ;
                }
                pauseAudio();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress=seekBar.getProgress();
                myMediaPlayer.seekTo(progress);
                resumeAudio();
            }
        });
        myBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState==BottomSheetBehavior.STATE_HIDDEN){
                    myBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
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
                isRecording=false;
            }

        });
        seekbar.setMax(myMediaPlayer.getDuration());
        myHandler=new Handler();
        updateHandler();
        myHandler.postDelayed(runnable,0);
    }

    public void stopAudio() {
        isRecording = false;
        playButtonInPlayerSheet.setImageResource(R.drawable.play_arrow);
        playSheetStatus.setText("stopped");
        myMediaPlayer.stop();
        myMediaPlayer.release();
        myMediaPlayer=null;
        myHandler.removeCallbacks(runnable);
    }
    public void pauseAudio(){
        if(myMediaPlayer!=null){
            playButtonInPlayerSheet.setImageResource(R.drawable.play_arrow);
            playSheetStatus.setText("paused");
            myMediaPlayer.pause();
            isRecording=false;
            myHandler.removeCallbacks(runnable);
            return ;
        }
    }
    public void resumeAudio(){
        if(myMediaPlayer!=null){
            playButtonInPlayerSheet.setImageResource(R.drawable.pause_arrow);
            playSheetStatus.setText("playing...");
            myMediaPlayer.start();
            isRecording=true;
            updateHandler();
            myHandler.postDelayed(runnable,0);
            return ;
        }
    }
    public void updateHandler(){
        runnable=new Runnable() {
            @Override
            public void run() {
                seekbar.setProgress(myMediaPlayer.getCurrentPosition());
                myHandler.postDelayed(this,500);
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.IdPlayButtonInPlayerSheet:{
                if(myMediaPlayer==null){
                    Toast.makeText(getActivity(),"select a item to play",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(isRecording){
                        pauseAudio();
                    }
                    else{
                        resumeAudio();
                    }
                }
                break;
            }

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(myMediaPlayer!=null){
            stopAudio();
        }
    }
}