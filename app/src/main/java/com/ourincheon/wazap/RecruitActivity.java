package com.ourincheon.wazap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.ourincheon.wazap.KaKao.infoKaKao;
import com.ourincheon.wazap.Retrofit.ContestData;
import com.ourincheon.wazap.Retrofit.ContestInfo;
import com.ourincheon.wazap.facebook.HttpService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Hsue.
 */

public class RecruitActivity extends AppCompatActivity {

    EditText reTitle, reHost, eNum, erIntro;
    TextView save;
    ImageView profileImg;
    String thumbnail;
    ContestInfo contest2;
    int mode,contest_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit);

        TextView nickname = (TextView) findViewById(R.id.leaderTxt);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        nickname.setText(pref.getString("name",""));
        String access_token = pref.getString("access_token", "");
        contest2 = new ContestInfo();
        contest2.setAccess_token(access_token);

        reTitle = (EditText) findViewById(R.id.reTitle);
        reHost = (EditText) findViewById(R.id.reHost);
        eNum = (EditText) findViewById(R.id.eNum);
        erIntro = (EditText) findViewById(R.id.erIntro);

        profileImg = (ImageView)findViewById(R.id.Pro);
        thumbnail = pref.getString("profile_img","");
        ThumbnailImage thumb = new ThumbnailImage(thumbnail, profileImg);
        thumb.execute();

        Intent intent = getIntent();
        mode = intent.getExtras().getInt("edit");
        if(mode==1) {
            System.out.println("----------------------------------------------");
            ContestData con = new ContestData();
            con = (ContestData) intent.getExtras().getSerializable("contestD");
            reTitle.setText(con.getTitle());
            reHost.setText(con.getHosts());
            eNum.setText(String.valueOf(con.getRecruitment()));
            erIntro.setText(con.getCover());
            contest_id = con.getContests_id();
        }

        save = (TextView) findViewById(R.id.textView12);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "adshf;aksdf", Toast.LENGTH_SHORT).show();
                contest2.setTitle(reTitle.getText().toString());
                contest2.setRecruitment(Integer.parseInt(eNum.getText().toString()));
                contest2.setHosts(reHost.getText().toString());
                contest2.setCover(erIntro.getText().toString());

                contest2.setCategories("디자인/UCC");
                contest2.setPeriod("2016-03-15");
                contest2.setPositions("개발자");

                if(mode == 0)
                    sendContest(contest2);
                else
                    editCon(contest2);

                finish();
            }
        });
    }

    void editCon(ContestInfo contest)
    {
        String baseUrl = "http://come.n.get.us.to/";
        Retrofit client = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WazapService service = client.create(WazapService.class);

        System.out.println("!!!!!!!!!!!!!!!!!!!"+contest_id);
        Call<LinkedTreeMap> call = service.editContest(String.valueOf(contest_id), contest);
        call.enqueue(new Callback<LinkedTreeMap>() {
            @Override
            public void onResponse(Response<LinkedTreeMap> response) {
                if (response.isSuccess() && response.body() != null) {
                    LinkedTreeMap temp = response.body();

                    boolean result = Boolean.parseBoolean(temp.get("result").toString());
                    String msg = temp.get("msg").toString();


                    if (result) {
                        Log.d("수정 결과: ", msg);
                        Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("수정 실패: ", msg);
                        Toast.makeText(getApplicationContext(), "수정이 안됬습니다.다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }

                } else if (response.isSuccess()) {
                    Log.d("Response Body is NULL", response.message());
                    Toast.makeText(getApplicationContext(), "수정이 안됬습니다.다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("Response Error Body", response.errorBody().toString());
                    System.out.println("------------------"+contest2.getAccess_token());
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    void sendContest(ContestInfo contest)
    {
        String baseUrl = "http://come.n.get.us.to/";
        Retrofit client = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WazapService service = client.create(WazapService.class);

        Call<LinkedTreeMap> call = service.createContests(contest);
        call.enqueue(new Callback<LinkedTreeMap>() {
            @Override
            public void onResponse(Response<LinkedTreeMap> response) {
                if (response.isSuccess() && response.body() != null) {
                    LinkedTreeMap temp = response.body();

                    boolean result = Boolean.parseBoolean(temp.get("result").toString());
                    String msg = temp.get("msg").toString();


                    if (result) {
                        Log.d("저장 결과: ", msg);
                        Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("저장 실패: ", msg);
                        Toast.makeText(getApplicationContext(), "저장이 안됬습니다.다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }

                } else if (response.isSuccess()) {
                    Log.d("Response Body is NULL", response.message());
                    Toast.makeText(getApplicationContext(), "저장이 안됬습니다.다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("Response Error Body", response.errorBody().toString());
                    System.out.println("------------------"+contest2.getAccess_token());
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

}