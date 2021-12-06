package com.example.banksalad;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.banksalad.fragment.fragCal;
import com.example.banksalad.fragment.fragChange;
import com.example.banksalad.fragment.fragPlan;
import com.example.banksalad.fragment.fragUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Menu menu;

    Fragment fragCal;
    Fragment fragUser;
    Fragment fragPlan;
    Fragment fragChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragCal = new fragCal();
        fragUser = new fragUser();
        fragPlan = new fragPlan();
        fragChange = new fragChange();

        bottomNavigationView = findViewById(R.id.navigation);

        //첫 화면 띄우기
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragCal).commitAllowingStateLoss();

        bottomNavigationView = findViewById(R.id.navigation);

        //case 함수를 통해 클릭 받을 때마다 화면 변경하기
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.cal :
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new fragCal()).commit();
                        break;
                    case R.id.user:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new fragUser()).commit();
                        break;
                    case R.id.plan:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new fragPlan()).commit();
                        break;
                }
                return true;
            }
        });
    }
}
