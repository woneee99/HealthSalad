package com.example.banksalad;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class addFood extends AppCompatActivity {
    private Button button;
    private EditText food_name, food_weight;
    String food_date, temp;
    String[] temp_date;
    int state = 0; // 라디오 버튼 상태
    private String TAG="태그:";

    Map<String, String> map= new HashMap<String, String>();
    String mJsonString;

    String cal_food_userId;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        cal_food_userId = userID; // userID 받아오기
        // cal_food_userId = "yys";

        button = findViewById(R.id.ok_food);
        temp = intent.getStringExtra("datetext");
        temp_date = temp.split("/"); // 2021/11/29 자르기
        food_date = "" + temp_date[0];
        food_date += (Integer.parseInt(temp_date[1]) < 10) ? "0" + temp_date[1] : temp_date[1];
        food_date += (Integer.parseInt(temp_date[2]) < 10) ? "0" + temp_date[2] : temp_date[2];
        //food_date = temp_date[0]+temp_date[1]+temp_date[2]; // 20211129로 넣기
        food_name = (EditText)findViewById(R.id.dlgName);
        food_weight = (EditText)findViewById(R.id.dlgweight);

        GetDataKcal task = new GetDataKcal();
        task.execute();

        RadioGroup radiogroup = (RadioGroup)findViewById(R.id.radioGroup);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.morning_rd) {
                    state=1;
                } else if (checkedId == R.id.lunch_rd) {
                    state=2;
                } else {
                    state=3;
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String DATE = food_date;
                String NAME = food_name.getText().toString();
                String WEIGHT = food_weight.getText().toString();
                String USERID = cal_food_userId;

                String INFO = NAME+" "+WEIGHT+"g";
                String BREAKFAST, LUNCH, DINNER, KCAL;

                if(state==1){
                    BREAKFAST=INFO;
                    LUNCH="";
                    DINNER="";
                }else if(state==2){
                    BREAKFAST="";
                    LUNCH=INFO;
                    DINNER="";
                }else{
                    BREAKFAST="";
                    LUNCH="";
                    DINNER=INFO;
                }
                if(map.containsKey(NAME)==true){
                    Double num = Double.parseDouble(map.get(NAME)) * Double.parseDouble(WEIGHT);
                    KCAL = num.toString();
                } else{ KCAL="130"; }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jasonObject = new JSONObject(response);
                            boolean success=jasonObject.getBoolean("success");
                            if (success) {
                                String cal_food_Name = jasonObject.optString("cal_food_name","");
                                String cal_food_Weight = jasonObject.optString("cal_food_weight","");

                                Toast.makeText(getApplicationContext(), "기록 완료", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(addFood.this, addList.class);
                                intent.putExtra("cal_food_date", temp);
                                startActivity(intent);
                                finish();
                            }
                            else{ //기록 실패한 경우
                                Toast.makeText(getApplicationContext(), "기록 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                FoodInsertRequest foodRequest = new FoodInsertRequest(DATE, BREAKFAST, LUNCH, DINNER, KCAL, USERID, responseListener);
                RequestQueue queue1 = Volley.newRequestQueue(addFood.this);
                queue1.add(foodRequest);
            }
        });
    }

    private class GetDataKcal extends AsyncTask<Void, Void, String> { // 칼로리 저장
        String errorString = null;
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://10.0.2.2:8012/foodKcal.php";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                if (s == null) {
                    //textview("X");
                } else {
                    mJsonString = s;

                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray results = jsonObject.getJSONArray("response");

                    for (int i = 0; i < results.length(); ++i) {
                        JSONObject temp = results.getJSONObject(i);
                        map.put(temp.getString("cal_food_name"), temp.getString("cal_food_kcal"));
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "오류", Toast.LENGTH_SHORT).show();
                //Log.d(TAG,"POST 에러: "+e);
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                StringBuilder sb = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return sb.toString().trim();

            } catch (Exception e) {
                //Log.d(TAG,"InsertData: Error ",e);
                errorString = e.toString();
            }
            return null;
        }
    }
}