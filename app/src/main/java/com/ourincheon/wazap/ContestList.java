package com.ourincheon.wazap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class ContestList extends AppCompatActivity {
    ScrollView scrollView;
    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;
    Contests contests;
    ArrayList<ContestData> contest_list;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_list);

        mListView = (ListView) findViewById(R.id.contestlistView);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String access_token = pref.getString("access_token", "");
        String user_id = pref.getString("user_id", "");

        contest_list = new ArrayList<ContestData>();

        loadContest(user_id, access_token);

    /*    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ContestData mData = mAdapter.mListData.get(position);
                Toast.makeText(ClipList.this, mData.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
*/
        mAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);



    }

    void loadContest(String user_id, String access_token)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://come.n.get.us.to/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WazapService service = retrofit.create(WazapService.class);

        Call<Contests> call = service.getContestlist(user_id,access_token);
        call.enqueue(new Callback<Contests>() {
            @Override
            public void onResponse(Response<Contests> response) {
                if (response.isSuccess() && response.body() != null) {

                    Log.d("SUCCESS", response.message());
                    contests = response.body();

                    String result = new Gson().toJson(contests);
                    Log.d("SUCESS-----", result);

                    JSONObject jsonRes;
                    try {
                        jsonRes = new JSONObject(result);
                        JSONArray jsonArr = jsonRes.getJSONArray("data");
                        count = jsonArr.length();
                        System.out.println(count);
                        for (int i = 0; i < count; i++) {

                            mAdapter.addItem(jsonArr.getJSONObject(i).getString("title"),
                                    jsonArr.getJSONObject(i).getString("period"),
                                    Integer.parseInt(jsonArr.getJSONObject(i).getString("appliers")),
                                    Integer.parseInt(jsonArr.getJSONObject(i).getString("recruitment")),
                                    Integer.parseInt(jsonArr.getJSONObject(i).getString("contests_id")),
                                    Integer.parseInt(jsonArr.getJSONObject(i).getString("members")));
                        }
                        mAdapter.notifyDataSetChanged();
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

        public void addItem(String title, String period, int apply, int recruit, int id, int member){
            ContestData addInfo = null;
            addInfo = new ContestData();
            String[] parts = period.split("T");
            addInfo.setPeriod(parts[0]);
            addInfo.setAppliers(apply);
            addInfo.setRecruitment(recruit);
            addInfo.setContests_id(id);
            addInfo.setMembers(member);
            addInfo.setTitle(title);

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
                convertView = inflater.inflate(R.layout.contest_item, null);

                holder.Dday = (TextView) convertView.findViewById(R.id.rdday);
                holder.Title = (TextView) convertView.findViewById(R.id.rtitle);
                holder.Cate = (TextView) convertView.findViewById(R.id.rcate);
                holder.Man = (TextView) convertView.findViewById(R.id.rman);
                holder.Member = (TextView) convertView.findViewById(R.id.rmember);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            ContestData mData = mListData.get(position);

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

