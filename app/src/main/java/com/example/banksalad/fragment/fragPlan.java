package com.example.banksalad.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.banksalad.CalendarActivity;
import com.example.banksalad.R;

public class fragPlan extends Fragment {
    private View bottomview;
    private View navi;
    private Button btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        bottomview = inflater.inflate(R.layout.activity_bottom, container, false);

        navi=bottomview.findViewById(R.id.navigation);
        btn=navi.findViewById(R.id.plan);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
            }
        });


        return bottomview;
    }
}
