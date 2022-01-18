package com.example.weatherapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    ImageView imageView;
    TextView country_yt,city_yt,temp_yt,latitude,longitude,humidity,sunrise,sunset,pressure,windspped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.editTextTextPersonName);
        button=(Button) findViewById(R.id.button);
        country_yt =findViewById(R.id.country);
        city_yt=findViewById(R.id.city);
        temp_yt=findViewById(R.id.temperature);
        imageView=findViewById(R.id.imageView);

        latitude =findViewById(R.id.latitude);
        longitude=findViewById(R.id.longitude);

        humidity=findViewById(R.id.humidity);
        sunrise=findViewById(R.id.sunrise);
        sunset=findViewById(R.id.sunset);
        pressure=findViewById(R.id.pressure);
        windspped=findViewById(R.id.windspped);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findWeather();
            }
        });
    }
    public  void findWeather(){
        String city=editText.getText().toString();
        String url="https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=3d3f42e506779f8eaa21766145257c14";

        StringRequest stringrequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject= new JSONObject(response);


                    //find country
                    JSONObject object1= jsonObject.getJSONObject("sys");
                    String country_find= object1.getString("country");
                    country_yt.setText(country_find);

                    //find city
                    String city_find=jsonObject.getString("name");
                    city_yt.setText(city_find);

                    //find temperature
                    JSONObject object2= jsonObject.getJSONObject("main");
                    double temp_find =object2.getDouble("temp");
                    double temperature= temp_find-273;

                    final DecimalFormat df = new DecimalFormat("0.00");
                    String temperature1=df.format(temperature);
                    temp_yt.setText(temperature1+" °C");

                    //get image Icon
                    JSONArray jsonArray= jsonObject.getJSONArray("weather");
                    JSONObject obj= jsonArray.getJSONObject(0);
                    String img=obj.getString("icon");
                    Picasso.get().load("https://openweathermap.org/img/wn/"+img+".png").into(imageView);

                    //find latitude
                    JSONObject object3= jsonObject.getJSONObject("coord");
                    double lat_find=object3.getDouble("lat");
                    latitude.setText(String.valueOf(lat_find+" °N"));
                    //find longitude
                    double long_find=object3.getDouble("lon");
                    longitude.setText(String.valueOf(long_find+" °E"));
                    //find humidity "object2 from main"
                    double hum_find=object2.getDouble("humidity");
                    humidity.setText(String.valueOf(hum_find+" %"));


                    //find sunrise
                    double find_sunrise= object1.getDouble("sunrise");
                    SimpleDateFormat HMM = new SimpleDateFormat("hh:mm");
                    Date date = new Date((long) (find_sunrise * 1000));
                    String prettyFormatted = HMM.format(date);
                    sunrise.setText(prettyFormatted+" AM");

                    //find sunset
                    double find_sunset= object1.getDouble("sunset");
                    Date date2 = new Date((long) (find_sunset * 1000));
                    String prettyFormatted2 = HMM.format(date2);
                    sunset.setText(prettyFormatted2+" PM");

                    //find pressure---main
                    String find_pressure= object2.getString("pressure");
                    pressure.setText(find_pressure+" hpa");

                    //find wind
                    JSONObject object4= jsonObject.getJSONObject("wind");
                    String find_windspeed= object4.getString("speed");
                    windspped.setText(find_windspeed+" kh/m");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringrequest);
    }
}