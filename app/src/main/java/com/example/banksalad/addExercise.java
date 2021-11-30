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
    private TextView exercise_date;
    private EditText exercise_name, exercise_cnt, exercise_set;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        button = findViewById(R.id.ok_exercise);
        exercise_date = (TextView)findViewById(R.id.input_date);
        exercise_name = (EditText)findViewById(R.id.dlgName_exercise);
        exercise_cnt = (EditText)findViewById(R.id.dlgcnt_exercise);
        exercise_set = (EditText)findViewById(R.id.dlgset_exercise);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String DATE = exercise_date.toString();
                final String NAME = exercise_name.getText().toString();
                final String CNT = exercise_cnt.getText().toString();
                final String SET = exercise_set.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jasonObject = new JSONObject(response);
                            boolean success=jasonObject.getBoolean("success");
                            if (success) {
                                String cal_sport_Name = jasonObject.optString("cal_sport_name","");
                                String cal_sport_Cnt = jasonObject.optString("cal_sport_cnt","");
                                String cal_sport_Set = jasonObject.optString("cal_sport_set","");

                                Toast.makeText(getApplicationContext(), "기록 완료", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(addExercise.this, add_Final.class);
                                intent.putExtra("cal_sport_date", DATE);
                                intent.putExtra("cal_sport_name", NAME);
                                intent.putExtra("cal_sport_cnt", CNT);
                                intent.putExtra("cal_sport_set", SET);
                                startActivity(intent);
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

                ExerciseInsertRequest exerciseRequest = new ExerciseInsertRequest(DATE, NAME, CNT, SET,responseListener);
                RequestQueue queue= Volley.newRequestQueue(addExercise.this);
                queue.add(exerciseRequest);
            }
        });
    }

}