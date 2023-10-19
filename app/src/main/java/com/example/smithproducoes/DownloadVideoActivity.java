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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    private EditText Urllist;
    private Button downloadButton;
    private static final String TAG = "DownloadVideoActivity";

    private DownloadManager downloadManager;
    private long downloadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_video);

        isPermissionGranted();

        downloadButton = findViewById(R.id.button);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //https://drive.google.com/file/d/1bRVktPCrRZA19OzDHB8RfYxynDbRslGN/view?usp=sharing
                //https://drive.google.com/file/d/1Mm56njT0dQ3mZzRaAGsZHw9lGAtXbMEF/view?usp=sharing
                String videoUrl = "https://drive.google.com/uc?export=download&id=1Mm56njT0dQ3mZzRaAGsZHw9lGAtXbMEF";
                //String videoUrl = "https://drive.google.com/uc?export=download&id=1bRVktPCrRZA19OzDHB8RfYxynDbRslGN";
                String videoTitle = "video";

                downloadVideo(DownloadVideoActivity.this, videoUrl, videoTitle);
            }
        });
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


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
            downloadID = manager.enqueue(request);
        } catch (IllegalArgumentException e){
            Toast.makeText(DownloadVideoActivity.this, "Algo deu errado!", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Line no: 455,Method: downloadFile: Download link is broken");
        }



        Log.i(TAG, "Download video concluido.");
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            long receivedID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (receivedID == downloadID) {
                // O download foi concluído
                Toast.makeText(DownloadVideoActivity.this, "Download concluído!", Toast.LENGTH_SHORT).show();
                Intent newActivityIntent = new Intent(DownloadVideoActivity.this, MainActivity.class);
                startActivity(newActivityIntent);
            }
        }
    };


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
    private class DownloadVideoTask2 extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            return null;
        }
    }
}


            // Inicialize o DownloadManager
//        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//
//        // Defina os detalhes do download
//        Uri videoUri = Uri.parse("URL_DO_VÍDEO_AQUI"); // Substitua pelo URL do vídeo que deseja baixar
//        DownloadManager.Request request = new DownloadManager.Request(videoUri);
//        request.setTitle("Nome do Vídeo"); // Nome do arquivo a ser exibido no gerenciador de downloads
//        request.setDescription("Baixando vídeo...");
//
//        // Defina o diretório de destino para "Movies"
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, "nome_do_video.mp4");
//
//        // Inicie o download e capture o ID do download
//        downloadId = downloadManager.enqueue(request);
//
//        // Registre um receptor de transmissão para monitorar o progresso do download
//        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


//    private BroadcastReceiver onComplete = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // Verifique se o download foi bem-sucedido
//            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//            if (id == downloadId) {
//                // O download foi concluído com sucesso
//                Toast.makeText(DownloadVideoActivity.this, "Download concluído", Toast.LENGTH_SHORT).show();
//            }
//        }
    //};

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // Desregistre o receptor de transmissão para evitar vazamentos de memória
//        unregisterReceiver(onComplete);
//    }




//        Urllist = findViewById(R.id.URLInput);
//
//        downloadButton = findViewById(R.id.button);
//        downloadButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Inicia a tarefa de download em segundo plano
//                new DownloadVideoTask2().execute();
//            }
//        });
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // A permissão foi concedida, você pode acessar o armazenamento agora
//                Toast.makeText(DownloadVideoActivity.this, "Permissão concedida!", Toast.LENGTH_SHORT).show();
//            } else {
//                // A permissão foi negada, informe ao usuário que a funcionalidade não está disponível
//                Toast.makeText(DownloadVideoActivity.this, "Permissão negada!", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }



    //isso aqui funciona só pra baixar a lista JSON
//    private class DownloadJsonTask extends AsyncTask<Void, Void, Boolean> {
//
//        @Override
//        protected Boolean doInBackground(Void... voids) {
//            OkHttpClient client = new OkHttpClient();
//
//            String fileId = "1j53hOMduSy7i1I-QTS1vmx0ktjLKAVHd"; // Substitua pelo ID do arquivo
//            //String url = "https://drive.google.com/uc?export=download&id=" + fileId;
//
//            String url = "https://drive.google.com/uc?export=download&id=1Mm56njT0dQ3mZzRaAGsZHw9lGAtXbMEF";


//
//            //String url = "https://drive.google.com/file/d/1j53hOMduSy7i1I-QTS1vmx0ktjLKAVHd"; // Substitua pelo URL do arquivo JSON que deseja baixar
//            Log.e("DownloadJsonTask", "Baixando: " + url);
//            try {
//                Request request = new Request.Builder()
//                        .url(url)
//                        .build();
//
//                Response response = client.newCall(request).execute();
//
//                Log.e("DownloadJsonTask", "response: " + response);
//
//
//                if (response.isSuccessful()) {
//                    String json = response.body().string();
//                    File jsonFile = new File(Environment.getExternalStorageDirectory(), "Movies/backinblack.mp4"); // Substitua o nome do arquivo e local de salvamento conforme necessário
//
//                    try (FileOutputStream fos = new FileOutputStream(jsonFile)) {
//                        fos.write(json.getBytes());
//                    }
//
//                    return true;
//                } else {
//                    Log.e("DownloadJsonTask", "Erro no download do JSON: " + response.code());
//                }
//            } catch (IOException e) {
//                Log.e("DownloadJsonTask", "Exceção ao baixar JSON: " + e.getMessage());
//            }
//
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            if (result) {
//                Toast.makeText(DownloadVideoActivity.this, "Arquivo JSON baixado com sucesso!", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(DownloadVideoActivity.this, "Erro ao baixar o arquivo JSON", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

//    private class DownloadTask extends AsyncTask<Void, Void, Boolean> {
//
//        @Override
//        protected Boolean doInBackground(Void... voids) {
//            // Coloque seu código de download de vídeos aqui
//            // Certifique-se de solicitar as permissões de Internet e Armazenamento
//
//            try {
//                // Simula um download
//                Thread.sleep(3000);
//                return true;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            if (result) {
//                // Se o download for bem-sucedido, inicie a próxima atividade
//                Intent intent = new Intent(DownloadVideoActivity.this, MainActivity.class);
//                startActivity(intent);
//            } else {
//                // Caso contrário, lide com o erro
//                // Exemplo: exibir uma mensagem de erro
//                // Toast.makeText(MainActivity.this, "Erro no download", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

//    private class DownloadVideoTask2 extends AsyncTask<Void, Void, Boolean>{
//
//        @Override
//        protected Boolean doInBackground(Void... voids) {
//            HttpClient httpClient = HttpClients.createDefault();
//            //https://drive.google.com/file/d/0BzgEOg19a3NNRk9Yc21BbTNzNlU/view?usp=share_link&resourcekey=0-OEVDCj5qgDBKTrC8ZswH3A
//            HttpGet httpGet = new HttpGet("https://drive.google.com/uc?export=download&id=0BzgEOg19a3NNRk9Yc21BbTNzNlU");
//
//            try {
//                InputStream inputStream = httpClient.execute(httpGet).getEntity().getContent();
//                File videoFile = new File(Environment.getExternalStorageDirectory(), "Movies/backinblack.mp4");
//                try (OutputStream outputStream = new FileOutputStream(videoFile)) {
//                    byte[] buffer = new byte[1024];
//                    int bytesRead;
//                    while ((bytesRead = inputStream.read(buffer)) != -1) {
//                        outputStream.write(buffer, 0, bytesRead);
//                    }
//                }
//            } catch (ClientProtocolException e) {
//                throw new RuntimeException(e);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } finally {
//                httpGet.releaseConnection();
//            }
//            return false;
//        }
//        @Override
//        protected void onPostExecute(Boolean result) {
//            if (result) {
//                // Se o download for bem-sucedido, inicie a próxima atividade
//                Intent intent = new Intent(DownloadVideoActivity.this, MainActivity.class);
//                startActivity(intent);
//            } else {
//                // Caso contrário, lide com o erro
//                // Exemplo: exibir uma mensagem de erro
//                // Toast.makeText(MainActivity.this, "Erro no download", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
