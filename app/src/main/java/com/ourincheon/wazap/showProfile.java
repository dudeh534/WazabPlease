package com.ourincheon.wazap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ourincheon.wazap.Retrofit.regUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class showProfile extends AppCompatActivity {
    ImageView profileImg;
    String thumbnail;
    regUser reguser;
    private TextView sName, sMajor, sUniv, sLoc, sKakao, sIntro, sExp;
    int flag;
    TextView pButton;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        Intent intent = getIntent();
        System.out.println(intent.getExtras().getString("thumbnail"));
        System.out.println(intent.getExtras().getString("user_id"));
        flag = intent.getExtras().getInt("flag");

        sName = (TextView) findViewById(R.id.pName);
        sMajor = (TextView)  findViewById(R.id.pMajor);
        sUniv = (TextView)  findViewById(R.id.pUniv);
        sLoc = (TextView)  findViewById(R.id.pLoc);
        sKakao = (TextView)  findViewById(R.id.pKakao);
        sIntro = (TextView) findViewById(R.id.pIntro);
        sExp = (TextView) findViewById(R.id.pExp);

        profileImg = (ImageView)findViewById(R.id.pPro);
        thumbnail = intent.getExtras().getString("thumbnail");
        ThumbnailImage thumb = new ThumbnailImage(thumbnail, profileImg);
        thumb.execute();
        user_id =intent.getExtras().getString("user_id");

        loadPage(user_id);

        pButton = (TextView) findViewById(R.id.pButton);
        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                Toast.makeText(showProfile.this, String.valueOf(flag), Toast.LENGTH_SHORT).show();
                loadPage(user_id);
            }
        });
    }

    void loadPage(String user_id)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://come.n.get.us.to/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WazapService service = retrofit.create(WazapService.class);

        Call<regUser> call = service.getUserInfo(user_id);
        call.enqueue(new Callback<regUser>() {
            @Override
            public void onResponse( Response<regUser> response) {
                if (response.isSuccess() && response.body() != null) {

                    Log.d("SUCCESS", response.message());
                    reguser = response.body();

                    //user = response.body();
                    //Log.d("SUCCESS", reguser.getMsg());

                    String result = new Gson().toJson(reguser);
                    Log.d("SUCESS-----",result);

                    JSONObject jsonRes;
                    try{
                        jsonRes = new JSONObject(result);
                        JSONArray jsonArr = jsonRes.getJSONArray("data");
                        Log.d("username", jsonArr.getJSONObject(0).getString("username"));
                        sName.setText(jsonArr.getJSONObject(0).getString("username"));
                        sMajor.setText(jsonArr.getJSONObject(0).getString("major"));
                        sUniv.setText(jsonArr.getJSONObject(0).getString("school"));
                        sLoc.setText(jsonArr.getJSONObject(0).getString("locate"));
                        if(flag == 0)
                            sKakao.setVisibility(View.INVISIBLE);
                        else {
                            sKakao.setVisibility(View.VISIBLE);
                            System.out.println(flag);
                            sKakao.setText(jsonArr.getJSONObject(0).getString("kakao_id"));
                        }
                        sIntro.setText(jsonArr.getJSONObject(0).getString("introduce"));
                        sExp.setText(jsonArr.getJSONObject(0).getString("exp"));
                    }catch (JSONException e)
                    {};

                } else if (response.isSuccess()) {
                    Log.d("Response Body isNull", response.message());
                } else {
                    Log.d("Response Error Body", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure( Throwable t) {
                t.printStackTrace();
                Log.e("Errorglg''';kl", t.getMessage());
            }
        });
    }

}

