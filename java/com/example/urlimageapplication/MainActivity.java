package com.example.urlimageapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
{
    private ImageView _randomImage;
    private Button _button;
    private String _apiUrl = "https://forza-api.tk/";
    private String _imageUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _randomImage = findViewById(R.id.RandomPhoto);
        _button = findViewById(R.id.GetRandomPhotoButton);

        _button.setOnClickListener(view -> new ImageLoader().execute());
    }





    private class ImageLoader extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            String json = getJson(_apiUrl);

            try
            {
                JSONObject obj = new JSONObject(json);
                _imageUrl = obj.getString("image");

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            if(!_imageUrl.equals(""))
            {
                Glide.with(MainActivity.this).load(_imageUrl).into(_randomImage);
                _button.setText("Получить фото");
            }
            else
            {
                Log.e("PostExecute1", "Ошибка");
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            _button.setText("Загрузка");
            _imageUrl = "";
        }


        private String getJson(String link)
        {
            String data = "";

            try
            {
                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                    data = reader.readLine();

                    connection.disconnect();
                }

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return data;
        }


    }
}