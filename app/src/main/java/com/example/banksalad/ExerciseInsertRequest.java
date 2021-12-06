package com.example.banksalad;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ExerciseInsertRequest extends StringRequest{
    //서버 url 설정(php파일 연동)
    final static  private String URL="http://10.0.2.2/calsports.php";
    private Map<String,String> map;

    public ExerciseInsertRequest(String cal_sport_date, String cal_sport_name, String cal_sport_cnt, String cal_sport_set, String cal_sport_userID, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);

        map = new HashMap<>();
        map.put("cal_sport_date",cal_sport_date);
        map.put("cal_sport_name",cal_sport_name);
        map.put("cal_sport_cnt",cal_sport_cnt);
        map.put("cal_sport_set",cal_sport_set);
        map.put("cal_sport_userID",cal_sport_userID);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
