package com.example.smithproducoes;



import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.smithproducoes.core.UpdateCheckJobService;
import com.example.smithproducoes.model.Video;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
//    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
//        if(result){
//            getVideos();
//        }
//    });
    VideoView videoView;
    private static final String TAG = "MyActivity";

    ArrayList<Video> arrayList = new ArrayList<>();
    ArrayList<String> videoUrls = new ArrayList<>();
    RecyclerView recyclerView;
    private static int currentVideo = 0;
    private int position = 0;
    private static final int UPDATE_JOB_ID = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ComponentName serviceName = new ComponentName(getPackageName(), UpdateCheckJobService.class.getName());
        JobInfo jobInfo = new JobInfo.Builder(UPDATE_JOB_ID, serviceName)
                .setPeriodic(24 * 60 * 60 * 1000) // A cada 24 horas
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);


//        if (ContextCompat.checkSelfPermission( MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            activityResultLauncher.launch (android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        } else if (ContextCompat.checkSelfPermission( MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            activityResultLauncher.launch (android.Manifest.permission.READ_EXTERNAL_STORAGE);
//        } else{
//            videoUrls = getVideos();
//        }


        videoUrls = getVideos();
        Log.i(TAG, "tamanho da list "+ videoUrls.size());
        if(videoUrls.isEmpty()){
            Toast.makeText(this,"Sem Videos", Toast.LENGTH_SHORT).show();
            Intent newActivityIntent = new Intent( MainActivity.this, DownloadVideoActivity.class);
            startActivity(newActivityIntent);
            finish();
        }
        playVideo(position);



    }
    private void playVideo(int index){
        VideoView videoView = findViewById(R.id.videoView);
        Log.i(TAG, "playvideo chamado 0: ");
        if (index < videoUrls.size()) {

            Uri videoUri = Uri.parse(videoUrls.get(index));
            videoView.setVideoURI(videoUri);
            Log.i(TAG, "URL do video 0: "+videoUrls.get(index));

            MediaController mediaController = new MediaController(this);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);
            videoView.setOnCompletionListener(mp -> {
                // Move to the next video when the current video is completed
                position++;
                Log.i(TAG, "position : "+position);
                playVideo(position);
            });
            videoView.start();
        } else {
            position = 0;
            playVideo(position);
            // All videos have been played
            // You can handle this as needed, e.g., show a message or restart the playlist
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getVideos();
    }

    private ArrayList<String> getVideos(){
        videoUrls.clear();
        String videoPath = "/sdcard/Movies";
        File file = new File(videoPath);
        File[] files = file.listFiles();
        if(files != null){
            for (File fileItem : files){
                if(fileItem.getPath().endsWith(".mp4")){
                    videoUrls.add(fileItem.getPath());
                    Log.i(TAG, "getVideos: "+ fileItem.getPath());
                }
            }
        }
        return videoUrls;

    }

}