package com.example.banksalad;

import androidx.lifecycle.MutableLiveData;
import com.example.banksalad.utils.Keys;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class setCalendarList {
    MutableLiveData<ArrayList<Object>> mCalendarList= new MutableLiveData<>();

    public void setCalendarList() {
        GregorianCalendar cal = new GregorianCalendar(); // 오늘 날짜
        ArrayList<Object> calendarList = new ArrayList<>();

        for (int i = -300; i < 300; i++) {
            try {
                GregorianCalendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + i, 1, 0, 0, 0);
                calendarList.add(calendar.getTimeInMillis()); //날짜 타입

                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; //해당 월에 시작하는 요일 -1 을 하면 빈칸을 구할 수 있겠죠 ?
                int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 해당 월에 마지막 요일

                for (int j = 0; j < dayOfWeek; j++) {
                    calendarList.add(Keys.EMPTY);  //비어있는 일자 타입
                }
                for (int j = 1; j <= max; j++) {
                    calendarList.add(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), j)); //일자 타입
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mCalendarList.setValue(calendarList);
    }
}
