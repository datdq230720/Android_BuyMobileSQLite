package com.example.duan1.Dao;

import static com.example.duan1.Helper.Contents.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.duan1.Database.MyDatabase;
import com.example.duan1.Models.DienThoai;
import com.example.duan1.Models.HangDt;
import com.example.duan1.Models.HoaDon;
import com.example.duan1.Models.NhanVien;

import java.util.ArrayList;
import java.util.List;

public class HangDao {

    private SQLiteDatabase sql;
    private MyDatabase db;

    public HangDao(Context context){
        db = new MyDatabase(context);
        sql = db.getWritableDatabase();
    }

    public long insert (HangDt h){
        ContentValues values = new ContentValues();
        values.put(COLUMN_H_TEN, h.getTenH());
        values.put(COLUMN_H_HA, h.getImg());
        return sql.insert(TABLE_HANG, null, values);
    }

    public int update(HangDt h ){
        ContentValues values = new ContentValues();
        values.put(COLUMN_H_TEN, h.getTenH());
        values.put(COLUMN_H_HA, h.getImg());
        return sql.update(TABLE_HANG, values,
                COLUMN_H_MA + " = ? ",
                new String[]{String.valueOf(h.getMaH())});
    }

    public int delete(String id){
        return sql.delete(TABLE_HANG,
                COLUMN_H_MA  + " = ? ",
                new String[]{String.valueOf(id)});
    }

    public List<HangDt> getAll(){
        String sql = "select * from " + TABLE_HANG;
        return getData(sql);
    }

    public HangDt getID(String id) {
        String sql = "select * from " + TABLE_HANG + " where " + COLUMN_H_MA + " = ? ";
        List<HangDt> list = getData(sql, id);
        return list.get(0);
    }

    public List<HangDt> getData(String _sql, String...selectionArgs) {
        List<HangDt> list = new ArrayList<>();
        Cursor cursor = sql.rawQuery(_sql, selectionArgs);
        cursor.moveToFirst();
        try {
            while (cursor.isAfterLast() == false) {
                int id = cursor.getInt(0);
                String ten = cursor.getString(1);
                byte[] hinhAnh = cursor.getBlob(2);
                HangDt h = new HangDt(id, ten, hinhAnh);
                list.add(h);
                cursor.moveToNext();
            }
        } catch (Exception e) {
            Log.e("Get Category error: ", e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return list;
    }

    public HangDt getByID(String _id) {
        HangDt h = new HangDt();
        sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery("select * from " + TABLE_HANG + " where " + COLUMN_H_MA + " = ? ", new String[]{String.valueOf(_id)});
        try {
            while (cursor.moveToNext()) {
                h.setMaH(cursor.getInt(0));
                h.setTenH(cursor.getString(1));
            }
        } catch (Exception e) {
            Log.e("Get Category error: ", e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        cursor.close();
        return h;
    }

    public String checkTen(String tenH){
        String tenHdt = "";
        sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(" select " + COLUMN_H_TEN +" from " + TABLE_HANG + " WHERE " + COLUMN_H_TEN + " = ?", new String[]{String.valueOf(tenH)});
        while (cursor.moveToNext()) {
            int a = cursor.getColumnIndex(COLUMN_H_TEN);
            tenHdt = cursor.getString(a);
        }
        cursor.close();
        return tenHdt;

    }
}
