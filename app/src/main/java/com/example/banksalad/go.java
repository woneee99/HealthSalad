package com.example.banksalad;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.banksalad.fragment.fragCal;
import com.example.banksalad.fragment.fragPlan;
import com.example.banksalad.fragment.fragUser;
import com.example.banksalad.fragment.fragWatch;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class go extends AppCompatActivity {
    private static final String TAG = "";

    String mJsonString;
    BottomNavigationView bottomNavigationView;
    Menu menu;
    Fragment fragCal;
    Fragment fragUser;
    Fragment fragWatch;
    Fragment fragPlan;

    String userID;
    String userName;
    String userBirth;
    String userHeight;
    String userWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        Bundle bundle1 = new Bundle();
        bundle1.putString("userID",userID);
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        Fragment fragment2 = new fragPlan();
        fragment2.setArguments(bundle1);
        transaction1.replace(R.id.frame_container, fragment2);
        transaction1.commit();

        fragPlan = new fragPlan();
        fragCal = new fragCal();
        fragUser = new fragUser();
        fragWatch = new fragWatch();

        GetDataUser userTask = new GetDataUser();
        userTask.execute(userID);

        bottomNavigationView = findViewById(R.id.navigation);
        //첫화면
        bottomNavigationView.setSelectedItemId(R.id.plan);

        //getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragPlan).commitAllowingStateLoss();

        //case 함수를 통해 클릭 받을 때마다 화면 변경하기
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.cal :
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("userID",userID);
                        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                        Fragment fragment2 = new fragCal();
                        fragment2.setArguments(bundle1);
                        transaction1.replace(R.id.frame_container, fragment2);
                        transaction1.commit();
                        break;
                    case R.id.user:
                        Bundle bundle = new Bundle();
                        bundle.putString("userName",userName);
                        bundle.putString("userBirth",userBirth);
                        bundle.putString("userHeight",userHeight);
                        bundle.putString("userWeight",userWeight);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        Fragment fragment1 = new fragUser();
                        fragment1.setArguments(bundle);
                        transaction.replace(R.id.frame_container, fragment1);
                        transaction.commit();
                        break;
                    case R.id.plan:
                        Bundle bundle3 = new Bundle();
                        bundle3.putString("userID",userID);
                        FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                        fragPlan.setArguments(bundle3);
                        transaction3.replace(R.id.frame_container, fragPlan);
                        transaction3.commit();
                        break;
                    case R.id.watch:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new fragWatch()).commit();
                        break;
                }
                return true;
            }
        });

    }

    private class GetDataUser extends AsyncTask<String, Void, String> {
        String errorString = null;
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://10.0.2.2/plan_info.php";
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
                        if (userID.equals(temp.getString("userID"))) {
                            userName = temp.getString("userName");
                            userWeight = temp.getString("userWeight");
                            userBirth = temp.getString("userBirth");
                            userHeight = temp.getString("userHeight");
                            break;
                        }
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "오류", Toast.LENGTH_SHORT).show();
                //Log.d(TAG,"POST 에러: "+e);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String id=params[0];
            String postParam="userID="+id;

            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream=httpURLConnection.getOutputStream();
                outputStream.write(postParam.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode=httpURLConnection.getResponseCode();
                Log.d(TAG,"response code - "+responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line;//temp가 line임
                StringBuilder sb = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
            }
            return null;
        }
    }
}