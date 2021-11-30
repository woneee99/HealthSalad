package com.example.banksalad.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.banksalad.R;

public class fragWatch extends Fragment {
    private View view;
    private Button mStartBtn, mStopBtn, mRecordBtn, mPauseBtn;
    private TextView mTimeTextView, mRecordTextView;

    private Thread timeThread = null;
    private Boolean isRunning = true;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.activity_watch, container, false);

        mStartBtn = (Button) view.findViewById(R.id.btn_start);
        mStopBtn = (Button) view.findViewById(R.id.btn_stop);
        mRecordBtn = (Button) view.findViewById(R.id.btn_record);
        mPauseBtn = (Button) view.findViewById(R.id.btn_pause);
        mTimeTextView = (TextView) view.findViewById(R.id.timeView);
        mRecordTextView = (TextView) view.findViewById(R.id.recordView);


        mStartBtn.setOnClickListener(new View.OnClickListener() { // 첫화면 시작 버튼
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                mStopBtn.setVisibility(View.VISIBLE);
                mRecordBtn.setVisibility(View.VISIBLE);
                mPauseBtn.setVisibility(View.VISIBLE);

                timeThread = new Thread(new fragWatch.timeThread());
                timeThread.start();
            }
        });

        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                mRecordBtn.setVisibility(View.GONE);
                mStartBtn.setVisibility(View.VISIBLE);
                mPauseBtn.setVisibility(View.GONE);
                mRecordTextView.setText("");
                timeThread.interrupt();
            }
        });

        mRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecordTextView.setText(mRecordTextView.getText() + mTimeTextView.getText().toString() + "\n");
            }
        });

        mPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = !isRunning;
                if (isRunning) {
                    mPauseBtn.setText("일시정지");
                } else {
                    mPauseBtn.setText("시작");
                }
            }
        });
        return view;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //1000이 1초 1000*60 은 1분 1000*60*10은 10분 1000*60*60은 한시간
            int sec = (msg.arg1 / 100) % 60;
            int min = (msg.arg1 / 100) / 60;
            int hour = (msg.arg1 / 100) / 360;

            String result = String.format("%02d:%02d:%02d", hour, min, sec);
            mTimeTextView.setText(result);
        }
    };

    public class timeThread implements Runnable {
        @Override
        public void run() {
            int i = 0;

            while (true) {
                while (isRunning) { //일시정지를 누르면 멈춤
                    Message msg = new Message();
                    msg.arg1 = i++;
                    handler.sendMessage(msg);

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return; // 인터럽트 받을 경우 return
                    }
                }
            }
        }
    }

}
