package com.example.smithproducoes;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VideoDownloader {
    public static void main(String[] args) throws JSONException {

        String URL = "https://drive.google.com/file/d/1j53hOMduSy7i1I-QTS1vmx0ktjLKAVHd/view?usp=sharing";
        String jsonFilePath = "Caminho_Para_Arquivo_JSON";
        JSONArray videos = readJsonFile(jsonFilePath);

        if (videos != null) {
            for (int i = 0; i < videos.length(); i++) {
                JSONObject videoInfo = videos.getJSONObject(i);
                String videoUrl = videoInfo.getString("url");
                String outputPath = videoInfo.getString("outputPath");

                try {
                    downloadVideo(videoUrl, outputPath);
                    System.out.println("Vídeo baixado com sucesso: " + outputPath);
                } catch (IOException e) {
                    System.err.println("Erro ao baixar o vídeo " + outputPath + ": " + e.getMessage());
                }
            }
        }
    }

    public static JSONArray readJsonFile(String jsonFilePath) {
        try {
            String jsonContent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)), StandardCharsets.UTF_8);
            }
            return new JSONArray(jsonContent);
        } catch (IOException | JSONException e) {
            System.err.println("Erro ao ler o arquivo JSON: " + e.getMessage());
            return null;
        }
    }

    public static void downloadVideo(String videoUrl, String outputPath) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(videoUrl);

        try {
            InputStream inputStream = httpClient.execute(httpGet).getEntity().getContent();
            try (OutputStream outputStream = new FileOutputStream(outputPath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } finally {
            httpGet.releaseConnection();
        }
    }
}
