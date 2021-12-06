package com.example.banksalad;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class addList extends AppCompatActivity {
    TextView record_exercise;
    private EditText exercise_name, exercise_cnt, exercise_set;
    String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // 날짜 선택
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        final TextView Datetext = (TextView)findViewById(R.id.input_date);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Datetext.setText(year + "/" + (month+1) + "/" + dayOfMonth);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
        ////////////

        record_exercise = (TextView) findViewById(R.id.exercise_list);

        GetDataExercise task=new GetDataExercise();
        task.execute();
    }

    public void mOnClickfood(View v){ // 음식 추가 버튼
        Intent intent = new Intent(getApplicationContext(), addFood.class);
        startActivity(intent);
    }

    public void mOnClickexercise(View v){ //운동 추가 버튼
        Intent intent = new Intent(getApplicationContext(), addExercise.class);
        startActivity(intent);
    }

    public void mOnClickDate(View v){ // 날짜 선택 버튼
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        final TextView Datetext = (TextView)findViewById(R.id.input_date);
        final Button btn = (Button)findViewById(R.id.date_btn);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Datetext.setText(year + "/" + (month+1) + "/" + dayOfMonth);
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

    private class GetDataExercise extends AsyncTask<Void,Void,String> { // 운동 데이터 SELECT
        String errorString=null;
        String target;

        @Override
        protected void onPreExecute() {
            target="http://10.0.2.2/calsports_select.php";
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
                    JSONArray results=jsonObject.getJSONArray("response");
                    String inp = "- ";
                    for(int i=0;i<results.length();++i){
                        JSONObject temp=results.getJSONObject(i);
                        inp += temp.getString("cal_sport_name")+"/";
                        inp+=temp.getString("cal_sport_cnt")+"/";
                        inp+=temp.getString("cal_sport_set")+", ";
                    }
                    record_exercise.setText(inp);
                }
            }catch (JSONException e){
                Toast.makeText(getApplicationContext(), "오류", Toast.LENGTH_SHORT).show();
                //Log.d(TAG,"POST 에러: "+e);
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));

                String line;
                StringBuilder sb = new StringBuilder();

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return sb.toString().trim();

            }catch (Exception e){
                //Log.d(TAG,"InsertData: Error ",e);
                errorString=e.toString();
            }
            return null;
        }
    }
}


