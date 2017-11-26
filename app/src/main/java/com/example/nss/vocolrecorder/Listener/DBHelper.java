package com.example.nss.vocolrecorder.Listener;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by NSS on 9/3/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "s";
    private static final int DATABASE_VERSION = 1;

    private String tag = "DBHelper";

    private static OnDataBaseChangeListner mOnDataBaseChangeListner;

    static private abstract class DBvoice implements BaseColumns{

        static public final String table_name = "vocal_sound";

        static public final String fileName ="fileName";
        static public final String length = "length";
        static public final String filePath ="filePath";
        static public final String mtime="mTime";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER =" INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE =
            "create table "+DBvoice.table_name +" ("
                        +DBvoice.filePath + TEXT_TYPE + " primary key" +COMMA_SEP
                        +DBvoice.fileName + TEXT_TYPE +COMMA_SEP
                        +DBvoice.length +INTEGER +COMMA_SEP
                        +DBvoice.mtime + INTEGER+ ");";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void SetOnDataBaseChangeLisener(OnDataBaseChangeListner onDataBaseChangeListner ){

        mOnDataBaseChangeListner =onDataBaseChangeListner;

    }


    public void AddVoice(String filePath,String filaName,long length,long mTime){

        Log.d(tag,"start add Voice");

        SQLiteDatabase db =getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBvoice.filePath, filePath);
        contentValues.put(DBvoice.fileName,filaName);
        contentValues.put(DBvoice.length, length);
        contentValues.put(DBvoice.mtime, mTime);

        db.insert(DBvoice.table_name,null,contentValues);

        if(mOnDataBaseChangeListner != null){

            mOnDataBaseChangeListner.onDBAddVoice();

        }


        Log.d(tag,"end add Voice");

    }

    public VoiceItem GetVoice(int position){

        SQLiteDatabase db =getReadableDatabase();

        String[] queryColumn ={
                DBvoice.fileName,
                DBvoice.filePath,
                DBvoice.length,
                DBvoice.mtime
        };

        Cursor c =db.query(DBvoice.table_name,queryColumn,null,null,null,null,null);

        if(c.moveToPosition(position)){

            Log.d(tag,"fileName: " +c.getString(c.getColumnIndex(DBvoice.fileName)));
            Log.d(tag,"FilePath: " +c.getString( c.getColumnIndex(DBvoice.filePath)));
            Log.d(tag,"length: " +c.getLong(c.getColumnIndex(DBvoice.length)));
            Log.d(tag,"mtime: " +c.getLong( c.getColumnIndex(DBvoice.mtime)));

            VoiceItem item = new VoiceItem();

            item.setFileName(c.getString(c.getColumnIndex(DBvoice.fileName)));
            item.setFilePath(c.getString( c.getColumnIndex(DBvoice.filePath)));
            item.setLength(c.getInt(c.getColumnIndex(DBvoice.length)));
            item.setmTime(c.getLong( c.getColumnIndex(DBvoice.mtime)));


            c.close();

            return item;
        }

        return null;
    }

    public void DeleteVoice(String filePath){

        SQLiteDatabase db =getWritableDatabase();

        db.delete(DBvoice.table_name,null,null);


    }

    public int getCount(){
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = { DBvoice.filePath};
        Cursor c = db.query(DBvoice.table_name, projection, null, null, null, null, null);
        int count = c.getCount();
        c.close();

        Log.d(tag,String.valueOf(count));

        return count;
    }





}
