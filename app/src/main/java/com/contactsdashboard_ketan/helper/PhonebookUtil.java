package com.contactsdashboard_ketan.helper;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;

import com.contactsdashboard_ketan.model.CallLogsModel;
import com.contactsdashboard_ketan.model.PhonebookModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ketan on 3/19/17.
 */

public class PhonebookUtil {

    private static final String LOG_TAG = "PhonebookUtil" ;

    public static List<CallLogsModel> getPastCallLogs(Context context) {
        ArrayList<CallLogsModel> notificationFlowArrayList = new ArrayList<>();
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                return notificationFlowArrayList;
            }
            String[] columns = null;
            Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                    columns, CallLog.Calls.DURATION+"> ?", new String[]{"0"}, CallLog.Calls.DURATION + " DESC limit 200");
            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
            int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
            int uri = managedCursor.getColumnIndex(CallLog.Calls.CACHED_PHOTO_URI);

            while (managedCursor.moveToNext()) {
                String usrname = managedCursor.getString(name);
                String phNumber = managedCursor.getString(number);
                String callDate = managedCursor.getString(date);
                String picUri = managedCursor.getString(uri);
                Date callDayTime = new Date(Long.valueOf(callDate));
                String fDate = new SimpleDateFormat("dd-MM-yyy hh:mm:ss").format(callDayTime);
                String callDuration = managedCursor.getString(duration);
                BLog.e(LOG_TAG, "name - " + usrname);
                BLog.e(LOG_TAG, "phn - " + phNumber);
                BLog.e(LOG_TAG, "callDayTime - " + fDate);
                BLog.e(LOG_TAG, "callDuration - " + callDuration);
                BLog.e(LOG_TAG, "picUri - " + picUri);
                if (usrname == null || !usrname.isEmpty()) {
                    usrname = getContactName(context, phNumber);
                }

                CallLogsModel callLogsModel = new CallLogsModel(usrname,
                        picUri,
                        phNumber,
                        "",
                        fDate,
                        callDuration);

                notificationFlowArrayList.add(callLogsModel);
            }
            managedCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return notificationFlowArrayList;
    }

    public static String getContactName(Context context, String number) {
        String name = null;
        String[] projection = new String[]{
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup._ID};

        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        Cursor cursor = context.getContentResolver().query(contactUri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                BLog.e(LOG_TAG, "Started uploadcontactphoto: Contact Found @ " + number);
                BLog.e(LOG_TAG, "Started uploadcontactphoto: Contact name  = " + name);
            } else {
                BLog.e(LOG_TAG, "Contact Not Found @ " + number);
            }
            cursor.close();
        }
        return name;
    }

    public static String getDurationString(int seconds) {
        BLog.e(LOG_TAG, "Enter value - "+seconds);
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
    }

    private static String twoDigitString(int number) {
        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

}
