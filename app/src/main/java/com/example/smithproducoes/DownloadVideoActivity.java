package com.example.smithproducoes;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.database.Cursor;
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

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;



import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DownloadVideoActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
//    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
//        if(result){
//            requestStoragePermission();
//        }
//    });

    private long downloadID;
    private EditText urlListId;
    private TextView progresso;
    private Button downloadButton;
    private static final String TAG = "DownloadVideoActivity";

    private static final String BASE_URL = "https://drive.google.com/uc?export=download&id=";
    private static final String JSON_URL = "https://https://raw.githubusercontent.com/Werverton/justJsonRaw/main/playlistvideo.json.google.com/uc?export=download&id=";

    private long downloadId;

    private DownloadManager downloadManager;
    private int downloadCount = 0;
    List<Video> listDeUrls = new ArrayList<>();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_video);

        isPermissionGranted();
        deleteFilesInMoviesFolder();


        urlListId = findViewById(R.id.URLInput);
        progressBar = findViewById(R.id.progressBar);
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        progresso = findViewById(R.id.textView2);

//        SharedPreferences sharedPreferences = getSharedPreferences("MinhasPreferencias", Context.MODE_PRIVATE);
//        String textoDigitado = urlListId.getText().toString();
//
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("texto_salvo", textoDigitado);
//        editor.apply();

//        SharedPreferences sharedPreferences = getSharedPreferences("MinhasPreferencias", Context.MODE_PRIVATE);
//        String textoSalvo = sharedPreferences.getString("texto_salvo", "");
//        urlListId.setText(textoSalvo);





        downloadButton = findViewById(R.id.button);

        downloadButton.setOnClickListener(view -> {

            getJsonData(urlListId.getText().toString());

            //downloadVideo(listDeUrls.get(downloadCount).getFileId(), listDeUrls.get(downloadCount).getName());

        });

        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    private void getJsonData(String URL) {
        //String URL ="https://raw.githubusercontent.com/Werverton/justJsonRaw/main/playlistvideo.json";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, response -> {

            try {
                JSONArray videos = response.getJSONArray("videos");
                //JSONObject videosData = videos.getJSONObject(0);

                for (int i = 0; i < videos.length(); i++) {
                    JSONObject videoObject = videos.getJSONObject(i);
                    Video video = new Video(
                            videoObject.getString("url"),
                            videoObject.getString("fileId"),
                            videoObject.getString("name"));
                    listDeUrls.add(video);

                    Log.i(TAG,"Video objeto: "+video.toString());

                }
                downloadVideo(listDeUrls.get(downloadCount).getFileId(), listDeUrls.get(downloadCount).getName());

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, error -> Log.d(TAG,"onErrorResponse"+error.getMessage()));
        requestQueue.add(jsonObjectRequest);
    }

    public void deleteFilesInMoviesFolder() {
        String moviesFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getPath();
        File moviesFolder = new File(moviesFolderPath);

        if (moviesFolder.isDirectory()) {
            File[] files = moviesFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        if (file.delete()) {
                            Log.d("DeleteFilesActivity", "Arquivo excluído: " + file.getName());
                        } else {
                            Log.e("DeleteFilesActivity", "Falha ao excluir arquivo: " + file.getName());
                        }
                    }
                }
            }
        }
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

    private void downloadVideo(String fileId, String videoTitle) {
        Log.i(TAG, "Download video iniciado");
            try{
                Toast.makeText(DownloadVideoActivity.this, "Baixando: "+videoTitle, Toast.LENGTH_SHORT).show();
                String videoUrl = BASE_URL+fileId;

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(videoUrl));
                request.setTitle(videoTitle);
                request.setDescription("Baixando Video");


                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, videoTitle +".mp4");
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

                request.allowScanningByMediaScanner();
                request.setAllowedOverMetered(true);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setAllowedOverRoaming(true);


                downloadManager.enqueue(request);
                progresso.setText("Fazendo download de: " + (downloadCount+1)+"/"+listDeUrls.size());
            } catch (IllegalArgumentException e){
                Toast.makeText(DownloadVideoActivity.this, "Algo deu errado!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Line no: 455,Method: downloadFile: Download link is broken");
            }


    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            long receivedID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (receivedID != -1) {

                downloadCount++;
                if(downloadCount< listDeUrls.size()){
                    Log.i(TAG, "Download video concluido.");
                    downloadVideo(listDeUrls.get(downloadCount).getFileId(), listDeUrls.get(downloadCount).getName());
                }
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
