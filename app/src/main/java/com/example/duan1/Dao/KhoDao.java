package com.example.duan1.Dao;

import static com.example.duan1.Helper.Contents.*;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.duan1.Database.MyDatabase;
import com.example.duan1.Models.HangDt;

public class KhoDao {

    private SQLiteDatabase sql;
    private MyDatabase db;

    public KhoDao(Context context){
        db = new MyDatabase(context);
        sql = db.getWritableDatabase();
    }

    public int getSLConLai(int ID){
        int soL = 0;
        sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(" select " + COLUMN_DT_SL +" from " + TABLE_DT + " WHERE " + COLUMN_DT_MA + " = ?", new String[]{String.valueOf(ID)});
        while (cursor.moveToNext()) {
            int a = cursor.getColumnIndex(COLUMN_DT_SL);
            soL = cursor.getInt(a);
        }
        cursor.close();
        return soL;
    }
    public int updateSL(int sl, int maDT){
        ContentValues values = new ContentValues();
        values.put(COLUMN_DT_SL, sl);
        return sql.update(TABLE_DT, values,
                COLUMN_DT_MA + " = ? ",
                new String[]{String.valueOf(maDT)});
    }

}
