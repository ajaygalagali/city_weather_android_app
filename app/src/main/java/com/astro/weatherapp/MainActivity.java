package com.astro.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    Button button;
    TextView tempt;



    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";

            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e) {
                Log.i("FFFFFFFFFFFF", String.valueOf(e));
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Log.i("JSON",s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                String wetInfo = jsonObject.getString("weather");
                String tempInfo = jsonObject.getString("main");
                JSONObject tempObject = new JSONObject(tempInfo);
//                Log.i("temp",tempObject.getString("temp"));
                tempt.setText(tempObject.getString("temp")+" °C");
                JSONArray array = new JSONArray(wetInfo);


                for(int i=0;i<array.length();i++){
                    JSONObject part = array.getJSONObject(i);
//                    Log.i("In for loop", part.getString("main"));
                    textView.setText(part.getString("main"));
                }

//                for(int j=0;j<jsonArray.length();j++){
//                    JSONObject tempPart = jsonArray.getJSONObject(j);
//                    Log.i("Temprature",tempPart.getString("temp"));
////                    tempt.setText(tempPart.getString("temp"));
//                }



            } catch (Exception e) {
                e.printStackTrace();
                textView.setText("Sorry!");
                tempt.setText("City Not Found in Our Database");
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        tempt = findViewById(R.id.textView2);



    }
    public void getClicked(View view) {

        DownloadTask downloadTask = new DownloadTask();
        String cityName = String.valueOf(editText.getText());
//        Log.i("City Name",cityName);
        downloadTask.execute("https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&units=metric&appid=YOUR_API_KEY");
    }
}
