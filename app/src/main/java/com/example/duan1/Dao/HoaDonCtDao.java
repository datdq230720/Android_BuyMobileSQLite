package com.example.duan1.Dao;

import static com.example.duan1.Helper.Contents.COLUMN_DT_GIA;
import static com.example.duan1.Helper.Contents.COLUMN_HDCT_DT_MA;
import static com.example.duan1.Helper.Contents.COLUMN_HDCT_HD_MA;
import static com.example.duan1.Helper.Contents.COLUMN_HDCT_MA;
import static com.example.duan1.Helper.Contents.COLUMN_HDCT_SL;
import static com.example.duan1.Helper.Contents.COLUMN_HDCT_TIEN;
import static com.example.duan1.Helper.Contents.COLUMN_HD_DATE;
import static com.example.duan1.Helper.Contents.COLUMN_HD_KH;
import static com.example.duan1.Helper.Contents.COLUMN_HD_MA;
import static com.example.duan1.Helper.Contents.COLUMN_HD_NV_MA;
import static com.example.duan1.Helper.Contents.COLUMN_HD_TIEN;
import static com.example.duan1.Helper.Contents.COLUMN_H_MA;
import static com.example.duan1.Helper.Contents.TABLE_HANG;
import static com.example.duan1.Helper.Contents.TABLE_HD;
import static com.example.duan1.Helper.Contents.TABLE_HDCT;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.duan1.Database.MyDatabase;
import com.example.duan1.Models.HangDt;
import com.example.duan1.Models.HoaDon;
import com.example.duan1.Models.HoaDonChiTiet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HoaDonCtDao {
    private SQLiteDatabase sql;
    private MyDatabase db;
    int _maCT, _maHD, _maDT, _soLuong, _tien;

    public HoaDonCtDao(Context context){
        db = new MyDatabase(context);
        sql = db.getWritableDatabase();
    }

    public long insert(HoaDonChiTiet ct){
        ContentValues values = new ContentValues();

        values.put(COLUMN_HDCT_HD_MA, ct.getMaHD());
        values.put(COLUMN_HDCT_DT_MA, ct.getMaDT());
        values.put(COLUMN_HDCT_SL, ct.getSoLuong());
        values.put(COLUMN_HDCT_TIEN, ct.getThanhTien());

        return sql.insert(TABLE_HDCT, null, values);
    }

    public int delete(String id){
        return sql.delete(TABLE_HDCT,
                COLUMN_HDCT_MA  + " = ? ",
                new String[]{String.valueOf(id)});
    }

    public int update(HoaDonChiTiet ct){
        ContentValues values = new ContentValues();

        values.put(COLUMN_HDCT_MA, ct.getMaCT());
        values.put(COLUMN_HDCT_HD_MA, ct.getMaHD());
        values.put(COLUMN_HDCT_DT_MA, ct.getMaDT());
        values.put(COLUMN_HDCT_SL, ct.getSoLuong());
        values.put(COLUMN_HDCT_TIEN, ct.getThanhTien());

        return sql.update(TABLE_HDCT, values,
                COLUMN_HDCT_MA + " = ? ",
                new String[]{String.valueOf(ct.getMaCT())});
    }
    public int updateSL(int MaHdct, int sl, Double gia, int MaHD){
        ContentValues values = new ContentValues();

        values.put(COLUMN_HDCT_MA, MaHdct);
        values.put(COLUMN_HDCT_SL, sl);
        values.put(COLUMN_HDCT_TIEN, gia);

        return sql.update(TABLE_HDCT, values,
                COLUMN_HDCT_MA + " = ? " + " and " + COLUMN_HDCT_HD_MA + " = ? ",
                new String[]{String.valueOf(MaHdct), String.valueOf(MaHD)});
    }

    public List<HoaDonChiTiet> getAllHD(String maHD){
        String sql = "select * from " + TABLE_HDCT + " where " + COLUMN_HDCT_HD_MA + " = ? ";
        return getData(sql, maHD);
    }

    public HoaDonChiTiet getID(String id){
        String sql = "select * from " + TABLE_HDCT + " where " + COLUMN_HDCT_MA + " = ? ";
        List<HoaDonChiTiet> list = getData(sql, id);
        return list.get(0);
    }
    public Double getIdTien(String id){
        String sql = "select * from " + TABLE_HDCT + " where " + COLUMN_HDCT_MA + " = ? ";
        List<HoaDonChiTiet> list = getData(sql, id);
        return list.get(0).getThanhTien();
    }
    public int checkHdctDt(String _id) {
        int a = -1;
        sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery("select * from " + TABLE_HDCT + " where " + COLUMN_HDCT_DT_MA + " = ? ", new String[]{String.valueOf(_id)});
        try {
            while (cursor.moveToNext()) {
                a = cursor.getInt(_maDT);
            }
        } catch (Exception e) {
            Log.e("Get Category error: ", e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        cursor.close();
        return a;
    }
    public HoaDonChiTiet getIdAll(String _id){
        HoaDonChiTiet hdct = new HoaDonChiTiet();
        sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery("select * from " + TABLE_HDCT + " where " + COLUMN_HDCT_MA + " = ? ", new String[]{String.valueOf(_id)});
        try {
            while (cursor.moveToNext()) {
                hdct.setMaCT(cursor.getInt(_maCT));
                hdct.setMaHD(cursor.getInt(_maDT));
                hdct.setMaDT(cursor.getInt(_maDT));
                hdct.setThanhTien(cursor.getDouble(_tien));
                hdct.setSoLuong(cursor.getInt(_soLuong));
            }
        } catch (Exception e) {
            Log.e("Get Category error: ", e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        cursor.close();
        return hdct;
    }

    public int getByIdDt(String _id, String _idHD) {
        int MaDTHDCT = -1;
        sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery("select " + COLUMN_HDCT_MA +
                " from " + TABLE_HDCT + " where " + COLUMN_HDCT_DT_MA + " = ? " + " and " +
                COLUMN_HDCT_HD_MA + " = ? ", new String[]{String.valueOf(_id), String.valueOf(_idHD)});
        try {
            while (cursor.moveToNext()) {
                MaDTHDCT = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e("Get Category error: ", e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        cursor.close();
        return MaDTHDCT;
    }

    public List<HoaDonChiTiet> getData(String _sql, String...selectionArgs){

        List<HoaDonChiTiet> list = new ArrayList<>();
        Cursor cursor = sql.rawQuery(_sql, selectionArgs);
        cursor.moveToFirst();
        try {
            while (cursor.isAfterLast() == false){
                column(cursor);
                int MaCT = cursor.getInt(_maCT);
                int MaHD = cursor.getInt(_maHD);
                int MaDT = cursor.getInt(_maDT);
                int SoLuong = cursor.getInt(_soLuong);
                Double tienTong = cursor.getDouble(_tien);

                HoaDonChiTiet hdct = new HoaDonChiTiet(MaCT, MaHD, MaDT, SoLuong, tienTong);
                list.add(hdct);
                cursor.moveToNext();
            }

        }catch (Exception e){
            Log.e("Get Category error: ", e.getMessage());
        }finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return list;
    }

    public int getTongTien(int id){
        String sqlDT = "select SUM(TongTien) " +
                "as doanhThu from HoaDonChiTiet WHERE MaHD = ?";
        List<Integer> list = new ArrayList<Integer>();
        SQLiteDatabase sdb = db.getReadableDatabase();
        Cursor cursor = sdb.rawQuery(sqlDT, new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        try {
            while (cursor.isAfterLast() == false){
                int b = cursor.getColumnIndex("doanhThu");
                int doanhThu = Integer.parseInt(cursor.getString(b));
                list.add(doanhThu);
                cursor.moveToNext();
            }
        }catch (Exception e){
            Log.e("Get Category error: ", e.getMessage());
            list.add(0);
        }finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return list.get(0);
    }



    public int deleteHD(String id){
        return sql.delete(TABLE_HDCT,
                COLUMN_HDCT_HD_MA  + " = ? ",
                new String[]{String.valueOf(id)});
    }

    public void column(Cursor cursor){
        _maCT = cursor.getColumnIndex(COLUMN_HDCT_MA);
        _maHD = cursor.getColumnIndex(COLUMN_HDCT_HD_MA);
        _maDT = cursor.getColumnIndex(COLUMN_HDCT_DT_MA);
        _soLuong = cursor.getColumnIndex(COLUMN_HDCT_SL);
        _tien = cursor.getColumnIndex(COLUMN_HDCT_TIEN);
    }
}
