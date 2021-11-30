package com.example.banksalad;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class joinRequest extends StringRequest {

    //서버 url 설정(php파일 연동)
    final static  private String URL="http://10.0.2.2/join.php";
    private Map<String,String>map;

    public joinRequest(String userID, String userPassword, String userName, String userBirth, Double userHeight, Double userWeight, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null); //위 url에 post방식으로 값을 전송

        map=new HashMap<>();
        map.put("userID",userID);
        map.put("userPassword",userPassword);
        map.put("userName",userName);
        map.put("userBirth",userBirth);
        map.put("userHeight",userHeight+"");
        map.put("userWeight",userWeight+"");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}