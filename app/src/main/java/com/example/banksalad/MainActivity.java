package com.example.banksalad;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.banksalad.fragment.fragCal;
import com.example.banksalad.fragment.fragWatch;
import com.example.banksalad.fragment.fragPlan;
import com.example.banksalad.fragment.fragUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Menu menu;

    Fragment fragCal;
    Fragment fragUser;
    Fragment fragPlan;
    Fragment fragWatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragCal = new fragCal();
        fragUser = new fragUser();
        fragWatch = new fragWatch();
        fragPlan = new fragPlan();

        Intent intent = getIntent();
        final String userID = intent.getStringExtra("userID");
        final String userName = intent.getStringExtra("userName");
        final String userBirth = intent.getStringExtra("userBirth");
        final String userHeight = intent.getStringExtra("userHeight");
        final String userWeight = intent.getStringExtra("userWeight");

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
}
