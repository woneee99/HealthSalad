package com.example.banksalad.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.banksalad.AddSportPlanActivity;
import com.example.banksalad.go;
import com.example.banksalad.R;

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
import static java.lang.Integer.max;

public class fragPlan extends Fragment {
    private View view;
    /**
     * 연/월 텍스트뷰
     */
    private TextView tvDate;
    /**
     * 그리드뷰 어댑터
     */
    private fragPlan.GridAdapter gridAdapter;

    /**
     * 일 저장 할 리스트
     */
    private class DayItem {
        private String Type;
        private String Day;
        private String Set;

        public void setSet(String set) {
            Set = set;
        }

        public String getType() {
            return Type;
        }

        public String getDay() {
            return Day;
        }

        public String getSet() {
            return Set;
        }

        public DayItem(String type, String day, String set) {
            Type = type;
            Day = day;
            Set = set;
        }
    }
    private ArrayList<fragPlan.DayItem> dayList;
    private Map<String,Integer> map;

    /**
     * 그리드뷰
     */
    private GridView gridView;

    /**
     * 왼쪽, 오른쪽 버튼
     */
    private Button leftBtn, rightBtn, toAddBtn;

    /**
     * 캘린더 변수
     */
    private Calendar mCal;
    private int showYear;
    private int showMon;
    private int showDay;
    String pickdays;
    String todays;
    TextView todaytv;


    private LinearLayout container;
    String mJsonString;

    private class DbItem{
        String day;
        String sport;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getSport() {
            return sport;
        }

        public void setSport(String sport) {
            this.sport = sport;
        }

        public DbItem(String day, String sport) {
            this.day = day;
            this.sport = sport;
        }
    }

    ArrayList<fragPlan.DbItem> dbList;
    int lastidx;
    String user_id;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.activity_calendar, container, false);

        dbList = new ArrayList<>();
        map=new HashMap<>();
//        Intent addintent=new Intent(getActivity(),AddSportPlanActivity.class);//intent설정


        user_id = getArguments().getString("userID");//main에서 받아옴

        Log.d(TAG,"go에서 받아오긴 할거임"+user_id);



        fragPlan.GetData task = new fragPlan.GetData();

        Log.d(TAG,"넘겨주는 id"+user_id);
        task.execute(user_id);


        tvDate = (TextView) view.findViewById(R.id.tv_date);
        gridView = (GridView) view.findViewById(R.id.gridview);
        leftBtn = (Button) view.findViewById(R.id.pre_btn);
        rightBtn = (Button) view.findViewById(R.id.next_btn);
        toAddBtn=(Button)view.findViewById(R.id.toAddBtn);

        // 오늘에 날짜를 세팅 해준다.
        long now = System.currentTimeMillis();
        final Date date = new Date(now);
        //연,월,일을 따로 저장
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        showYear = Integer.parseInt(curYearFormat.format(date));
        showMon = Integer.parseInt(curMonthFormat.format(date));
        showDay=Integer.parseInt(curDayFormat.format(date));

        //나중에 추가페이지에 넘겨줄 선택 날짜 String
        pickdays=""+showYear;
        pickdays+=(showMon<10)? "0"+showMon:showMon;
        pickdays+=(showDay<10)? "0"+showDay:showDay;
        todays=pickdays;


        //현재 날짜 텍스트뷰에 뿌려줌
        tvDate.setText(curYearFormat.format(date) + "/" + curMonthFormat.format(date));

        //gridview 요일 표시
        dayList = new ArrayList<>();
        dayList.add(new fragPlan.DayItem("0", "",  ""));
        dayList.add(new fragPlan.DayItem("0", "일",  ""));
        dayList.add(new fragPlan.DayItem("0", "월",  ""));
        dayList.add(new fragPlan.DayItem("0", "화",  ""));
        dayList.add(new fragPlan.DayItem("0", "수",  ""));
        dayList.add(new fragPlan.DayItem("0", "목",  ""));
        dayList.add(new fragPlan.DayItem("0", "금",  ""));
        dayList.add(new fragPlan.DayItem("0", "토",  ""));

        mCal = Calendar.getInstance();

        //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        //1일 - 요일 매칭 시키기 위해 공백 add

        dayList.add(new fragPlan.DayItem("0", "1주차", ""));
        for (int i = 1; i < dayNum; i++) {
            dayList.add(new fragPlan.DayItem("0", "",  ""));
        }
        setCalendarDate(mCal.get(Calendar.MONTH) + 1, dayNum);


        gridAdapter = new fragPlan.GridAdapter(getActivity(), dayList);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a_parent, View a_view, int a_position, long a_id) {
                fragPlan.DayItem item = gridAdapter.getItem(a_position);
                if(!dayList.get(a_position).getType().equals("0")) {
                    int pDay = Integer.parseInt(item.getDay());

                    pickdays = "" + showYear;
                    pickdays += (showMon < 10) ? "0" + showMon : showMon;
                    pickdays += (pDay < 10) ? "0" + pDay : pDay;

                    Log.d(TAG,"선택 날짜 "+pickdays);
                }
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
            dayList.add(new fragPlan.DayItem("1", "" + (i + 1), "0세트"));
            if (j % 7 == 0)
                dayList.add(new fragPlan.DayItem("0", "" + ((j / 8) + 2) + "주차", ""));//j:처음=1, 칸 수
        }

    }

    /**
     * 그리드뷰 어댑터
     */
    private class GridAdapter extends BaseAdapter {

        private final List<fragPlan.DayItem> list;
        private final LayoutInflater inflater;


        /**
         * 생성자
         *
         * @param context
         * @param list
         */
        public GridAdapter(Context context, List<fragPlan.DayItem> list) {
            this.list = list;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public fragPlan.DayItem getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            fragPlan.ViewHolder holder = null;


            String posDays="0";
            if(!dayList.get(position).getType().equals("0")){
                int pDay=Integer.parseInt(dayList.get(position).getDay());
                posDays=showYear+"";
                posDays+=(showMon<10)? "0"+showMon:showMon;
                posDays+=(pDay<10)? "0"+pDay:pDay;

                if(map.containsKey(posDays)==true) {
                    Log.d(TAG,map.get(posDays)+"");
                    dayList.get(position).setSet(map.get(posDays).toString()+"세트");
                }

            }

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);
//                listView=inflater.inflate()
                holder = new fragPlan.ViewHolder();

                holder.tvItemDay = (TextView) convertView.findViewById(R.id.tv_item_gridview);
                holder.tvItemWorkSet = (TextView) convertView.findViewById(R.id.tv_item_workset);
                holder.tvItemWorks = (LinearLayout) convertView.findViewById(R.id.listview);
                convertView.setTag(holder);

            } else {
                holder = (fragPlan.ViewHolder) convertView.getTag();
            }
            holder.bind(dayList.get(position).getDay(), dayList.get(position).getSet());


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
                    dayList.add(new fragPlan.DayItem("0", "",  ""));
                    dayList.add(new fragPlan.DayItem("0", "일",  ""));
                    dayList.add(new fragPlan.DayItem("0", "월",  ""));
                    dayList.add(new fragPlan.DayItem("0", "화",  ""));
                    dayList.add(new fragPlan.DayItem("0", "수",  ""));
                    dayList.add(new fragPlan.DayItem("0", "목",  ""));
                    dayList.add(new fragPlan.DayItem("0", "금",  ""));
                    dayList.add(new fragPlan.DayItem("0", "토",  ""));

                    dayList.add(new fragPlan.DayItem("0", "1주차",  ""));
                    for (int i = 1; i < dayNum; i++) {
                        dayList.add(new fragPlan.DayItem("0", "",  ""));
                    }

                    todaytv.setTextColor(getResources().getColor(R.color.black));

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
                    dayList.add(new fragPlan.DayItem("0", "",  ""));
                    dayList.add(new fragPlan.DayItem("0", "일",  ""));
                    dayList.add(new fragPlan.DayItem("0", "월",  ""));
                    dayList.add(new fragPlan.DayItem("0", "화",  ""));
                    dayList.add(new fragPlan.DayItem("0", "수",  ""));
                    dayList.add(new fragPlan.DayItem("0", "목", ""));
                    dayList.add(new fragPlan.DayItem("0", "금",  ""));
                    dayList.add(new fragPlan.DayItem("0", "토",  ""));

                    dayList.add(new fragPlan.DayItem("0", "1주차",  ""));
                    for (int i = 1; i < dayNum; i++) {
                        dayList.add(new fragPlan.DayItem("0", "",  ""));
                    }

                    todaytv.setTextColor(getResources().getColor(R.color.black));

                    setCalendarDate(mCal.get(Calendar.MONTH) + 2, dayNum);
                    gridAdapter.notifyDataSetChanged();
                }
            });

            toAddBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(), AddSportPlanActivity.class);
                    Log.d(TAG,"넘겨주는 idx값"+lastidx);
                    intent.putExtra("idxcnt",lastidx);
                    intent.putExtra("user_id",user_id);
                    intent.putExtra("dayString",pickdays);
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


            if (todays.equals(posDays)) { //오늘 day 텍스트 컬러 변경
                Log.d(TAG,"오늘컬러~~");
                Log.d(TAG,"todays: "+todays+" posdays: "+posDays);
                TextView textView=holder.tvItemDay;
                textView.setTextColor(getResources().getColor(R.color.colorAccent));

                todaytv=holder.tvItemDay;
            }


            //textview 추가
            holder.tvItemWorks.removeAllViews();
            container = holder.tvItemWorks;




            if (!dayList.get(position).getType().equals("0")) {
                Log.d(TAG, "반복문 넘어옴~~, dbsize: "+dbList.size());
                for (int i = 0; i < dbList.size(); i++) {
                    if(posDays.equals(dbList.get(i).getDay())) {
                        textview(dbList.get(i).getSport());
                    }
                }
            }


            return convertView;
        }


    }//adaptor끝

    private class GetData extends AsyncTask<String, Void, String> {

        String errorString = null;
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://10.0.2.2/sportdate.php";
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
                    textview("안되네요~~~");
                } else {
                    mJsonString = s;

                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray results = jsonObject.getJSONArray("response");

                    for (int i = 0; i < results.length(); ++i) {
                        JSONObject temp = results.getJSONObject(i);

                        String inpDay=temp.getString("sport_date");
                        String inp = temp.getString("sport_name")+" ";
                        inp += temp.getString("sport_set");
                        dbList.add(new fragPlan.DbItem(inpDay,inp));

                        Log.d(TAG,"받아온 세트! "+temp.getString("sport_set"));

                        int mSet=temp.getInt("sport_set");
                        if(map.containsKey(inpDay)==true){
                            mSet+=(Integer)map.get(inpDay);
                        }
                        map.put(inpDay,mSet);

                        Log.d(TAG,"하나 Day: "+inpDay+"내용: "+inp);
                    }
                }
            } catch (JSONException e) {
                Log.d(TAG, "POST 에러~~: " + e);
            }

            gridAdapter.notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(String... params) {

            String id=params[0];
            String postParam="user_id="+id;

            Log.d(TAG,"보낼거: "+postParam);

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
        TextView tvItemWorkSet;
        LinearLayout tvItemWorks;

        public void bind(String day, String set) {
            tvItemDay.setText(day);
            tvItemWorkSet.setText(set);
        }
    }

    public void textview(String a) {
        if (a != null) {
            TextView view1 = new TextView(getActivity());

            String inp;
            if (a.length() > 10) {
                inp = a.substring(2, 10);
            } else inp = a;

            view1.setText(inp);
            view1.setTextSize(10);
            view1.setTextColor(Color.BLACK);
            view1.setPadding(1, 3, 3, 3);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.LEFT;
            view1.setLayoutParams(lp);

            container.addView(view1);
        } else {
            Log.d(TAG, "NULL인가벼~~~~~");
        }

    }

}