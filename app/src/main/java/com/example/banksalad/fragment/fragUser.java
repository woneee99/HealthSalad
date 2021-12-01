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
    private TextView name, weight, birth, BMI;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.activity_my, container, false);

        name = (TextView)view.findViewById(R.id.user_name1);
        weight = (TextView)view.findViewById(R.id.user_weight1);
        birth = (TextView)view.findViewById(R.id.user_birth1);
        BMI = (TextView)view.findViewById(R.id.user_BMI1);

        if(getArguments() != null) {
            String userName = getArguments().getString("userName");
            String userBirth = getArguments().getString("userBirth");
            String userWeight = getArguments().getString("userWeight");

            name.setText(userName);
            birth.setText(userBirth);
            weight.setText(userWeight);
        }
        return view;
    }
}
