package com.example.banksalad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class addFood extends AppCompatActivity {
    int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        Button button = findViewById(R.id.ok);
        final EditText food_name = (EditText)findViewById(R.id.dlgName);
        final EditText food_cnt = (EditText)findViewById(R.id.dlgcnt);
        final EditText food_weight = (EditText)findViewById(R.id.dlgweight);

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
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), add_Final.class);
                if (state == 1) { //아침
                    intent.putExtra("num",1);
                } else if (state == 2) { //점심
                    intent.putExtra("num",2);
                } else { //저녁
                    intent.putExtra("num",3);
                }
                intent.putExtra("fd_name", food_name.getText().toString());
                intent.putExtra("fd_cnt", food_cnt.getText().toString());
                intent.putExtra("fd_weight", food_weight.getText().toString());
                startActivity(intent);
            }
        });
    }
}