package com.example.banksalad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

class add_Final extends AppCompatActivity {
    int m_cnt = 0;
    int l_cnt = 0;
    int d_cnt = 0;

    TextView record_exercise;
    private EditText exercise_name, exercise_cnt, exercise_set;
    String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        TextView record_m = (TextView) findViewById(R.id.breakfast_list);
        TextView record_l = (TextView) findViewById(R.id.lunch_list);
        TextView record_d = (TextView) findViewById(R.id.dinner_list);

        record_exercise = (TextView) findViewById(R.id.exercise_list);
        exercise_name = (EditText)findViewById(R.id.dlgName_exercise);
        exercise_cnt = (EditText)findViewById(R.id.dlgcnt_exercise);
        exercise_set = (EditText)findViewById(R.id.dlgset_exercise);

        Intent intent = getIntent();
        int Num = intent.getExtras().getInt("num");
        String f_Name = intent.getStringExtra("fd_name");
        String f_cnt = intent.getStringExtra("fd_cnt");
        String f_weight = intent.getStringExtra("fd_weight");

        String e_Name = intent.getStringExtra("cal_sport_name");
        String e_Cnt = intent.getStringExtra("cal_sport_cnt");
        String e_Set = intent.getStringExtra("cal_sport_set");

        if(f_Name != null){ //음식이 입력 됐을 때
            if(Num == 1){
                record_m.setText(record_m.getText() + f_Name);
                //추가할때마다 db에 insert -> select
            }else if (Num == 2){
                record_l.setText(record_m.getText() + f_Name);
                //추가할때마다 db에 insert -> select
            }else{
                record_d.setText(record_m.getText() + f_Name);
                //추가할때마다 db에 insert -> select
            }
        }
    }


}