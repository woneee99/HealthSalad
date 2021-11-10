package com.example.banksalad.bindingAdapter;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TextBindingAdapter {
    @BindingAdapter({"setCalendarHeaderText"})
    public static void setCalendarHeaderText(TextView view, Long date) {
        try {
            if (date != null) {
                view.setText(com.example.banksalad.utils.DateFormat.getDate(date, com.example.banksalad.utils.DateFormat.CALENDAR_HEADER_FORMAT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BindingAdapter({"setDayText"})
    public static void setDayText(TextView view, Calendar calendar) {
        try {
            if (calendar != null) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                view.setText(com.example.banksalad.utils.DateFormat.getDate(gregorianCalendar.getTimeInMillis(), com.example.banksalad.utils.DateFormat.DAY_FORMAT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}