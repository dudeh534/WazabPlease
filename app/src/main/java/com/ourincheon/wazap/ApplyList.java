package com.ourincheon.wazap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.ourincheon.wazap.Retrofit.ContestData;
import com.ourincheon.wazap.Retrofit.Contests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ApplyList extends AppCompatActivity {
    ScrollView scrollView;
    private ListView mListView = null;
    private ListView mListView2 = null;
    private ListViewAdapter mAdapter = null;
    private Not_ListViewAdapter  not_listAdapter = null;
    Contests applies;
    ArrayList<ContestData> apply_list;
    int count, posi;
    String[] cont_id,apply_id;
    AlertDialog dialog;
    String access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_list);

        mListView = (ListView) findViewById(R.id.listView);
        mListView2 = (ListView)findViewById(R.id.listView1);

        scrollView = (ScrollView) findViewById(R.id.scrollView1);
        //ListView listView1 = (ListView) findViewById(R.id.listView1);
        //not_listAdapter = new Not_ListAdapter(this, R.layout.not_require_item);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        access_token = pref.getString("access_token", "");

        apply_list = new ArrayList<ContestData>();

        loadApply(access_token);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("찜 목록 지우기").setMessage("해당 스크랩을 지우시겠습니까?")
                .setCancelable(true).setPositiveButton("지우기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
                System.out.println("----------------------"+posi);
                deleteApply(cont_id[posi],apply_id[posi]);
               // loadApply(access_token);
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog = builder.create();

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ApplyList.this, "취소", Toast.LENGTH_SHORT).show();
                posi = position;
                dialog.show();
                return false;
            }
        });

        mAdapter = new ListViewAdapter(this);
        not_listAdapter = new Not_ListViewAdapter(this);

        mListView.setAdapter(mAdapter);
        mListView2.setAdapter(not_listAdapter);
    }

    void deleteApply(String contest, String apply)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://come.n.get.us.to/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WazapService service = retrofit.create(WazapService.class);

        System.out.println("-------------------"+access_token);

        Call<LinkedTreeMap> call = service.delApply(contest, apply, access_token);
        call.enqueue(new Callback<LinkedTreeMap>() {
            @Override
            public void onResponse(Response<LinkedTreeMap> response) {
                if (response.isSuccess() && response.body() != null) {

                    LinkedTreeMap temp = response.body();

                    boolean result = Boolean.parseBoolean(temp.get("result").toString());
                    String msg = temp.get("msg").toString();

                    if (result) {
                        Log.d("저장 결과: ", msg);
                        Toast.makeText(getApplicationContext(), "신청 취소되었습니다.", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("저장 실패: ", msg);
                        Toast.makeText(getApplicationContext(), "신청취소 안됬습니다.다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }

                } else if (response.isSuccess()) {
                    Log.d("Response Body isNull", response.message());
                } else {
                    Log.d("Response Error Body", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                Log.e("Error", t.getMessage());
            }
        });
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    void loadApply(String access_token)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://come.n.get.us.to/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WazapService service = retrofit.create(WazapService.class);

        Call<Contests> call = service.getAppplylist(access_token, 10, 10);
        call.enqueue(new Callback<Contests>() {
            @Override
            public void onResponse(Response<Contests> response) {
                if (response.isSuccess() && response.body() != null) {

                    Log.d("SUCCESS", response.message());
                    applies = response.body();

                    String result = new Gson().toJson(applies);
                    Log.d("SUCESS-----", result);

                    JSONObject jsonRes;
                    try {
                        jsonRes = new JSONObject(result);
                        JSONArray jsonArr = jsonRes.getJSONArray("data");
                        count = jsonArr.length();
                        cont_id = new String[count];
                        apply_id = new String[count];
                        System.out.println(count);
                        for (int i = 0; i < count; i++) {
                            System.out.println(Integer.parseInt(jsonArr.getJSONObject(i).getString("recruitment")));
                            System.out.println(jsonArr.getJSONObject(i).getString("recruitment"));
                            System.out.println(jsonArr.getJSONObject(i).getString("is_finish"));
                            if(Integer.parseInt(jsonArr.getJSONObject(i).getString("is_finish")) == 0) {
                                cont_id[i] = jsonArr.getJSONObject(i).getString("contests_id");
                                apply_id[i] = jsonArr.getJSONObject(i).getString("applies_id");

                                mAdapter.addItem(jsonArr.getJSONObject(i).getString("title"),
                                        jsonArr.getJSONObject(i).getString("period"),
                                        Integer.parseInt(jsonArr.getJSONObject(i).getString("applies_id")),
                                        Integer.parseInt(jsonArr.getJSONObject(i).getString("appliers")),
                                        Integer.parseInt(jsonArr.getJSONObject(i).getString("recruitment")),
                                        Integer.parseInt(jsonArr.getJSONObject(i).getString("contests_id")),
                                        Integer.parseInt(jsonArr.getJSONObject(i).getString("members")));
                            }else{
                                not_listAdapter.addItem(jsonArr.getJSONObject(i).getString("title"),
                                        jsonArr.getJSONObject(i).getString("period"),
                                        Integer.parseInt(jsonArr.getJSONObject(i).getString("applies_id")),
                                        Integer.parseInt(jsonArr.getJSONObject(i).getString("appliers")),
                                        Integer.parseInt(jsonArr.getJSONObject(i).getString("recruitment")),
                                        Integer.parseInt(jsonArr.getJSONObject(i).getString("contests_id")),
                                        Integer.parseInt(jsonArr.getJSONObject(i).getString("members")));
                            }
                        }
                        mListView.setAdapter(mAdapter);
                        mListView2.setAdapter(not_listAdapter);

                        setListViewHeightBasedOnChildren(mListView);
                        setListViewHeightBasedOnChildren(mListView2);
                    } catch (JSONException e) {
                    }

                } else if (response.isSuccess()) {
                    Log.d("Response Body isNull", response.message());
                } else {
                    Log.d("Response Error Body", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                Log.e("Error", t.getMessage());
            }
        });
    }



    private class ViewHolder {
        // public ImageView mIcon;
        public TextView Dday;
        public TextView Title;
        public TextView Cate;
        public TextView Man;
        public TextView Member;
    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ContestData> mListData = new ArrayList<ContestData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItem(String title, String period, int apply_id, int apply, int recruit, int id, int member){
            ContestData addInfo = null;
            addInfo = new ContestData();
            String[] parts = period.split("T");
            addInfo.setPeriod(parts[0]);
            addInfo.setApplies_id(apply_id);
            addInfo.setAppliers(apply);
            addInfo.setRecruitment(recruit);
            addInfo.setContests_id(id);
            addInfo.setMembers(member);
            addInfo.setTitle(title);

            System.out.println("----------------33333333   " + member);
            System.out.println("-------------------------"+addInfo.getMembers());
            mListData.add(addInfo);

        }

        public void remove(int position){
            mListData.remove(position);
            dataChange();
        }

        public void dataChange(){
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.apply_item, null);

                holder.Dday = (TextView) convertView.findViewById(R.id.dday);
                holder.Title = (TextView) convertView.findViewById(R.id.title);
                holder.Cate = (TextView) convertView.findViewById(R.id.cate);
                holder.Man = (TextView) convertView.findViewById(R.id.man);
                holder.Member = (TextView) convertView.findViewById(R.id.member);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            ContestData mData = mListData.get(position);

            // System.out.println(mData.getAppliers());

            Dday day = new Dday();
            holder.Dday.setText("D "+day.dday(mData.getPeriod()));

            holder.Title.setText(mData.getTitle());

            holder.Cate.setText("모집인원 " + String.valueOf(mData.getRecruitment()) + "명");

            holder.Man.setText("신청인원 "+mData.getAppliers() + "명");

            holder.Member.setText("확정인원 " + mData.getMembers() + "명");

            return convertView;
        }
    }

    private class Not_ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ContestData> mListData = new ArrayList<ContestData>();

        public Not_ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItem(String title, String period, int apply_id, int apply, int recruit, int id, int member){
            ContestData addInfo = null;
            addInfo = new ContestData();
            String[] parts = period.split("T");
            addInfo.setPeriod(parts[0]);
            addInfo.setApplies_id(apply_id);
            addInfo.setAppliers(apply);
            addInfo.setRecruitment(recruit);
            addInfo.setContests_id(id);
            addInfo.setMembers(member);
            addInfo.setTitle(title);

            System.out.println("----------------33333333   " + member);
            System.out.println("-------------------------"+addInfo.getMembers());
            mListData.add(addInfo);

        }

        public void remove(int position){
            mListData.remove(position);
            dataChange();
        }

        public void dataChange(){
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.not_require_item, null);

                holder.Dday = (TextView) convertView.findViewById(R.id.dday);
                holder.Title = (TextView) convertView.findViewById(R.id.title);
                holder.Cate = (TextView) convertView.findViewById(R.id.cate);
                holder.Man = (TextView) convertView.findViewById(R.id.man);
                holder.Member = (TextView) convertView.findViewById(R.id.member);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            ContestData mData = mListData.get(position);

            // System.out.println(mData.getAppliers());

            Dday day = new Dday();
            // holder.Dday.setText("D "+day.dday(mData.getPeriod()));

            holder.Title.setText(mData.getTitle());

            holder.Cate.setText("모집인원 " + String.valueOf(mData.getRecruitment()) + "명");

            holder.Man.setText("신청인원 "+mData.getAppliers() + "명");

            holder.Member.setText("확정인원 " + mData.getMembers() + "명");

            return convertView;
        }
    }


/*
    public class not_ListAdapter extends ArrayAdapter<String> {
        private final Activity context;
        TextView dday, title, cate, man;
        ImageView small;
        Button bt;
        Contests contest;
        ArrayList<ContestData> cont_list;
        int count;

        public not_ListAdapter(Activity context, int resource) {
            super(context, resource);
            this.context = context;
            //list = new ArrayList<Contests>();
            SharedPreferences pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
            String access_token = pref.getString("access_token", "");
            //loadPage(access_token);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.not_require_item, null, true);
            dday = (TextView) rowView.findViewById(R.id.dday);
            title = (TextView) rowView.findViewById(R.id.title);
            cate = (TextView) rowView.findViewById(R.id.cate);
            man = (TextView) rowView.findViewById(R.id.man);
            bt = (Button) rowView.findViewById(R.id.require);

            title.setText("[서울] 한화생명 보험 아이디어 공모전");
            cate.setText("영상/ucc/사진");
            man.setText("모집인원 5명");
            return rowView;
        }

        @Override
        public int getCount() {
            return cont_list.size();
        }
    }*/
}