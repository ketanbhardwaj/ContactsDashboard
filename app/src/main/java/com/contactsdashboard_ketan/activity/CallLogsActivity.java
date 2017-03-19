package com.contactsdashboard_ketan.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.contactsdashboard_ketan.R;
import com.contactsdashboard_ketan.adapter.CallLogsAdapter;
import com.contactsdashboard_ketan.adapter.PhonebookAdapter;
import com.contactsdashboard_ketan.helper.PhonebookUtil;
import com.contactsdashboard_ketan.helper.ViewUtil;
import com.contactsdashboard_ketan.model.CallLogsModel;

import java.util.ArrayList;
import java.util.List;

public class CallLogsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<CallLogsModel> callLogsModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_logs);
        initViews();
        new FetchCallLogs().execute();
    }

    private void initViews(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_logs);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Call Logs");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_logs);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private class FetchCallLogs extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ViewUtil.showLoadingProgressDialog(CallLogsActivity.this);
            //this method will be running on UI thread
        }
        @Override
        protected Void doInBackground(Void... params) {
            callLogsModelList = PhonebookUtil.getPastCallLogs(CallLogsActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(callLogsModelList.size() > 0){
                CallLogsAdapter phonebookAdapter = new CallLogsAdapter(CallLogsActivity.this, callLogsModelList);
                recyclerView.setAdapter(phonebookAdapter);
            }else{
            }
            ViewUtil.hideLoadingProgressDialog();
        }

    }

}
