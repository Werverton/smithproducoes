package com.example.smithproducoes.core;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import org.json.JSONObject;


public class UpdateCheckJobService extends JobService {
    private static final String VERSION_KEY = "version_key";
    private static final String SHARED_PREFS_KEY = "MyPrefs";
    SharedPreferences sharedPreferences;


    @Override
    public boolean onStartJob(JobParameters params) {
        // Tarefa a ser executada em segundo plano
        new UpdateCheckTask(params).execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // Se a tarefa for interrompida, você pode retornar true para reagendá-la.
        return true;
    }

    private class UpdateCheckTask extends AsyncTask<Void, Void, Boolean> {
        private final JobParameters jobParameters;

        UpdateCheckTask(JobParameters jobParameters) {
            this.jobParameters = jobParameters;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);


            // Realize uma solicitação HTTP para a URL do JSON e verifique o campo "version"
            // Compare com a versão armazenada no banco de dados ou SharedPreferences
            String latestVersion = ""; // Você precisa buscar a versão mais recente
            //String storedVersion = DataRepository.retrieveVersion(); // Implemente esse método
            String storedVersion = sharedPreferences.getString(VERSION_KEY, "");

            if (latestVersion != null && !latestVersion.equals(storedVersion)) {
                // A versão foi alterada, realize as ações necessárias aqui
                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean needsReschedule) {
            jobFinished(jobParameters, needsReschedule);
        }
    }
}
