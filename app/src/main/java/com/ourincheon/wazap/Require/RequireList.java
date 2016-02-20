package com.ourincheon.wazap.Require;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ourincheon.wazap.R;

/**
 * Created by Youngdo on 2016-02-02.
 */
public class RequireList extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.require_activity);
        ListAdapter listAdapter = new com.ourincheon.wazap.Require.ListAdapter(this, R.layout.require_ltem);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(listAdapter);
    }
}
