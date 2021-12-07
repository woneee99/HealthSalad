package com.example.banksalad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class addExercise extends AppCompatActivity {
    private static final String TAG =  "MyActivity";
    String exercise_date, temp;
    String[] temp_date;
    private EditText exercise_name, exercise_cnt, exercise_set;
    private Button button;

    String cal_sport_userId;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        cal_sport_userId = userID; // userID 받아오기

        button = findViewById(R.id.ok_exercise);

        temp = intent.getStringExtra("datetext");
        temp_date = temp.split("/"); // 2021/11/29 자르기
        exercise_date = "" + temp_date[0];
        exercise_date += (Integer.parseInt(temp_date[1]) < 10) ? "0" + temp_date[1] : temp_date[1];
        exercise_date += (Integer.parseInt(temp_date[2]) < 10) ? "0" + temp_date[2] : temp_date[2];

        exercise_name = (EditText)findViewById(R.id.dlgName_exercise);
        exercise_cnt = (EditText)findViewById(R.id.dlgcnt_exercise);
        exercise_set = (EditText)findViewById(R.id.dlgset_exercise);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String DATE = exercise_date;
                String NAME = exercise_name.getText().toString();
                String CNT = exercise_cnt.getText().toString();
                String SET = exercise_set.getText().toString();

                Intent intent = getIntent();
                userID = intent.getStringExtra("userID");
                String USERID = userID;

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jasonObject = new JSONObject(response);
                            boolean success=jasonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "기록 완료", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(addExercise.this, addList.class);
                                intent.putExtra("cal_sport_date", temp);
                                intent.putExtra("userID", userID);
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

                ExerciseInsertRequest exerciseRequest = new ExerciseInsertRequest(DATE, NAME, CNT, SET, USERID, responseListener);
                RequestQueue queue= Volley.newRequestQueue(addExercise.this);
                queue.add(exerciseRequest);
            }
        });
    }
}