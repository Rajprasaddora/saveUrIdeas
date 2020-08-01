package com.example.saveurideas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;

import static android.service.autofill.Validators.and;

public class RecordeFragment extends Fragment implements View.OnClickListener {


    ImageView list_button;
    NavController navController;
    ImageView play_button,cancel_recording;
    ImageView save_button;
    boolean isRecording=false;
    MediaRecorder myMediaRecorder;
    TextInputEditText nameOfIdea;
    long timeWhenStopped=0;
    String currIdeaName="";
    Chronometer timer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recorde, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        list_button=view.findViewById(R.id.IdListBtn);
        play_button=view.findViewById(R.id.IdPlayButton);
        save_button=view.findViewById(R.id.IdSaveButton);
        nameOfIdea=view.findViewById(R.id.IdNameOfIdea);
        nameOfIdea.setText("");
        timer=view.findViewById(R.id.IdTimer);
        list_button.setOnClickListener(this);
        play_button.setOnClickListener(this);
        save_button.setOnClickListener(this);
        nameOfIdea.setOnClickListener(this);
        cancel_recording=view.findViewById(R.id.IdCancelRecording);
        cancel_recording.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.IdListBtn:{
                navController.navigate(R.id.action_recordeFragment_to_audioListFragment);
                break;
            }
            case R.id.IdPlayButton:{
                if(isRecording){
                    timeWhenStopped=timer.getBase()-SystemClock.elapsedRealtime();
                    timer.stop();
                    myMediaRecorder.pause();
                    play_button.setImageResource(R.drawable.play_button);
                    isRecording=false;
                }
                else{
                    if(checkForAudioRecordingPermission()){
                        if(myMediaRecorder==null){
                            Log.d("raj","mymediaRecorder is null");
                        }
                        else{
                            Log.d("raj","myMediaRecorder is not null");
                        }
                        if(myMediaRecorder==null){
                            String fileName=getActivity().getExternalFilesDir("/").getAbsolutePath();
                            String ideaName=nameOfIdea.getText().toString();
                            if(ideaName.equals("")){
                                Toast.makeText(getActivity(),"Enter a name for ur Idea",Toast.LENGTH_SHORT).show();
                                break;
                            }
                            myMediaRecorder=new MediaRecorder();
                            myMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            myMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                            myMediaRecorder.setOutputFile(fileName+"/"+ideaName+".3gp");
                            myMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                            try {
                                myMediaRecorder.prepare();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            timer.setBase(SystemClock.elapsedRealtime());
                            timer.start();
                            myMediaRecorder.start();
                            nameOfIdea.setFocusable(false);
                        }
                        else{
                            timer.setBase(SystemClock.elapsedRealtime()+timeWhenStopped);
                            timer.start();
//                            if(myMediaRecorder==null){
//                                Log.d("raj","mymediaRecorder is null");
//                            }
//                            else{
//                                Log.d("raj","myMediaRecorder is not null");
//                            }
                            myMediaRecorder.resume();
                        }
                        play_button.setImageResource(R.drawable.pause_button);
                        isRecording=true;
                    }

                }
                break;
            }
            case R.id.IdSaveButton: {
                if(myMediaRecorder==null){
                    Toast.makeText(getActivity(),"Record Your Idea First then try to save", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(isRecording){
                        Toast.makeText(getActivity(),"Finish Your Recording then save",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getActivity(),"Your Idea is saved",Toast.LENGTH_SHORT).show();
                        myMediaRecorder.stop();
                        myMediaRecorder.release();
                        myMediaRecorder=null;
                        nameOfIdea.setText("");
                        isRecording=false;
                        nameOfIdea.setFocusableInTouchMode(true);
                        timer.stop();
                        timer.setBase(SystemClock.elapsedRealtime());
                        timeWhenStopped=0;
                    }
                }
                break;
            }
            case R.id.IdNameOfIdea:{
                if(nameOfIdea.getFocusable()==View.NOT_FOCUSABLE){
                    Toast.makeText(getActivity(),"while Recording you cannot change name of idea",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.IdCancelRecording:{
                if(myMediaRecorder!=null){
                    myMediaRecorder.stop();
                    myMediaRecorder.release();
                    myMediaRecorder=null;
                    File file=new File(getActivity().getExternalFilesDir("/").getAbsolutePath()+"/"+nameOfIdea.getText()+".3gp");
                    file.delete();
                    nameOfIdea.setFocusableInTouchMode(true);nameOfIdea.setText("");
                    isRecording=false;
                    play_button.setImageResource(R.drawable.play_button);
                    timer.setBase(SystemClock.elapsedRealtime());
                    timer.stop();
                    timeWhenStopped=0;

                }
            }
        }
    }
    public boolean checkForAudioRecordingPermission(){
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.RECORD_AUDIO},123);
            return false;
        }
    }
}