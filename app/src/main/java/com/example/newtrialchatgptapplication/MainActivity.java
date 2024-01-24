package com.example.newtrialchatgptapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView generate,answer;
    EditText question;
   

    String stringUrl= "https://api.openai.com/v1/chat/completions";
    //String apiKey ="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";
    String stringOutput ="";
    public static final MediaType Json =
            MediaType.get("application/json; charset=utf-8");

    OkHttpClient client =new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).build();

    private double requestTime = 0.0;
    private long readTimeout = 0;
    private long writeTimeout = 0;
    private long connectTimeout = 0;





    // ... rest of your class



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generate=findViewById(R.id.generate);
        answer=findViewById(R.id.answer);
        question=findViewById(R.id.question);

        generate.setOnClickListener(v->{

            String ask =question.getText().toString().trim();

            triggerGpt(ask);

        });

    }

    private void triggerGpt(String question) {

        JSONObject jsonBody =new JSONObject();
        JSONArray jsonArrayMessage = new JSONArray();
        JSONObject obj =new JSONObject();
        try {
            jsonBody.put("model","gpt-3.5-turbo");

            obj.put("role","user");
            obj.put("content",question);
            jsonArrayMessage.put(obj);

            jsonBody.put("messages",jsonArrayMessage);
            jsonBody.put("temperature", 0.0);
            
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        RequestBody body =RequestBody.create(jsonBody.toString(),Json);
        Request request =new Request.Builder()
                .url(stringUrl)
               // .header("Content-Type","application/json")
                .header("Content-Type", "application/json")
                .header("Authorization","Bearer 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")
                .post(body)
                .build();
        

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //   Toast.makeText(getApplicationContext(),"failed answering due to "+e.getMessage(),Toast.LENGTH_LONG).show();
              
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()){
                    

                    JSONObject jsonObject =null;
                    try {
                        jsonObject =new JSONObject(response.body().string());
                        JSONArray jsonArray =jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");



                        answer.setText(result);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
                else {
                    //  Toast.makeText(getApplicationContext(),"failed answering due to "+response.body().toString(),Toast.LENGTH_LONG).show();
                   

                }

            }
        });


    }
}
