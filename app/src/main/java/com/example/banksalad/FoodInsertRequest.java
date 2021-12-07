package com.example.banksalad;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FoodInsertRequest extends StringRequest {
    //서버 url 설정(php파일 연동)
    final static  private String URL="http://10.0.2.2/calfood.php";
    private Map<String,String> map;

    public FoodInsertRequest(String cal_food_date, String cal_food_breakfast, String cal_food_lunch, String cal_food_dinner,  String cal_food_kcal, String cal_food_userID, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);

        map = new HashMap<>();
        map.put("cal_food_date",cal_food_date);
        map.put("cal_food_breakfast",cal_food_breakfast);
        map.put("cal_food_lunch",cal_food_lunch);
        map.put("cal_food_dinner",cal_food_dinner);
        map.put("cal_food_kcal",cal_food_kcal);
        map.put("cal_food_userID",cal_food_userID);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
