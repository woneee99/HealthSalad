package com.example.banksalad;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.banksalad.fragment.fragChange;
import com.example.banksalad.fragment.fragPlan;
import com.example.banksalad.fragment.fragUser;
import com.example.banksalad.fragment.tabFragment1;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Menu menu;

    Fragment tabFragment1;
    Fragment fragUser;
    Fragment fragPlan;
    Fragment fragChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabFragment1 = new tabFragment1();
        fragUser = new fragUser();
        fragPlan = new fragPlan();
        fragChange = new fragChange();

        bottomNavigationView = findViewById(R.id.navigation);

        //첫 화면 띄우기
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,tabFragment1).commitAllowingStateLoss();

        //FragmentManager fragmentManager = getSupportFragmentManager();
        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.frag1, new tabFragment1());
        //fragmentTransaction.commit();

        bottomNavigationView = findViewById(R.id.navigation);

        //case 함수를 통해 클릭 받을 때마다 화면 변경하기
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.cal :
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new tabFragment1()).commit();
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
    public void mOnClick(View v){
        Intent intent = new Intent(getApplicationContext(),addList.class);
        startActivity(intent);
    }
}
