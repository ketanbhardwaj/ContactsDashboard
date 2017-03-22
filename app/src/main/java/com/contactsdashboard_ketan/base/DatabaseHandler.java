package com.contactsdashboard_ketan.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.contactsdashboard_ketan.helper.BLog;
import com.contactsdashboard_ketan.model.CallLogsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ketan on 3/20/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone";
    private static final String KEY_PIC = "pic";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_LAST_CONTACT_TIME = "last_contact_time";
    private static final String KEY_TOTAL_TALK_TIME = "total_talk_time";
    private static final String LOG_TAG = "DatabaseHandler";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT,"
                + KEY_PIC + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_LAST_CONTACT_TIME + " TEXT,"
                + KEY_TOTAL_TALK_TIME + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void addContact(CallLogsModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getMobile());
        values.put(KEY_PIC, contact.getPic());
        values.put(KEY_EMAIL, contact.getEmail());
        values.put(KEY_LAST_CONTACT_TIME, contact.getLastContactTime());
        values.put(KEY_TOTAL_TALK_TIME, contact.getTotalTalkTime());

        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    public CallLogsModel getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

//        CallLogsModel contact = new CallLogsModel(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1), cursor.getString(2));
        CallLogsModel contact = new CallLogsModel();
        // return contact
        return contact;
    }

    public List<CallLogsModel> getAllContacts() {
        List<CallLogsModel> contactList = new ArrayList<CallLogsModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CallLogsModel contact = new CallLogsModel();
//                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setMobile(cursor.getString(2));
                contact.setPic(cursor.getString(3));
                contact.setEmail(cursor.getString(4));
                contact.setLastContactTime(cursor.getString(5));
                contact.setTotalTalkTime(cursor.getString(6));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void deleteAllRecords(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_CONTACTS);
    }

    public ArrayList<CallLogsModel> getAggregatedData(){
        ArrayList<CallLogsModel> contactList = new ArrayList<CallLogsModel>();
        String selectQuery = "SELECT "+
                KEY_NAME + "," +
                KEY_PH_NO + "," +
                KEY_PIC + "," +
                KEY_EMAIL + "," +
                KEY_LAST_CONTACT_TIME + ","
                +"sum(total_talk_time) FROM " + TABLE_CONTACTS + " WHERE total_talk_time > 0 group by "+KEY_PH_NO+" order by sum(total_talk_time) desc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CallLogsModel contact = new CallLogsModel();
                BLog.e(LOG_TAG, "cursor TTK - "+cursor.getString(5));
                BLog.e(LOG_TAG, "cursor phone - "+cursor.getString(1));
                contact.setName(cursor.getString(0));
                contact.setMobile(cursor.getString(1));
                contact.setPic(cursor.getString(2));
                contact.setEmail(cursor.getString(3));
                contact.setLastContactTime(cursor.getString(4));
                contact.setTotalTalkTime(cursor.getString(5));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        return contactList;
    }

}
