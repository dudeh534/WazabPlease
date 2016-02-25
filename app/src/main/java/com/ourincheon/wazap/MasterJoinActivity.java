package com.ourincheon.wazap;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.ourincheon.wazap.Retrofit.ContestData;
import com.ourincheon.wazap.Retrofit.reqContest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MasterJoinActivity extends AppCompatActivity {

    reqContest contest;
    ContestData contestData;
    TextView jTitle,jButton;
    String access_token,num;
    AlertDialog.Builder ad,deleteD;
    Button eBtn;
    CharSequence list[] = {"수정하기", "삭제하기","취소"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_join);

        contestData = new ContestData();

        jTitle = (TextView) findViewById(R.id.jmTitle);

        Intent intent = getIntent();
        num =  intent.getExtras().getString("id");
        loadPage(num);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        access_token = pref.getString("access_token", "");


        deleteD = new AlertDialog.Builder(this);
        deleteD.setMessage("프로그램을 종료 하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // endContest(num, access_token);
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // 마감버튼
        jButton = (TextView) findViewById(R.id.jmButton);
        jButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert =  deleteD.create();
                alert.show();
            }
        });

        ad = new AlertDialog.Builder(this);
        ad.setTitle("팀원모집");
        ad.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),
                        "You Choose : " + list[which],
                        Toast.LENGTH_LONG).show();
                if(which == 0)
                   editCont();
                else if(which == 1)
                    dialog.cancel();
                else if(which == 2)
                    dialog.cancel();
            }
        });

        // 수정,삭제버튼
        eBtn = (Button) findViewById(R.id.jmEdit);
        eBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.show();
            }
        });

    }

    void editCont()
    {
        Intent intent = new Intent(MasterJoinActivity.this, RecruitActivity.class);
        intent.putExtra("edit",1);
        intent.putExtra("contestD",contestData);
        startActivity(intent);
    }


    void endContest(String num, String access_token)
    {
        /*
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://come.n.get.us.to/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WazapService service = retrofit.create(WazapService.class);

        System.out.println("-------------------"+access_token);
        Call<LinkedTreeMap> call = service.applyContests(num,access_token);
        call.enqueue(new Callback<LinkedTreeMap>() {
            @Override
            public void onResponse(Response<LinkedTreeMap> response) {
                if (response.isSuccess() && response.body() != null) {

                    LinkedTreeMap temp = response.body();

                    boolean result = Boolean.parseBoolean(temp.get("result").toString());
                    String msg = temp.get("msg").toString();

                    if (result) {
                        Log.d("저장 결과: ", msg);
                        Toast.makeText(getApplicationContext(), "신청되었습니다.", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("저장 실패: ", msg);
                        Toast.makeText(getApplicationContext(), "신청 안됬습니다.다시 시도해주세요.", Toast.LENGTH_SHORT).show();
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
        */
    }

    void loadPage(String num)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://come.n.get.us.to/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WazapService service = retrofit.create(WazapService.class);

        System.out.println("-----------------------------"+num);

        Call<reqContest> call = service.getConInfo(num);
        call.enqueue(new Callback<reqContest>() {
            @Override
            public void onResponse(Response<reqContest> response) {
                if (response.isSuccess() && response.body() != null) {

                    Log.d("SUCCESS", response.message());
                    contest = response.body();

                    //user = response.body();
                    Log.d("SUCCESS", contest.getMsg());


                    System.out.println(contest.getData().getTitle());
                    contestData.setTitle(contest.getData().getTitle());
                    contestData.setHosts(contest.getData().getHosts());
                    contestData.setRecruitment(contest.getData().getRecruitment());
                    contestData.setCategories(contest.getData().getCategories());
                    contestData.setPeriod(contest.getData().getPeriod());
                    contestData.setCover(contest.getData().getCover());
                    contestData.setPositions(contest.getData().getPositions());

                    jTitle.setText(contestData.getTitle());
                  /* String result = new Gson().toJson(contest);
                    Log.d("SUCESS-----", result);

                    JSONObject jsonRes;
                    try {
                        jsonRes = new JSONObject(result);
                        JSONArray jsonArr = jsonRes.getJSONArray("data");
                        System.out.println("--------------" + jsonArr.length());
                         Log.d("title", jsonArr.getJSONObject(0).getString("title"));

                    } catch (JSONException e) {
                    }
                */
                } else if (response.isSuccess()) {
                    Log.d("Response Body isNull", response.message());
                } else {
                    Log.d("Response Error Body", response.errorBody().toString());
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                Log.e("Error", t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_join, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}