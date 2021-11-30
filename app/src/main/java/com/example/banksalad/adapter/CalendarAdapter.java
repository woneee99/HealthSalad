package com.example.banksalad.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.banksalad.R;
import com.example.banksalad.model.CalendarHeader;
import com.example.banksalad.model.Day;
import com.example.banksalad.model.EmptyDay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter {
    private final int HEADER_TYPE = 0;
    private final int EMPTY_TYPE = 1;
    private final int DAY_TYPE = 2;

    private List<Object> mCalendarList;

    public CalendarAdapter(List<Object> calendarList) {
        mCalendarList = calendarList;
    }

    public void setCalendarList(List<Object> calendarList) {
        mCalendarList = calendarList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) { //뷰타입 나누기
        Object item = mCalendarList.get(position);
        if (item instanceof Long) {
            return HEADER_TYPE; //날짜 타입
        } else if (item instanceof String) {
            return EMPTY_TYPE; // 비어있는 일자 타입
        } else {
            return DAY_TYPE; // 일자 타입
        }
    }

    // onCreateViewHolder(ViewGroup parent, int viewType) : viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성.
    // viewHolder 생성
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // 날짜 타입
        if (viewType == HEADER_TYPE) {
            HeaderViewHolder viewHolder = new HeaderViewHolder(inflater.inflate(R.layout.activity_item_calendar_header, parent, false));

            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams)viewHolder.itemView.getLayoutParams();
            params.setFullSpan(true); //Span을 하나로 통합하기
            viewHolder.itemView.setLayoutParams(params);

            return viewHolder;
            //비어있는 일자 타입
        } else if (viewType == EMPTY_TYPE) {
            return new EmptyViewHolder(inflater.inflate(R.layout.activity_item_day_empty, parent, false));
        }
        // 일자 타입
        else {
            return new DayViewHolder(inflater.inflate(R.layout.activity_item_day, parent, false));
        }
    }

    //onBindViewHolder(ViewHolder holder, int position) : position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    // 데이터 넣어서 완성시키는것
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);

        /**날짜 타입 꾸미기*/
        /** EX : 2021년 11월*/
        if (viewType == HEADER_TYPE) {
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
            Object item = mCalendarList.get(position);
            CalendarHeader model = new CalendarHeader();

            // long type의 현재시간
            if (item instanceof Long) {
                // 현재시간 넣으면, 2017년 7월 같이 패턴에 맞게 model에 데이터 들어감.
                model.setHeader((Long) item);
            }
            // view에 표시하기
            holder.bind(model);
        }
        /** 비어있는 날짜 타입 꾸미기 */
        /** EX : empty */
        else if (viewType == EMPTY_TYPE) {
            EmptyViewHolder holder = (EmptyViewHolder) viewHolder;
            EmptyDay model = new EmptyDay();
            holder.bind(model);
        }
        /** 일자 타입 꾸미기 */
        /** EX : 22 */
        else if (viewType == DAY_TYPE) {
            DayViewHolder holder = (DayViewHolder) viewHolder;
            Object item = mCalendarList.get(position);
            Day model = new Day();
            if (item instanceof Calendar) {
                // Model에 Calendar값을 넣어서 몇일인지 데이터 넣기
                model.setCalendar((Calendar) item);
            }
            // Model의 데이터를 View에 표현하기
            holder.bind(model);
        }
    }

    // getItemCount() : 전체 아이템 갯수 리턴.
    // 개수 구하기
    @Override
    public int getItemCount() {
        if (mCalendarList != null) {
            return mCalendarList.size();
        }
        return 0;
    }
    /** viewHolder */
    private class HeaderViewHolder extends RecyclerView.ViewHolder { //날짜 타입 ViewHolder
        TextView itemHeaderTitle;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            initView(itemView);
        }

        public void initView(View v){
            itemHeaderTitle = (TextView)v.findViewById(R.id.item_header_title);
        }

        public void bind(CalendarHeader model){
            // 일자 값 가져오기
            String header = ((CalendarHeader)model).getHeader();

            // header에 표시하기, ex : 2021년 11월
            itemHeaderTitle.setText(header);
        };
    }

    private class EmptyViewHolder extends RecyclerView.ViewHolder { // 비어있는 요일 타입 ViewHolder
        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        public void initView(View v){

        }

        public void bind(EmptyDay model){

        };
    }

    // TODO : item_day와 매칭
    private class DayViewHolder extends RecyclerView.ViewHolder {// 요일 입 ViewHolder
        TextView itemDay;
        TextView selectDay;
        TextView text1;
        TextView text2;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);

            initView(itemView);

        }

        public void initView(View v){
            itemDay = (TextView)v.findViewById(R.id.item_day);
            selectDay = (TextView)v.findViewById(R.id.select_day);
            text1 = (TextView)v.findViewById(R.id.food_kcal);
            text2 = (TextView)v.findViewById(R.id.exercise_kcal);
        }

        public void bind(Day model){
            // 일자 값 가져오기
            String day = ((Day)model).getDay();
            int check = 1;

            if(check == 1){
                SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd");
                Date now = new Date();
                String today = sdf.format(now);
                String temp = ((Day)model).getYear()+"-"+((Day)model).getMonth()+"-"+day; //현재 칸 날짜 -> db 조회 할 떄 사용하귀
                // 달력에 추가할떄도 temp

                if(day.charAt(0) == '0') {
                    day = day.substring(1);
                }

                if(today.equals(temp) && check==1){
                    selectDay.setText(day);
//                    text1.setText("음식: " +"10kcal");
                    check=0;
                }
                else{
                    // 일자 값 View에 보이게하기
                    itemDay.setText(day);
                }
                //if(음식 값 있을떄?)
                text1.setText("음식: " +"10kcal");
                //if(운동 값 있을떄?)
                text2.setText("운동ㅇ");
            }
            else{
                // 일자 값 View에 보이게하기
                itemDay.setText(day);

                text1.setText("음식: " +"10kcal");
                text2.setText("운동: " +"10kcal");
            }
        };
    }
}