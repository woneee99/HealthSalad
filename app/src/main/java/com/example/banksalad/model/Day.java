package com.example.banksalad.model;

import com.example.banksalad.util.DateUtil;

import java.util.Calendar;

public class Day extends ViewModel {
    String year;
    String month;
    String day;

    public Day() {
    }

    public String getYear() {
        return year;
    }
    public String getMonth() {
        return month;
    }
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    // TODO : day에 달력일값넣기
    public void setCalendar(Calendar calendar){
        year = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.YEAR_FORMAT);
        month = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.MONTH_FORMAT);
        day = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.DAY_FORMAT);
    }
}