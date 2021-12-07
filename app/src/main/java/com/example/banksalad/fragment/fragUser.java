package com.example.banksalad.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.banksalad.R;

import static androidx.constraintlayout.widget.StateSet.TAG;

public class fragUser extends Fragment {
    private View view;
    private TextView name, weight, birth, height, BMI, join_chk_password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.activity_my, container, false);

        name = (TextView)view.findViewById(R.id.user_name1);
        weight = (TextView)view.findViewById(R.id.user_weight1);
        height = (TextView)view.findViewById(R.id.user_height1);
        birth = (TextView)view.findViewById(R.id.user_birth1);
        BMI = (TextView)view.findViewById(R.id.user_BMI1);

        if(getArguments() != null) {
            String userName = getArguments().getString("userName");
            String userBirth = getArguments().getString("userBirth");
            String userWeight = getArguments().getString("userWeight");
            String userHeight = getArguments().getString("userHeight");

            double uHeight = Double.parseDouble(userHeight)*0.01;
            double uWeight = Double.parseDouble(userWeight);
            double result = uWeight/(uHeight*uHeight);
            if(result<=18.5){
                BMI.setText("저체중");
            }else if(result>18.5 && result<=22.9){
                BMI.setText("정상");
            }else if(result>=23 && result<=24.9){
                BMI.setText("과체중");
            }else{
                BMI.setText("비만");
            }
            Log.d(TAG, userName);
            name.setText(userName);
            birth.setText(userBirth);
            weight.setText(userWeight);
            height.setText(userHeight);
        }
        return view;
    }
}
