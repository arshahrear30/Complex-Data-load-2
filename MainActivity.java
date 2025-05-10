package com.example.complexjson;

import static android.view.View.GONE;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        progressBar =findViewById(R.id.progressBar);

        String url="https://dummyjson.com/products";

        JsonObjectRequest jsonObjectRequest =new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                //যেহেতু শুরু { দিয়ে তাইলে jsonobject
                //JSONObject call করে Response এই ভেরিয়েবল এ রাখছি।
                progressBar.setVisibility(GONE);
                Log.d("serverRes",Response.toString());//Developer view in logcat

                try {
                    JSONArray JSONarrayproducs = Response.getJSONArray("products");
                    //product নামে JSONArray call করে JSONarrayproducs এই ভেরিয়েবল এ রাখছি।
                    JSONObject jsonObject0 = JSONarrayproducs.getJSONObject(0);
                    //JSONarrayproducs থেকে ০ নম্বর JSONObject কে jsonObject0 ধরলাম
                    String titleout=jsonObject0.getString("title");
                    //jsonObject0 থেকে  title কে string আকারে get করলাম এবং titleout নামে ভেরিয়েবল ধরলাম
                    textView.append(titleout);
                    //append আকারে ধরলাম যাতে পূর্বের Data আগের মত রেখে নতুন কিছু
                    //যুক্ত করলে আগেরটুকু মুচবে না । বরং নতুন data যুক্ত হবে /update হবে ।

                    JSONArray imageArray = jsonObject0.getJSONArray("images");
                    for(int x=0;x<imageArray.length();x++){

                        String images =imageArray.getString(x);
                        textView.append("\n"+images);

                    }

                } catch (JSONException e) {
                    progressBar.setVisibility(GONE);
                    textView.setText("JSON parsing error: " + e.getMessage());
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonObjectRequest);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
