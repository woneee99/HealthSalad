package com.example.banksalad;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.banksalad.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class addList extends AppCompatActivity {
    private static final String TAG = "날짜:";
    TextView record_exercise;
    TextView record_breakfast, record_lunch, record_dinner;
    TextView record_breakfast_kcal, record_lunch_kcal, record_dinner_kcal;
    TextView Datetext;
    String mJsonString;

    String temp_str = "";
    String date = "";
    String[] temp_date;
    String food_data = "";
    Double inp1, inp2, inp3;

    String cal_sport_userId;
    String cal_food_userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Intent intent = getIntent();
        date = intent.getStringExtra("cal_sport_date");
        food_data = intent.getStringExtra("cal_food_date");

        Datetext = (TextView) findViewById(R.id.input_date);
        record_exercise = (TextView) findViewById(R.id.exercise_list);

        record_breakfast = (TextView) findViewById(R.id.breakfast_list);
        record_lunch = (TextView) findViewById(R.id.lunch_list);
        record_dinner = (TextView) findViewById(R.id.dinner_list);

        record_breakfast_kcal = (TextView) findViewById(R.id.breakfast_kcal);
        record_lunch_kcal = (TextView) findViewById(R.id.lunch_kcal);
        record_dinner_kcal = (TextView) findViewById(R.id.dinner_kcal);

        cal_sport_userId="qqq";
        cal_food_userId="qqq";

        if (date != null || food_data != null) {
            if (date != null) {
                String date_s[] = date.split("/");
                temp_str = date_s[0] + date_s[1] + date_s[2];
            } else if (food_data != null) {
                String date_s[] = food_data.split("/");
                temp_str = date_s[0] + date_s[1] + date_s[2];
            }

            GetDataExercise task = new GetDataExercise();
            task.execute(cal_sport_userId);

            GetDataFood foodTask = new GetDataFood();
            foodTask.execute(cal_food_userId);

            if (date != null) {
                Datetext.setText(date);
            } else if (food_data != null) {
                Datetext.setText(food_data);
            }
        } else {
            // 날짜 선택
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Datetext.setText(year + "/" + (month + 1) + "/" + dayOfMonth);

                    temp_str = "";
                    temp_str += year;
//                temp_str += (month + 1);
//                temp_str += dayOfMonth;
                    temp_str += ((month + 1) < 10) ? "0" + (month + 1) : (month + 1);
                    temp_str += (dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth;

                    GetDataExercise task = new GetDataExercise();
                    task.execute(cal_sport_userId);

                    GetDataFood foodTask = new GetDataFood();
                    foodTask.execute(cal_food_userId);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
            ////////////
        }
    }

    public void mOnClickfood(View v) { // 음식 추가 버튼
        Intent intent = new Intent(getApplicationContext(), addFood.class);
        intent.putExtra("datetext", Datetext.getText()); //날짜 넘기기
        startActivity(intent);
    }

    public void mOnClickexercise(View v) { //운동 추가 버튼
        Intent intent = new Intent(getApplicationContext(), addExercise.class);
        intent.putExtra("datetext", Datetext.getText()); //날짜 넘기기
        startActivity(intent);
    }

    public void mOnClickDate(View v) { // 날짜 선택 버튼
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        final TextView Datetext = (TextView) findViewById(R.id.input_date);
        final Button btn = (Button) findViewById(R.id.date_btn);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Datetext.setText(year + "/" + (month + 1) + "/" + dayOfMonth);

                temp_str = "";
                temp_str += year;
//                temp_str += (month + 1);
//                temp_str += dayOfMonth;
                temp_str += ((month + 1) < 10) ? "0" + (month + 1) : (month + 1);
                temp_str += (dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth;

                GetDataExercise task = new GetDataExercise();
                task.execute(cal_sport_userId);

                GetDataFood foodTask = new GetDataFood();
                foodTask.execute(cal_food_userId);
            }
        }, mYear, mMonth, mDay);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn.isClickable()) {
                    datePickerDialog.show();
                }
            }
        });
    }

    private class GetDataExercise extends AsyncTask<String, Void, String> { // 운동 데이터 SELECT
        String errorString = null;
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://10.0.2.2/calsports_select.php";
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

                    String inp = "- ";

                    for (int i = 0; i < results.length(); ++i) {
                        JSONObject temp = results.getJSONObject(i);
                        if (temp_str.equals(temp.getString("cal_sport_date"))) {
                            inp += temp.getString("cal_sport_name") + "/";
                            inp += temp.getString("cal_sport_cnt") + "/";
                            inp += temp.getString("cal_sport_set") + ", ";
                        }
                    }
                    record_exercise.setText(inp);
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "오류", Toast.LENGTH_SHORT).show();
                //Log.d(TAG,"POST 에러: "+e);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String id=params[0];
            String postParam="cal_sport_userID="+id;

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

    private class GetDataFood extends AsyncTask<String, Void, String> { // 음식 데이터 SELECT
        String errorString = null;
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://10.0.2.2/calfood_select.php";
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

                    String inp_breakfast = "- ";
                    String inp_lunch = "- ";
                    String inp_dinner = "- ";

                    String inp_breakfast2 = "0";
                    String inp_lunch2 = "0";
                    String inp_dinner2 = "0";
                    //Double inp1, inp2, inp3;

                    for (int i = 0; i < results.length(); ++i) {
                        JSONObject temp = results.getJSONObject(i);
                        if (temp_str.equals(temp.getString("cal_food_date"))) {
                            if (!temp.getString("cal_food_breakfast").equals("")) {
                                inp_breakfast += temp.getString("cal_food_breakfast") + ", ";
                                inp1 = Double.parseDouble(inp_breakfast2) + Double.parseDouble(temp.getString("cal_food_kcal"));
                                inp_breakfast2 = inp1.toString();
                            }
                            if (!temp.getString("cal_food_lunch").equals("")) {
                                inp_lunch += temp.getString("cal_food_lunch") + ", ";
                                inp2 = Double.parseDouble(inp_lunch2) + Double.parseDouble(temp.getString("cal_food_kcal"));
                                inp_lunch2 = inp2.toString();
                            }
                            if (!temp.getString("cal_food_dinner").equals("")) {
                                inp_dinner += temp.getString("cal_food_dinner") + ", ";
                                inp3 = Double.parseDouble(inp_dinner2) + Double.parseDouble(temp.getString("cal_food_kcal"));
                                inp_dinner2 = inp3.toString();
                            }
                        }
                    }
                    record_breakfast.setText(inp_breakfast);
                    record_lunch.setText(inp_lunch);
                    record_dinner.setText(inp_dinner);

                    Log.d(TAG,"추가"+inp_breakfast+inp_lunch+inp_dinner);

                    record_breakfast_kcal.setText(": "+inp_breakfast2+"kcal");
                    record_lunch_kcal.setText(": "+inp_lunch2+"kcal");
                    record_dinner_kcal.setText(": "+inp_dinner2+"kcal");
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Food 오류", Toast.LENGTH_SHORT).show();
                //Log.d(TAG,"POST 에러: "+e);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String id=params[0];
            String postParam="cal_food_userID="+id;

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
    public void mOnClickRecord(View v) { // 기록 확인 버튼
        Intent intent = new Intent(addList.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}