package com.example.saveurideas;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriveServiceHelper {
    private final Executor mExecutor= Executors.newSingleThreadExecutor();
    private Drive mDriveService;
    public DriveServiceHelper(Drive mDriveService){
        this.mDriveService=mDriveService;
    }

    public Task<String > createFile(String filePath){
        return Tasks.call(mExecutor,()->{
            File fileMetaData=new File();
            fileMetaData.setName("MyPDFFile");
            java.io.File file=new java.io.File(filePath);
            FileContent mediaContent=new FileContent("video/3gpp",file);
            File myFile =null;
            try{
                myFile=mDriveService.files().create(fileMetaData,mediaContent).execute();

            }
            catch(Exception e){
                e.printStackTrace();
            }
            if(file==null){
                throw new IOException("Null resule while file creation ");
            }
            return myFile.getId();
        });
    }
}
