package com.ourincheon.wazap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncStatusObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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
    Contests applies;
    ArrayList<ContestData> apply_list;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_list);

        mListView = (ListView) findViewById(R.id.listView);
        mListView2 = (ListView)findViewById(R.id.listView1);

        scrollView = (ScrollView) findViewById(R.id.scrollView1);
        //ListView listView1 = (ListView) findViewById(R.id.listView1);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String access_token = pref.getString("access_token", "");

        apply_list = new ArrayList<ContestData>();

        loadApply(access_token);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ContestData mData = mAdapter.mListData.get(position);
                Toast.makeText(ApplyList.this, "******"+mData.getApplies_id(), Toast.LENGTH_SHORT).show();
            }
        });


        mAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView2.setAdapter(mAdapter);



        //mAdapter.addItem("qewrqwe");
    /*
*/

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
                        System.out.println(count);
                        for (int i = 0; i < count; i++) {
                            System.out.println(Integer.parseInt(jsonArr.getJSONObject(i).getString("recruitment")));
                            System.out.println(jsonArr.getJSONObject(i).getString("recruitment"));
                            mAdapter.addItem(jsonArr.getJSONObject(i).getString("title"),
                                    jsonArr.getJSONObject(i).getString("period"),
                                    Integer.parseInt(jsonArr.getJSONObject(i).getString("applies_id")),
                                    Integer.parseInt(jsonArr.getJSONObject(i).getString("appliers")),
                                    Integer.parseInt(jsonArr.getJSONObject(i).getString("recruitment")),
                                    Integer.parseInt(jsonArr.getJSONObject(i).getString("contests_id")),
                                    Integer.parseInt(jsonArr.getJSONObject(i).getString("members")));
                        }

                        mListView.setAdapter(mAdapter);
                        mListView2.setAdapter(mAdapter);
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
            holder.Dday.setText("D - "+day.dday(mData.getPeriod()));

            holder.Title.setText(mData.getTitle());

            holder.Cate.setText("모집인원 " + String.valueOf(mData.getRecruitment()) + "명");

            holder.Man.setText("신청인원 "+mData.getAppliers() + "명");

            holder.Member.setText("확정인원 " + mData.getMembers() + "명");

            return convertView;
        }
    }
}
