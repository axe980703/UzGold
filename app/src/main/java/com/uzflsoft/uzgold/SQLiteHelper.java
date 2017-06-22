package com.uzflsoft.uzgold;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "local_courses";
    public static final String TABLE_NAME = "courses";
    public static final String column[] = {"dollar_tashkent_course", "euro_tashkent_course","ruble_tashkent_course",
     "dollar_world_course","euro_world_course","ruble_world_course","gold_tashkent_course","gold_world_course"};


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" + column[0] + " INTEGER, " + column[1] + " INTEGER," +
                column[2] + " INTEGER, " + column[3] + " INTEGER," + column[4] + " INTEGER," + column[5] + " INTEGER," +
                column[6] + " INTEGER," + column[7] + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }

    public void updateData(String[] courses) {
        SQLiteDatabase db  = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for(int i = 0; i < courses.length; i++)
            cv.put(column[i], courses[i]);

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
        for(int i = 0; i < 8; i++)
            cv.put(column[i], 999);
        db.insert(TABLE_NAME, null, cv);
    }

}
