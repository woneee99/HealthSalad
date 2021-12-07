package com.example.banksalad;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.banksalad.fragment.fragPlan;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AddSportChange extends AppCompatActivity {
    Fragment fragPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        Bundle bundle1 = new Bundle();
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        Fragment fragment2 = new fragPlan();
        fragment2.setArguments(bundle1);
        transaction1.replace(R.id.container, fragment2);
        transaction1.commit();
    }
}