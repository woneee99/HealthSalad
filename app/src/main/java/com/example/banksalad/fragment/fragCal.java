package com.example.banksalad.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.banksalad.MainCalendarActivity;
import com.example.banksalad.R;
import com.example.banksalad.addList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static androidx.constraintlayout.widget.StateSet.TAG;

public class fragCal extends Fragment {
    private View view;
    Map<String, Double> map= new HashMap<String, Double>(); //음식 칼로리 map
    Map<String, String> map2= new HashMap<String, String>(); //운동 체크 map
    String cal_sport_userId;
    String cal_food_userId;

    /**
     * 연/월 텍스트뷰
     */
    private TextView tvDate;
    /**
     * 그리드뷰 어댑터
     */
    private fragCal.GridAdapter gridAdapter;

    /**
     * 일 저장 할 리스트
     */
    private class DayItem {
        private String Type;
        private String Day;
        private Double Kcal;
        private String Set;

        public void setType(String type) {
            Type = type;
        }

        public void setDay(String day) {
            Day = day;
        }

        public void setKcal(Double kcal) {
            Kcal = kcal;
        }

        public void setSet(String set) {
            Set = set;
        }

        public String getType() {
            return Type;
        }

        public String getDay() {
            return Day;
        }

        public Double getKcal() {
            return Kcal;
        }

        public String getSet() {
            return Set;
        }

        public DayItem(String type, String day, Double kcal, String set) {
            Type = type;
            Day = day;
            Kcal = kcal;
            Set = set;
        }
    }
    private ArrayList<fragCal.DayItem> dayList;

    /**
     * 그리드뷰
     */
    private GridView gridView;

    /**
     * 왼쪽, 오른쪽 버튼
     */
    private Button leftBtn, rightBtn,toAddBtn;

    /**
     * 캘린더 변수
     */
    private Calendar mCal;
    private int showYear;
    private int showMon;
    private int showDay;

    private LinearLayout container;
    String mJsonString;

    private class DbItem{
        String day;
        String kcal;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getKcal() {
            return kcal;
        }

        public void setSport(String kcal) {
            this.kcal = kcal;
        }

        public DbItem(String day, String kcal) {
            this.day = day;
            this.kcal = kcal;
        }
    }

    ArrayList<fragCal.DbItem> dbList;
    int lastidx;
    String pickdays;
    TextView tvvitem;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.activity_calendar_tab, container, false);
        Log.d(TAG,"캘린더 돌아옴");

        dbList = new ArrayList<>();

        String userID = getActivity().getIntent().getStringExtra("userID");

        cal_food_userId = userID;
        cal_sport_userId = userID;

        fragCal.GetDataFoodKcal taskKcal = new fragCal.GetDataFoodKcal();
        taskKcal.execute(cal_food_userId);

        fragCal.GetDataExerciseCheck taskexercise = new fragCal.GetDataExerciseCheck();
        taskexercise.execute(cal_sport_userId);

        tvDate = (TextView) view.findViewById(R.id.tv_date);
        gridView = (GridView) view.findViewById(R.id.gridview);
        leftBtn = (Button) view.findViewById(R.id.pre_btn);
        rightBtn = (Button) view.findViewById(R.id.next_btn);
        toAddBtn=(Button) view.findViewById(R.id.btnrecord);

        // 오늘에 날짜를 세팅 해준다.
        long now = System.currentTimeMillis();
        final Date date = new Date(now);

        //연,월,일을 따로 저장
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        showYear = Integer.parseInt(curYearFormat.format(date));
        showMon = Integer.parseInt(curMonthFormat.format(date));
        showDay = Integer.parseInt(curDayFormat.format(date));

        //나중에 추가페이지에 넘겨줄 선택 날짜 String
        pickdays=""+showYear;
        pickdays+=(showMon<10)? "0"+showMon:showMon;
        pickdays+=(showDay<10)? "0"+showDay:showDay;

        //현재 날짜 텍스트뷰에 뿌려줌
        tvDate.setText(curYearFormat.format(date) + "/" + curMonthFormat.format(date));

        //gridview 요일 표시
        dayList = new ArrayList<>();
        dayList.add(new fragCal.DayItem("0", "일", 0.0, ""));
        dayList.add(new fragCal.DayItem("0", "월", 0.0, ""));
        dayList.add(new fragCal.DayItem("0", "화", 0.0, ""));
        dayList.add(new fragCal.DayItem("0", "수", 0.0, ""));
        dayList.add(new fragCal.DayItem("0", "목", 0.0, ""));
        dayList.add(new fragCal.DayItem("0", "금", 0.0, ""));
        dayList.add(new fragCal.DayItem("0", "토", 0.0, ""));

        mCal = Calendar.getInstance();

        //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        //1일 - 요일 매칭 시키기 위해 공백 add

        for (int i = 1; i < dayNum; i++) {
            dayList.add(new fragCal.DayItem("0", "", 0.0, ""));
        }
        setCalendarDate(mCal.get(Calendar.MONTH) + 1, dayNum);


        gridAdapter = new fragCal.GridAdapter(getActivity(), dayList);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a_parent, View a_view, int a_position, long a_id) {
                fragCal.DayItem item = gridAdapter.getItem(a_position);
                int pDay=Integer.parseInt(item.getDay());

                pickdays=""+showYear;
                pickdays+=(showMon<10)? "0"+showMon:showMon;
                pickdays+=(pDay<10)? "0"+pDay:pDay;
            }
        });
        return view;
    }//onCreate()

    /**
     * 해당 월에 표시할 일 수 구함
     *
     * @param month
     */
    private void setCalendarDate(int month, int dayNum) {
        mCal.set(Calendar.MONTH, month - 1);

        for (int i = 0, j = dayNum; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++, j++) {
            dayList.add(new fragCal.DayItem("1", "" + (i + 1), 0.0, "20세트"));
        }
    }

    /**
     * 그리드뷰 어댑터
     */
    private class GridAdapter extends BaseAdapter {
        private final List<fragCal.DayItem> list;
        private final LayoutInflater inflater;

        /**
         * 생성자
         *
         * @param context
         * @param list
         */
        public GridAdapter(Context context, List<fragCal.DayItem> list) {
            this.list = list;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public fragCal.DayItem getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            fragCal.ViewHolder holder = null;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.activity_calendar_tab_item, parent, false);
//                listView=inflater.inflate()
                holder = new fragCal.ViewHolder();

                holder.tvItemDay = (TextView) convertView.findViewById(R.id.tv_item_gridview);
                holder.tvItemfoodKCAL = (TextView) convertView.findViewById(R.id.food_KCAL);
                holder.tvItemExercise = (TextView) convertView.findViewById(R.id.exercise_tv);
                holder.tvItemWorks = (LinearLayout) convertView.findViewById(R.id.listview);
                convertView.setTag(holder);

            } else {
                holder = (fragCal.ViewHolder) convertView.getTag();
            }

            String result_kcal="";
            String result_exercise="";
            String posDays="0";
            if(!dayList.get(position).getType().equals("0")){
                int pDay=Integer.parseInt(dayList.get(position).getDay());
                posDays=showYear+"";
                posDays+=(showMon<10)? "0"+showMon:showMon;
                posDays+=(pDay<10)? "0"+pDay:pDay;

                if(map.containsKey(posDays)==true){
                    result_kcal = Double.toString(map.get(posDays));
                }else{
                    result_kcal="";
                }

                if(map2.containsKey(posDays)==true){
                    result_exercise = map2.get(posDays);
                }else{
                    result_exercise="";
                }
            }

            //holder.bind(dayList.get(position).getDay(), result_kcal, dayList.get(position).getSet());
            holder.bind(dayList.get(position).getDay(), result_kcal, result_exercise);

            leftBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (--showMon == 0) {
                        showYear--;
                        showMon = 12;
                    }

                    mCal = Calendar.getInstance();
                    mCal.set(showYear, showMon - 1, 1);
                    int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
//1일 - 요일 매칭 시키기 위해 공백 add

                    tvDate.setText(showYear + "/" + showMon);

                    dayList.clear();
                    dayList.add(new fragCal.DayItem("0", "일", 0.0, ""));
                    dayList.add(new fragCal.DayItem("0", "월", 0.0, ""));
                    dayList.add(new fragCal.DayItem("0", "화", 0.0, ""));
                    dayList.add(new fragCal.DayItem("0", "수", 0.0, ""));
                    dayList.add(new fragCal.DayItem("0", "목", 0.0, ""));
                    dayList.add(new fragCal.DayItem("0", "금", 0.0, ""));
                    dayList.add(new fragCal.DayItem("0", "토", 0.0, ""));

                    for (int i = 1; i < dayNum; i++) {
                        dayList.add(new fragCal.DayItem("0", "", 0.0, ""));
                    }

                    setCalendarDate(mCal.get(showMon), dayNum);
                    gridAdapter.notifyDataSetChanged();
                }
            });

            rightBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (++showMon == 13) {
                        showYear++;
                        showMon = 1;
                    }

                    mCal = Calendar.getInstance();
                    mCal.set(showYear, showMon - 1, 1);
                    int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
//1일 - 요일 매칭 시키기 위해 공백 add

                    tvDate.setText(showYear + "/" + showMon);

                    dayList.clear();
                    dayList.add(new fragCal.DayItem("0", "일", 0.0, ""));
                    dayList.add(new fragCal.DayItem("0", "월", 0.0, ""));
                    dayList.add(new fragCal.DayItem("0", "화", 0.0, ""));
                    dayList.add(new fragCal.DayItem("0", "수", 0.0, ""));
                    dayList.add(new fragCal.DayItem("0", "목", 0.0, ""));
                    dayList.add(new fragCal.DayItem("0", "금", 0.0, ""));
                    dayList.add(new fragCal.DayItem("0", "토", 0.0, ""));

                    for (int i = 1; i < dayNum; i++) {
                        dayList.add(new fragCal.DayItem("0", "", 0.0, ""));
                    }

                    setCalendarDate(mCal.get(Calendar.MONTH) + 2, dayNum);
                    gridAdapter.notifyDataSetChanged();
                }
            });

            toAddBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(), addList.class);
                    intent.putExtra("userID",cal_sport_userId); // id 넘기기
                    startActivity(intent);
                }
            });

            // 오늘에 날짜를 세팅 해준다.
            long now = System.currentTimeMillis();
            final Date date = new Date(now);
            //연,월,일을 따로 저장
            final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
            final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
            final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

            //해당 날짜 텍스트 컬러,배경 변경
            mCal = Calendar.getInstance();
            //오늘 day 가져옴
            Integer today = mCal.get(Calendar.DAY_OF_MONTH);
            String sToday = String.valueOf(today);
            if (sToday.equals(getItem(position))) { //오늘 day 텍스트 컬러 변경
                holder.tvItemDay.setTextColor(getResources().getColor(R.color.color_000000));
            }


            //textview 추가
            holder.tvItemWorks.removeAllViews();
            container = holder.tvItemWorks;

            return convertView;
        }


    }//adaptor끝

    private class GetDataFoodKcal extends AsyncTask<String, Void, String> { //칼로리 계산

        String errorString = null;
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://10.0.2.2:8012/calfood_select.php";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

//            progressDialog.dismiss();
                Log.d(TAG, "Activity- response - " + s);

                if (s == null) {
                    //textview("안되네요~~~");
                } else {
                    mJsonString = s;

                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray results = jsonObject.getJSONArray("response");

                    for (int i = 0; i < results.length(); ++i) {
                        JSONObject temp = results.getJSONObject(i);

                        String inpDay = temp.getString("cal_food_date");
                        //String inp = temp.getString("cal_food_kcal");

                        Double dd = Double.parseDouble(temp.getString("cal_food_kcal"));
                        if(map.containsKey(inpDay)==true){
                            Double tt = map.get(inpDay);
                            map.remove(inpDay);
                            map.put(inpDay, tt +Double.parseDouble(temp.getString("cal_food_kcal")));
                        } else{ map.put(temp.getString("cal_food_date"), Double.parseDouble(temp.getString("cal_food_kcal"))); }
                    }
                }
            } catch (JSONException e) {
                Log.d(TAG, "POST 에러: " + e);
            }

            gridView.setAdapter(gridAdapter);
        }

        @Override
        protected String doInBackground(String... params) {
            String id=params[0];
            String postParam="cal_food_userID="+id;

            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream=httpURLConnection.getOutputStream();
                outputStream.write(postParam.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode=httpURLConnection.getResponseCode();
                Log.d(TAG,"response code - "+responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line;//temp가 line임
                StringBuilder sb = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
            }
            return null;
        }
    }

    private class GetDataExerciseCheck extends AsyncTask<String, Void, String> { //운동체크

        String errorString = null;
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://10.0.2.2:8012/calsports_select.php";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

//            progressDialog.dismiss();
                Log.d(TAG, "Activity- response - " + s);

                if (s == null) {
                    //textview("안되네요~~~");
                } else {
                    mJsonString = s;

                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray results = jsonObject.getJSONArray("response");

                    for (int i = 0; i < results.length(); ++i) {
                        JSONObject temp = results.getJSONObject(i);

                        map2.put(temp.getString("cal_sport_date"), temp.getString("cal_sport_name"));
                    }
                }
            } catch (JSONException e) {
                Log.d(TAG, "POST 에러: " + e);
            }

            gridView.setAdapter(gridAdapter);
        }

        @Override
        protected String doInBackground(String... params) {
            String id=params[0];
            String postParam="cal_sport_userID="+id;

            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream=httpURLConnection.getOutputStream();
                outputStream.write(postParam.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode=httpURLConnection.getResponseCode();
                Log.d(TAG,"response code - "+responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line;//temp가 line임
                StringBuilder sb = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
            }
            return null;
        }
    }

    private class ViewHolder {
        TextView tvItemDay;
        TextView tvItemfoodKCAL;
        TextView tvItemExercise;
        LinearLayout tvItemWorks;

        public void bind(String day, String kcal, String set) {
            tvItemDay.setText(day);

            if(kcal.equals(""))tvItemfoodKCAL.setText(kcal);
            else tvItemfoodKCAL.setText(kcal+"kcal");

            if(set.equals("")) tvItemExercise.setText(set);
            else tvItemExercise.setText("DONE");
        }
    }

}