package com.example.smithproducoes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;

public class VideoPlayActivity extends AppCompatActivity {
    String path;

    private static final String TAG = "VideoPlayActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        VideoView videoView = findViewById(R.id.videoView);

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video;

        //String videoPath = "/storage/emulated/0/Movies/video";

        //"android.resource://" + getPackageName() + "/" + R.raw.video;
        //storage/emulated/0/Movies
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);


        Intent intent = getIntent();
        if (intent!=null){
            path = intent.getStringExtra("video");
            videoView.setVideoPath(path);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                    Log.i(TAG, "getVideos: "+ path);
                }
            });
        }





        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });

//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                ///storage/emulated/0/Movies/video.mp4
//
//
//                if(!(currentVideo < fileData.size())) {
//                    return;
//                }
//                Uri nextUri = Uri.parse(fileData.get(currentVideo++));
//                videoView.setVideoURI(nextUri);
//                videoView.start();
//
//                //to keep looping into the list, reset the counter to 0.
//                //in case you need to stop playing after the list is completed remove the code.
//                if(currentVideo == fileData.size()) {
//                    currentVideo = 0;
//                }
//            }
//        });



    }
}