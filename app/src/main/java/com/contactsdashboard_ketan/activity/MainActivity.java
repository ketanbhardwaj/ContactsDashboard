package com.contactsdashboard_ketan.activity;

import android.Manifest;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.contactsdashboard_ketan.R;
import com.contactsdashboard_ketan.adapter.PhonebookAdapter;
import com.contactsdashboard_ketan.helper.BLog;
import com.contactsdashboard_ketan.helper.PhonebookUtil;
import com.contactsdashboard_ketan.helper.ViewUtil;
import com.contactsdashboard_ketan.model.PhonebookModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private static final int READ_CONTACTS_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    private List<PhonebookModel> phonebookModelList = new ArrayList<>();
    private Button proceed_btn;
    private boolean checlCallLogsPerm = false;

    String[] permissionsRequired = new String[]{Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CALL_LOG};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);
        initViews();
        checkPermission("fc");
    }

    private void initViews(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Phonebook List");
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        proceed_btn = (Button)findViewById(R.id.proceed_btn);
        proceed_btn.setVisibility(View.GONE);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        proceed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checlCallLogsPerm = true;
                checkPermission("cl");
            }
        });
    }

    private void getAllContacts(){
        phonebookModelList.clear();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String lastContactTime = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LAST_TIME_CONTACTED));
            String picUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
            BLog.e(LOG_TAG, "name - "+name);
            BLog.e(LOG_TAG, "phoneNumber - "+phoneNumber);
            BLog.e(LOG_TAG, "contactId - "+contactId);
            BLog.e(LOG_TAG, "lastContactTime - "+lastContactTime);
            BLog.e(LOG_TAG, "picUri - "+picUri);
            phonebookModelList.add(new PhonebookModel(name, contactId, phoneNumber, contactId, picUri, lastContactTime));
        }
    }

    private void checkPermission(String target){
        if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[1])
                    ||  ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[2])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Read Multiple Permissions");
                builder.setMessage("This app needs contacts and storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, permissionsRequired, READ_CONTACTS_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if(!checlCallLogsPerm){
                            finish();
                        }
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.READ_CONTACTS,false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Read Multiple Permissions");
                builder.setMessage("This app needs contacts and storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Read Contacts", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if(!checlCallLogsPerm){
                            finish();
                        }
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(MainActivity.this, permissionsRequired, READ_CONTACTS_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0],true);
            editor.commit();


        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission(target);
        }
    }

    private void proceedAfterPermission(String target) {
        if(target.equals("cl")){
            Intent intent = new Intent(MainActivity.this, CallLogsActivity.class);
            startActivity(intent);
        }else if(target.equals("fc")){
            new FetchContactsAsync().execute();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_CONTACTS_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                BLog.e(LOG_TAG, "onRequestPermissionsResult");
                if(checlCallLogsPerm && grantResults[2] == PackageManager.PERMISSION_GRANTED){
                    proceedAfterPermission("cl");
                    checlCallLogsPerm = false;
                }else{
                    proceedAfterPermission("fc");
                }
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[1])
                        || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[2])) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Need Read Multiple Permissions");
                    builder.setMessage("This app needs contacts and storage permission.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(MainActivity.this, permissionsRequired, READ_CONTACTS_PERMISSION_CONSTANT);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if(!checlCallLogsPerm){
                                finish();
                            }
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                BLog.e(LOG_TAG, "onActivityResult");
                if(checlCallLogsPerm){
                    proceedAfterPermission("cl");
                    checlCallLogsPerm = false;
                }else{
                    proceedAfterPermission("fc");
                }
            }
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                BLog.e(LOG_TAG, "onPostResume");
                if(checlCallLogsPerm){
                    proceedAfterPermission("cl");
                    checlCallLogsPerm = false;
                }else{
                    proceedAfterPermission("fc");
                }
            }
        }
    }

    private class FetchContactsAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ViewUtil.showLoadingProgressDialog(MainActivity.this);
            //this method will be running on UI thread
        }
        @Override
        protected Void doInBackground(Void... params) {
            getAllContacts();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(phonebookModelList.size() > 0){
                proceed_btn.setVisibility(View.VISIBLE);
                PhonebookAdapter phonebookAdapter = new PhonebookAdapter(MainActivity.this, phonebookModelList);
                recyclerView.setAdapter(phonebookAdapter);
            }else{
                proceed_btn.setVisibility(View.GONE);
            }
            ViewUtil.hideLoadingProgressDialog();
        }

    }

}
