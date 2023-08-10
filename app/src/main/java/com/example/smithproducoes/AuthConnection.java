//package com.example.smithproducoes;
//
//import android.accounts.AccountManager;
//import android.accounts.AccountManagerCallback;
//import android.accounts.AccountManagerFuture;
//import android.accounts.AuthenticatorException;
//import android.accounts.OperationCanceledException;
//import android.content.Context;
//import android.os.Bundle;
//
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLConnection;
//
//public class AuthConnection{
//    public void getToken(){
//        AccountManager am = AccountManager.get(this);
//        Bundle options = new Bundle();
//
//        am.getAuthToken(
//                myAccount_,                     // Account retrieved using getAccountsByType()
//                "Manage your tasks",            // Auth scope
//                options,                        // Authenticator-specific options
//                this,                           // Your activity
//                new OnTokenAcquired(),          // Callback called when a token is successfully acquired
//                new Handler(new OnError()));    // Callback called if an error occurs
//
//    }
//    private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
//        @Override
//        public void run(AccountManagerFuture<Bundle> result) {
//            // Get the result of the operation from the AccountManagerFuture.
//            Bundle bundle = null;
//            try {
//                bundle = result.getResult();
//            } catch (AuthenticatorException e) {
//                throw new RuntimeException(e);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } catch (OperationCanceledException e) {
//                throw new RuntimeException(e);
//            }
//
//            // The token is a named value in the bundle. The name of the value
//            // is stored in the constant AccountManager.KEY_AUTHTOKEN.
//            String token = bundle.getString(AccountManager.KEY_AUTHTOKEN)
//            Intent launch = (Intent) result.getResult().get(AccountManager.KEY_INTENT);
//            if (launch != null) {
//                startActivityForResult(launch, 0);
//                return;
//            }
//        }
//    }
//    URL url = new URL("https://www.googleapis.com/tasks/v1/users/@me/lists?key=" + your_api_key);
//    URLConnection conn = (HttpURLConnection) url.openConnection();
//    conn.addRequestProperty("client_id", your client id);
//    conn.addRequestProperty("client_secret", your client secret);
//    conn.setRequestProperty("Authorization", "OAuth " + token);
//
//
//
//
//
//
//}
