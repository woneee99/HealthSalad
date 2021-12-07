package com.example.banksalad;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.banksalad.fragment.fragCal;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class goFragCal extends AppCompatActivity {
    Fragment fragCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");

        Bundle bundle1 = new Bundle();
        bundle1.putString("userID",userID);
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        Fragment fragment2 = new fragCal();
        fragment2.setArguments(bundle1);
        transaction1.replace(R.id.frame_container, fragment2);
        transaction1.commit();
    }
}