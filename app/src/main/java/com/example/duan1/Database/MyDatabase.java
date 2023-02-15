package com.example.duan1.Database;

import static com.example.duan1.Helper.Contents.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabase extends SQLiteOpenHelper {



    public MyDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Bảng nhân viên
        String sql = "create table if not exists " + TABLE_NV +
                "(" +
                COLUMN_NV_MA + " text primary key ," +
                COLUMN_NV_PASS + " text," +
                COLUMN_NV_HOTEN + " text," +
                COLUMN_NV_EMAIL + " text," +
                COLUMN_NV_HA + " bold " +
                " ) ";

        db.execSQL(sql);

        sql = " insert into " + TABLE_NV + " values ('admin', 'admin', 'Đinh Quốc Đạt', 'nhoctengu2001@gmail.com', null) ";
        db.execSQL(sql);

        // bảng hãng điện thoại
        String sql2 = " create table if not exists " + TABLE_HANG  +
                "(" +
                COLUMN_H_MA + " integer primary key autoincrement, " +
                COLUMN_H_TEN +" text," +
                COLUMN_H_HA + " blod " +
                ")";

        db.execSQL(sql2);

        // Bảng điện thoại

        String sql3 = "create table if not exists " + TABLE_DT +
                "(" +
                COLUMN_DT_MA + " integer primary key autoincrement," +
                COLUMN_DT_TEN + " text," +
                COLUMN_DT_GIA + " real," +
                COLUMN_DT_SL + " integer," +
                COLUMN_DT_HANG_MA + " integer," +
                COLUMN_DT_HA + " bold, " +
                " foreign key ( "+COLUMN_DT_HANG_MA+" ) " +
                " references "+TABLE_HANG+" ( "+COLUMN_H_MA+" ) " +
                " ) ";
        db.execSQL(sql3);

        // Bảng hóa đơn

        String sql4 = "create table if not exists " + TABLE_HD +
                "(" +
                COLUMN_HD_MA + " integer primary key autoincrement," +
                COLUMN_HD_NV_MA + " text," +
                COLUMN_HD_KH + " text," +
                COLUMN_HD_DATE + " date," +
                COLUMN_HD_TIEN + " real," +
                " foreign key ( "+COLUMN_HD_NV_MA+" ) " +
                " references "+TABLE_NV+" ( "+COLUMN_NV_MA+" ) " +
                " ) ";
        db.execSQL(sql4);

        // Bảng hóa đơn chi tiết

        String sql5 = "create table if not exists " + TABLE_HDCT +
                "(" +
                COLUMN_HDCT_MA + " integer primary key autoincrement," +
                COLUMN_HDCT_HD_MA + " integer," +
                COLUMN_HDCT_DT_MA + " integer," +
                COLUMN_HDCT_SL + " integer," +
                COLUMN_HDCT_TIEN + " real," +
                " foreign key ( "+COLUMN_HDCT_HD_MA+" ) " +
                " references "+TABLE_HD+" ( "+COLUMN_HD_MA+" ), " +
                " foreign key ( "+COLUMN_HDCT_DT_MA+" ) " +
                " references "+TABLE_DT+" ( "+COLUMN_DT_MA+" ) " +
                " ) ";
        db.execSQL(sql5);

        //
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int upgrade = oldVersion + 1;
        while (upgrade < newVersion){
            switch (upgrade){
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
                default:

                    break;
            }
            upgrade++;
        }
    }
}
