package com.example.comtest.transportactiveageing;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Login extends Activity {

    EditText username;
    EditText password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = (EditText) findViewById(R.id.usernameLogin);
        password = (EditText) findViewById(R.id.passwordLogin);
        login = (Button) findViewById(R.id.loginButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.clearFocus();
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );

                String userNameString = username.getText().toString();
                String passwordString = password.getText().toString();
                LoginAsync la = new LoginAsync(userNameString, passwordString);
                la.execute("https://activeageing.se/resources/authentication");
            }
        });
    }

    class LoginAsync extends AsyncTask<String, String, String> {

        String user;
        String pass;

        private LoginAsync(String username, String password) {
            user = username;
            pass = password;
        }

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {

                JSONObject login = new JSONObject();
                login.put("userName", user);
                login.put("password", pass);
                String jsonData = login.toString();

                HttpPost post = new HttpPost(uri[0]);
                post.setHeader("Content-Type", "application/json");
                post.setHeader("Accept", "application/json");
                post.setEntity(new StringEntity(jsonData));
                response = httpclient.execute(post);
                StatusLine statusLine = response.getStatusLine();

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String resp = out.toString();
                System.out.println("Resp: " + resp);

                if (statusLine.getStatusCode() == 200) {
                    System.out.println("VALID");
                    responseString = "valid";
                } else {
                    System.out.println("INVALID");
                    responseString = "invalid";
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String response = result;

            if (response.equals("valid")) {
                Intent intent = new Intent(Login.this, MainView.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Login wrong. Try again!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}