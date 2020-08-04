package com.example.saveurideas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.util.Collections;

public class MainActivity extends AppCompatActivity implements OnSaveClickListener {

    DriveServiceHelper driveServiceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestSignIn();
    }
    public void requestSignIn(){
        GoogleSignInOptions signInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                .build();
        GoogleSignInClient client= GoogleSignIn.getClient(this,signInOptions);
        startActivityIfNeeded(client.getSignInIntent(),400);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 400:{
                handleSignInIntent(data);
                break;
            }
        }
    }
    public void handleSignInIntent(Intent data){
        GoogleSignIn.getSignedInAccountFromIntent(data).addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
            @Override
            public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                GoogleAccountCredential credential= GoogleAccountCredential.usingOAuth2(MainActivity.this, Collections.singleton(DriveScopes.DRIVE_FILE));
                credential.setSelectedAccount(googleSignInAccount.getAccount());
                Drive googleDriveService=new Drive.Builder(AndroidHttp.newCompatibleTransport(),
                        new GsonFactory(),credential).setApplicationName("MyDriveApp").build();
                driveServiceHelper=new DriveServiceHelper(googleDriveService);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void uploadFile(String filePath){
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("uploading to google drive ");
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        String filepath="/storage/emulated/0/WhatsApp/Media/WhatsApp Documents/Ericsson-INF.pdf";
        driveServiceHelper.createFile(filepath).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,"file uploaded to drive ",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "failed to upload : check api key ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}