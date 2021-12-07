package com.example.banksalad;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static androidx.constraintlayout.widget.StateSet.TAG;

public class AddSportPlanActivity extends AppCompatActivity {

    private TextView dateTv;
    private Button addBtn;
    private EditText sportName;
    private EditText sportSet;
    private EditText sportCnt;
    String day;
    String idx;
    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sportplan);

        Intent calintent=getIntent(); //얘 뭐하는 애더라

        dateTv=(TextView)findViewById(R.id.date_item);
        addBtn=(Button)findViewById(R.id.addBtn_sportP);
        sportName=(EditText)findViewById(R.id.add_sportName);
        sportSet=(EditText)findViewById(R.id.add_sportSet);
        sportCnt=(EditText)findViewById(R.id.add_sportCnt);

        int tmpidx=calintent.getIntExtra("idxcnt",1);
        idx=Integer.toString(++tmpidx);

        day=calintent.getStringExtra("dayString");
        id=calintent.getStringExtra("user_id");
        dateTv.setText(day);


        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String name=sportName.getText().toString();
                String set=sportSet.getText().toString();
                String cnt=sportCnt.getText().toString();

                InsertData task=new InsertData();
                task.execute("http://10.0.2.2/addsportdate.php",name,set,cnt);

            }
        });

    }//onCreate

    class InsertData extends AsyncTask<String,Void,String>{

        String target;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            target = "http://10.0.2.2/addsportdate.php";
        }

        @Override
        protected void onPostExecute(String s) {//디비에 있는거 다 받아옴
            super.onPostExecute(s);

            Log.d(TAG,"response--"+s);

            Intent intent=new Intent(getApplicationContext(),CalendarActivity.class);
            startActivity(intent);

            finish();
        }

        @Override
        protected String doInBackground(String... params) {

            String name=(String)params[1];
            String set=(String)params[2];
            String cnt=(String)params[3];



            Log.d(TAG,"id: "+id);

            String serverURL=(String)params[0];
            String postParameters="idx="+idx+"&user_id="+id+"&sport_date="+day+"&sport_name="+name+"&sport_cnt="+cnt+"&sport_set="+set;

            try{
                URL url=new URL(target);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream=httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode=httpURLConnection.getResponseCode();
                Log.d(TAG,"POST response code - "+responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode==HttpURLConnection.HTTP_OK){
                    inputStream=httpURLConnection.getInputStream();
                }
                else{
                    inputStream=httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"UTF-8");

                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);

                StringBuilder sb=new StringBuilder();
                String line=null;

                while((line=bufferedReader.readLine())!=null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();

            }catch (Exception e){

                return new String("Error: "+e.getMessage());
            }

        }
    }


}
