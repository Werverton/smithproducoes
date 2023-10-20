package com.example.smithproducoes;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

public class DownloadVideoActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
//    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
//        if(result){
//            requestStoragePermission();
//        }
//    });

    private long downloadID;
    private EditText urlListId;
    private Button downloadButton;
    private static final String TAG = "DownloadVideoActivity";

    private static final String BASE_URL = "https://drive.google.com/uc?export=download&id=";

    private long downloadId;
    private int downloadCount = 0;
    List<VideoModel> listDeUrls = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_video);

        isPermissionGranted();

        urlListId = findViewById(R.id.URLInput);



        downloadButton = findViewById(R.id.button);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url1 = "https://drive.google.com/uc?export=download&id=1uHEEku8YIhVqixThZ3qv70ZQ2Zdnl0fC";
                String url2 = "https://drive.google.com/uc?export=download&id=1EyJ0bWwGtmlpZj1TTdteD9GOiHU_KIO0";
                String url3 = "https://drive.google.com/uc?export=download&id=1bNsof3tOIrcHkFUC88X1EWW6t8FyOair";
                String url4 = "https://drive.google.com/uc?export=download&id=1Mm56njT0dQ3mZzRaAGsZHw9lGAtXbMEF";
                String url5 = "https://drive.google.com/uc?export=download&id=1sq3btgjqeb_L5MPlKESMYVSOIjTIenav";

                listDeUrls.add(new VideoModel(url1, "propaganda1","qualquernome1"));
                listDeUrls.add(new VideoModel(url2, "propaganda2","qualquernome2"));
                listDeUrls.add(new VideoModel(url3, "proganda3", "Qualquernome3"));
                listDeUrls.add(new VideoModel(url4, "propaganda4", "uqlauqernome4"));
                listDeUrls.add(new VideoModel(url5, "propaganda5", "qualquernome5"));
                //Url da lista
                //https://drive.google.com/file/d/1j53hOMduSy7i1I-QTS1vmx0ktjLKAVHd/view?usp=share_link
                //https://drive.google.com/file/d/1j53hOMduSy7i1I-QTS1vmx0ktjLKAVHd/view?usp=share_link

                String urlBase = "https://drive.google.com/uc?export=download&id=";
                String playlistUrl = urlBase+urlListId.getText().toString();
                Toast.makeText(DownloadVideoActivity.this, playlistUrl, Toast.LENGTH_SHORT).show();




                //String videoUrl = "https://drive.google.com/uc?export=download&id=1Mm56njT0dQ3mZzRaAGsZHw9lGAtXbMEF";
                //String videoTitle = "video";
                //downloadJsonFile(DownloadVideoActivity.this, playlistUrl);
                for (VideoModel video : listDeUrls) {
                    downloadVideo(DownloadVideoActivity.this, video.getUrl(), video.getName());
                }

            }
        });
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }


    private void downloadJsonFile(DownloadVideoActivity downloadVideoActivity, String playlistUrl) {
        Log.i(TAG, "Download JSON iniciado");
        try{
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(playlistUrl));
            request.setTitle("Baixando playlist");
            request.setDescription("Baixando Video");


            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, "playlist" +".json");
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

            request.allowScanningByMediaScanner();
            request.setAllowedOverMetered(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setAllowedOverRoaming(true);

            DownloadManager manager = (DownloadManager) downloadVideoActivity.getSystemService(Context.DOWNLOAD_SERVICE);
            downloadID = manager.enqueue(request);
        } catch (IllegalArgumentException e){
            Log.e(TAG, e.toString());

        }

        Log.i(TAG, "Download JSON concluido");
    }


    private boolean isVideoAlreadyDownloaded(String videoFileName) {
        // Obtenha o caminho da pasta "Movies"
        File moviesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        if (moviesDir != null) {
            // Crie um arquivo com o caminho completo do vídeo que você deseja verificar
            File videoFile = new File(moviesDir, videoFileName+".mp4");

            Log.i(TAG, "video file : "+videoFile);

            Boolean exist = videoFile.exists();

            Log.i(TAG, "video file : "+exist);

            return exist;
        }
        return false;
    }

    private void downloadVideo(DownloadVideoActivity downloadVideoActivity, String videoUrl, String videoTitle) {
        Log.i(TAG, "Download video iniciado");
            try{
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(videoUrl));
                request.setTitle(videoTitle);
                request.setDescription("Baixando Video");


                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, videoTitle +".mp4");
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

                request.allowScanningByMediaScanner();
                request.setAllowedOverMetered(true);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setAllowedOverRoaming(true);

                DownloadManager manager = (DownloadManager) downloadVideoActivity.getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            } catch (IllegalArgumentException e){
                Toast.makeText(DownloadVideoActivity.this, "Algo deu errado!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Line no: 455,Method: downloadFile: Download link is broken");
            }

        Log.i(TAG, "Download video concluido.");
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            long receivedID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (receivedID != -1) {

                downloadCount++;
                if(downloadCount== listDeUrls.size()){
                    Toast.makeText(DownloadVideoActivity.this, "Download concluído!", Toast.LENGTH_SHORT).show();
                    Intent newActivityIntent = new Intent(DownloadVideoActivity.this, MainActivity.class);
                    startActivity(newActivityIntent);
                    finish();
                }

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Certifique-se de desregistrar o BroadcastReceiver quando não for mais necessário
        if (onDownloadComplete != null) {
            unregisterReceiver(onDownloadComplete);
        }
    }



    //Permissões ------------------------------------
    private void isPermissionGranted(){
        if(ContextCompat.checkSelfPermission(DownloadVideoActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(DownloadVideoActivity.this, "you have al", Toast.LENGTH_SHORT).show();
        } else {
            requestStoragePermission();
        }
    }
    private void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permissão necessária")
                    .setMessage("Permitir o app gerenciar os arquivos")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(DownloadVideoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();

        } else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissão concedida", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
