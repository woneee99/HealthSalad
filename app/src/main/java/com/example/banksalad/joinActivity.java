package com.example.banksalad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class joinActivity extends AppCompatActivity {
    private EditText et_id, et_pass, et_name, et_birth,et_height,et_weight;
    private Button btn_register,validateButton;


    private AlertDialog dialog;
    private boolean validate=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        //아이디 값 찾아주기
        et_id=findViewById(R.id.join_id);
        et_pass=findViewById(R.id.join_password);
        et_name=findViewById(R.id.join_name);
        et_birth=findViewById(R.id.join_birth);

        et_height=findViewById(R.id.join_height);
        et_weight=findViewById(R.id.join_weight);
        validateButton=findViewById(R.id.validateButton);

        validateButton.setOnClickListener(new View.OnClickListener() {//id중복체크
            @Override
            public void onClick(View view) {
                String userID=et_id.getText().toString();
                if(validate)
                {
                    return;
                }
                if(userID.equals("")){
                    AlertDialog.Builder builder=new AlertDialog.Builder( joinActivity.this );
                    dialog=builder.setMessage("아이디는 빈 칸일 수 없습니다")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse=new JSONObject(response);
                            boolean success=jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder=new AlertDialog.Builder( joinActivity.this );
                                dialog=builder.setMessage("사용할 수 있는 아이디입니다.").setPositiveButton("확인",null).create();
                                dialog.show();
                                et_id.setEnabled(false);
                                validate=true;
                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder( joinActivity.this );
                                dialog=builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest=new ValidateRequest(userID,responseListener);
                RequestQueue queue= Volley.newRequestQueue(joinActivity.this);
                queue.add(validateRequest);
            }
        });


        btn_register=findViewById(R.id.lets_join);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //editText에 입력되어있는 값을 get(가져온다)해온다
                String userID=et_id.getText().toString();
                String userPass=et_pass.getText().toString();
                String userName=et_name.getText().toString();
                String userBirth=et_birth.getText().toString();

                Double userHeight= Double.parseDouble(et_height.getText().toString());
                Double userweight= Double.parseDouble(et_weight.getText().toString());

                Response.Listener<String> responseListener = new Response.Listener<String>(){//volley
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse=new JSONObject(response);//joib php에 response
                            boolean success=jsonResponse.getBoolean("success");//php에 sucess
                            if(success){ // 회원가입이 가능한다면
                                Toast.makeText(getApplicationContext(), "회원 등록 성공", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(joinActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }else{// 회원가입이 안된다면
                                Toast.makeText(getApplicationContext(), "회원가입에 실패했습니다. 다시 한 번 확인해 주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //서버로 volley를 이용해서 요청을 함
                joinRequest registerRequest=new joinRequest(userID,userPass, userName, userBirth,userHeight,userweight,responseListener);
                RequestQueue queue= Volley.newRequestQueue(joinActivity.this);
                queue.add(registerRequest);

            }
        });
    }

}