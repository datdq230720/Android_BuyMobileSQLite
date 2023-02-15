package com.example.duan1.Dao;

import static com.example.duan1.Helper.Contents.*;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.duan1.Database.MyDatabase;
import com.example.duan1.Models.DienThoai;
import com.example.duan1.Models.HangDt;
import com.example.duan1.Models.NhanVien;

import java.util.ArrayList;
import java.util.List;

public class DienThoaiDao {

    private int _maDT, _maH, _tenDT, _gia, _soLuong, _img;

    private SQLiteDatabase sql;
    private MyDatabase db;

    public DienThoaiDao(Context context){
        db = new MyDatabase(context);
        sql = db.getWritableDatabase();
    }

    public long insert (DienThoai dt){
        ContentValues values = new ContentValues();
        values.put(COLUMN_DT_HANG_MA, dt.getMaHdt());
        values.put(COLUMN_DT_TEN, dt.getTenDT());
        values.put(COLUMN_DT_GIA, dt.getGia());
        values.put(COLUMN_DT_SL, dt.getSoLuong());
        values.put(COLUMN_DT_HA, dt.getImg());
        return sql.insert(TABLE_DT, null, values);
    }

    public int update(DienThoai dt){
        ContentValues values = new ContentValues();
        values.put(COLUMN_DT_MA, dt.getMaDT());
        values.put(COLUMN_DT_HANG_MA, dt.getMaHdt());
        values.put(COLUMN_DT_TEN, dt.getTenDT());
        values.put(COLUMN_DT_GIA, dt.getGia());
        values.put(COLUMN_DT_SL, dt.getSoLuong());
        values.put(COLUMN_DT_HA, dt.getImg());
        return sql.update(TABLE_DT, values,
                COLUMN_DT_MA + " = ? ",
                new String[]{String.valueOf(dt.getMaDT())});
    }

    public int delete(String id){
        return sql.delete(TABLE_DT,
                COLUMN_DT_MA  + " = ? ",
                new String[]{String.valueOf(id)});
    }

    public List<DienThoai> getAll(){
        String sql = "select * from " + TABLE_DT;
        return getData(sql);
    }

    public DienThoai getID(String id) {
        String sql = "select * from " + TABLE_DT + " where " + COLUMN_DT_MA + " = ? ";
        List<DienThoai> list = getData(sql, id);
        return list.get(0);
    }

    public List<DienThoai> getData(String _sql, String...selectionArgs) {
        List<DienThoai> list = new ArrayList<>();
        Cursor cursor = sql.rawQuery(_sql, selectionArgs);
        cursor.moveToFirst();
        try {
            while (cursor.isAfterLast() == false) {
                column(cursor);
                int id = cursor.getInt(_maDT);
                int idH = cursor.getInt(_maH);
                String ten = cursor.getString(_tenDT);
                int soLuong = cursor.getInt(_soLuong);
                Double gia = cursor.getDouble(_gia);
                byte[] img = cursor.getBlob(_img);
                DienThoai dt = new DienThoai(id, ten, idH, soLuong, gia, img);
                list.add(dt);
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
    public DienThoai getByID(String _id) {
        DienThoai dienThoai = null;
        sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery("select * from " + TABLE_DT + " WHERE " + COLUMN_DT_HANG_MA + " = ?", new String[]{String.valueOf(_id)});
        while (cursor.moveToNext()) {
            column(cursor);
            dienThoai =  new DienThoai();
            dienThoai.setMaDT(cursor.getInt(_maDT));
            dienThoai.setMaHdt(cursor.getInt(_maDT));
            dienThoai.setTenDT(cursor.getString(_tenDT));
            Log.d("DTDAO", "getByID: "+cursor.getString(_tenDT));
            dienThoai.setSoLuong(cursor.getInt(_soLuong));
            dienThoai.setGia(cursor.getDouble(_gia));
            dienThoai.setImg(cursor.getBlob(_img));
//            int id = cursor.getInt(_maDT);
//            int idH = cursor.getInt(_maH);
//            String ten = cursor.getString(_tenDT);
//            int soLuong = cursor.getInt(_soLuong);
//            Double gia = cursor.getDouble(_gia);
//            byte[] img = cursor.getBlob(_img);
//            dienThoai = new DienThoai(id, ten, idH, soLuong, gia, img);

        }
        cursor.close();
        return dienThoai;
    }

    public String checkTen(String tenDT){
        String tenDt = "";
        sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(" select " + COLUMN_DT_TEN +" from " + TABLE_DT + " WHERE " + COLUMN_DT_TEN + " = ?", new String[]{String.valueOf(tenDT)});
        while (cursor.moveToNext()) {
            column(cursor);
            tenDt = cursor.getString(_tenDT);
        }
        cursor.close();
        return tenDt;

    }

    public void column(Cursor cursor){

        _maDT = cursor.getColumnIndex(COLUMN_DT_MA);
        _maH = cursor.getColumnIndex(COLUMN_DT_HANG_MA);
        _tenDT = cursor.getColumnIndex(COLUMN_DT_TEN);
        _gia = cursor.getColumnIndex(COLUMN_DT_GIA);
        _soLuong = cursor.getColumnIndex(COLUMN_DT_SL);
        _img = cursor.getColumnIndex(COLUMN_DT_HA);
        Log.d("STT của Ảnh: ", ""+_img);

    }
}
