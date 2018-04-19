package smartdbsavs.com.smartdb_demo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import smartdbsavs.com.smartdb_demo.constant.DbConstant;
import smartdbsavs.com.smartdb_demo.holders.AddMemberDTO;
import smartdbsavs.com.smartdb_demo.holders.LogsDTO;

/**
 * Created by hi on 08-03-2018.
 */

public class DbController  extends SQLiteOpenHelper implements DbConstant {

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "userName";
    private static final String KEY_EMAIL = "emailId";

    private static final String KEY_ID1 = "id1";
    private static final String KEY_TITLE = "userTitle";
    private static final String KEY_LABEL = "userLabel";
    private static final String KEY_DATE_TIME = "userDateTime";
    private static final String KEY_URL = "userUrl";
    private static final String KEY_STATUS = "userStatus";
    private static final String KEY_BTNSTATUS = "userBtnStatus";


    private static DbController dbController = null;
    private Context mContext;

    public DbController(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    public static DbController getInstance(Context context) {
        if (dbController == null) {
            dbController = new DbController(context);
        }
        return dbController;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ADD_MEMBER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MEMBERS_ADDED + "("
                + KEY_ID + " TEXT," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT" + ")";

        String CREATE_ADD_LOG_ENTRY = "CREATE TABLE IF NOT EXISTS " + TABLE_LOGS_ADDED + "("
                + KEY_ID1 + " TEXT," + KEY_TITLE + " TEXT,"+ KEY_LABEL + " TEXT,"
                + KEY_DATE_TIME + " TEXT," + KEY_URL + " TEXT," +  KEY_STATUS + " TEXT," + KEY_BTNSTATUS + " TEXT" + ")";

        db.execSQL(CREATE_ADD_MEMBER_TABLE);
        db.execSQL(CREATE_ADD_LOG_ENTRY);
        Log.d(TAG, "onCreate(): Add_Member table created");
        Log.d(TAG, "onCreate(): Add_Log_table created");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERS_ADDED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGS_ADDED);
        onCreate(db);
    }

    public void addMember(AddMemberDTO admDTO) {
        Log.d(TAG, "insertInAddMemberTable()");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, admDTO.getMemid_AM());
        values.put(KEY_NAME, admDTO.getUserName_AM());
        values.put(KEY_EMAIL, admDTO.getEmailId_AM());

        // Inserting Row
        db.insert(TABLE_MEMBERS_ADDED, null, values);
        db.close(); // Closing database connection
    }

    public void addLogs(LogsDTO logsDTO) {
        Log.d(TAG, "insertInAddLogTable()");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID1, logsDTO.getId());
        values.put(KEY_TITLE, logsDTO.getTitleMessage());
        values.put(KEY_LABEL, logsDTO.getLabel());
        values.put(KEY_DATE_TIME, logsDTO.getDateTime());
        values.put(KEY_URL, logsDTO.getImageUrl());
        values.put(KEY_STATUS, logsDTO.getStatus());
        values.put(KEY_BTNSTATUS, logsDTO.getBtn_status());

        // Inserting Row
        db.insert(TABLE_LOGS_ADDED, null, values);
        db.close(); // Closing database connection
    }

    public AddMemberDTO getMember(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_MEMBERS_ADDED,
                new String[] { KEY_ID, KEY_NAME, KEY_EMAIL },
                KEY_ID + "=?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null);

        if (cursor != null)
            cursor.moveToFirst();

        AddMemberDTO addMemberDTO = new AddMemberDTO((cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));

        return addMemberDTO;
    }

    public LogsDTO getLog(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_LOGS_ADDED,
                new String[] { KEY_ID1, KEY_TITLE, KEY_LABEL, KEY_DATE_TIME, KEY_URL, KEY_STATUS, KEY_BTNSTATUS},
                KEY_ID1 + "=?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null);

        if (cursor != null)
            cursor.moveToFirst();

        LogsDTO logsDTO = new LogsDTO();

        return logsDTO;
    }

    public List<AddMemberDTO> getAllMembers() {
        List<AddMemberDTO> memberList = new ArrayList<AddMemberDTO>();

        String selectQuery = "SELECT  * FROM " + TABLE_MEMBERS_ADDED;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AddMemberDTO addMemberDTO = new AddMemberDTO();
                addMemberDTO.setMemid_AM((cursor.getString(0)));
                addMemberDTO.setUserName_AM(cursor.getString(1));
                addMemberDTO.setEmailId_AM(cursor.getString(2));
                // Adding contact to list
                memberList.add(addMemberDTO);
            } while (cursor.moveToNext());
        }

        // return contact list
        return memberList;
    }

    public List<LogsDTO> getAllLogs() {
        List<LogsDTO> logList = new ArrayList<LogsDTO>();

        String selectQuery = "SELECT  * FROM " + TABLE_LOGS_ADDED;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LogsDTO logsDTO = new LogsDTO();
                logsDTO.setId(cursor.getString(0));
                logsDTO.setTitleMessage(cursor.getString(1));
                logsDTO.setLabel(cursor.getString(2));
                logsDTO.setDateTime(cursor.getString(3));
                logsDTO.setImageUrl(cursor.getString(4));
                logsDTO.setStatus((cursor.getString(5)));
                logsDTO.setBtn_status((cursor.getString(6)));

                // Adding contact to lis
                logList.add(logsDTO);
            } while (cursor.moveToNext());
        }

        // return contact list
        return logList;
    }

    public int getMemberCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MEMBERS_ADDED;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    public int getLogsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGS_ADDED;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    public void deleteMember(AddMemberDTO addMemberDTO) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEMBERS_ADDED, KEY_ID + " = ?",
                new String[] { String.valueOf(addMemberDTO.getMemid_AM()) });
        db.close();
    }

    public void deleteLog(LogsDTO logsDTO) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOGS_ADDED, KEY_ID1 + " = ?",
                new String[] { String.valueOf(logsDTO.getId()) });
        db.close();
    }

    public int updateMember(AddMemberDTO addMemberDTO) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, addMemberDTO.getMemid_AM());
        values.put(KEY_NAME, addMemberDTO.getUserName_AM());
        values.put(KEY_EMAIL, addMemberDTO.getEmailId_AM());

        // updating row
        return db.update(TABLE_MEMBERS_ADDED, values, KEY_ID + " = ?",
                new String[] { String.valueOf(addMemberDTO.getMemid_AM()) });
    }

    public int updateLogs(LogsDTO logsDTO) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID1, logsDTO.getId());
        values.put(KEY_TITLE, logsDTO.getTitleMessage());
        values.put(KEY_LABEL, logsDTO.getLabel());
        values.put(KEY_DATE_TIME, logsDTO.getDateTime());
        values.put(KEY_URL, logsDTO.getImageUrl());
        values.put(KEY_STATUS, logsDTO.getStatus());
        values.put(KEY_BTNSTATUS, logsDTO.getBtn_status());


        // updating row
        return db.update(TABLE_LOGS_ADDED, values, KEY_ID1 + " = ?",
                new String[] { String.valueOf(logsDTO.getId()) });
    }

}
