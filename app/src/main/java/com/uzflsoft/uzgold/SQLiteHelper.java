package com.uzflsoft.uzgold;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "local_courses";
    public static final String TABLE_NAME = "courses";
    public static final String column1 = "gold_tashkent_course";
    public static final String column2 = "dollar_tashkent_course";
    public static final String column3 = "gold_world_course";
    public static final String column4 = "dollar_world_course";


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" + column1 + " INTEGER, " + column2 + " INTEGER," +
                column3 + " INTEGER, " + column4 + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }

    public void updateData(/*String gt, String dt,*/ String gw, String dw) {
        SQLiteDatabase db  = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(column1, gw);
        cv.put(column2, dw);
        cv.put(column3, "322");
        cv.put(column4, "322");
        db.update(TABLE_NAME, cv, null, null);

    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return  res;
    }
/*
    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }
*/
    public boolean isTableEmpty(){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean hasTables = true;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if(cursor != null && cursor.getCount() > 0){
            hasTables=false;
            cursor.close();
        }

        return hasTables;
    }


    public void insertDataDefault() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(column1, "111");
        cv.put(column2, "111");
        cv.put(column3, "111");
        cv.put(column4, "111");
        db.insert(TABLE_NAME, null, cv);
    }

}
