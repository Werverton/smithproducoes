package com.example.smithproducoes;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.smithproducoes.model.Video;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class VideoDownloader {
    private static final String TAG = "VideoDownloader";
    List<Video> listDeUrls = new ArrayList<>();
    private int downloadCount = 0;
    private static final String BASE_URL = "https://drive.google.com/uc?export=download&id=";
    private DownloadManager downloadManager;
    private void downloadVideo(String fileId, String videoTitle) {
        if(isVideoAlreadyDownloaded(listDeUrls.get(downloadCount).getName())){
            downloadCount++;
            downloadVideo(listDeUrls.get(downloadCount).getFileId(), listDeUrls.get(downloadCount).getName());
        }
        Log.i(TAG, "Download video iniciado");
        try{
            //Toast.makeText(DownloadVideoActivity.this, "Baixando: "+videoTitle, Toast.LENGTH_SHORT).show();
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
            //progresso.setText("Fazendo download de: " + (downloadCount+1)+"/"+listDeUrls.size());
        } catch (IllegalArgumentException e){
            //Toast.makeText(DownloadVideoActivity.this, "Algo deu errado!", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Line no: 455,Method: downloadFile: Download link is broken");
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
}
