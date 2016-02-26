package com.ourincheon.wazap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ApplierList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applier_list);

        Intent intent = getIntent();
        System.out.println(intent.getExtras().getInt("id"));
    }
}
