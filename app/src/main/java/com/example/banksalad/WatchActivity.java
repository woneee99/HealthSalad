package com.example.banksalad;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WatchActivity extends AppCompatActivity {

    private Button mStartBtn, mStopBtn, mRecordBtn, mPauseBtn;
    private TextView mTimeTextView, mRecordTextView;

    private Thread timeThread = null;
    private Boolean isRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        mStartBtn = (Button) findViewById(R.id.btn_start);
        mStopBtn = (Button) findViewById(R.id.btn_stop);
        mRecordBtn = (Button) findViewById(R.id.btn_record);
        mPauseBtn = (Button) findViewById(R.id.btn_pause);
        mTimeTextView = (TextView) findViewById(R.id.timeView);
        mRecordTextView = (TextView) findViewById(R.id.recordView);


        mStartBtn.setOnClickListener(new View.OnClickListener() { // 첫화면 시작 버튼
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                mStopBtn.setVisibility(View.VISIBLE);
                mRecordBtn.setVisibility(View.VISIBLE);
                mPauseBtn.setVisibility(View.VISIBLE);

                timeThread = new Thread(new timeThread());
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

