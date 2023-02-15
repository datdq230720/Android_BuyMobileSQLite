package com.example.duan1.Dao;

import static com.example.duan1.Helper.Contents.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.duan1.Database.MyDatabase;
import com.example.duan1.Models.NhanVien;

import java.util.ArrayList;
import java.util.List;

public class NhanVienDao {

    private SQLiteDatabase SQL;
    private MyDatabase db;

    public NhanVienDao(Context context){
        db = new MyDatabase(context);
        SQL = db.getWritableDatabase();
    }

    public long insert(NhanVien nv){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NV_MA, nv.MaNV);
        values.put(COLUMN_NV_PASS, nv.Password);
        values.put(COLUMN_NV_HOTEN, nv.HoTen);
        values.put(COLUMN_NV_EMAIL, nv.Email);
        values.put(COLUMN_NV_HA, nv.HinhAnh);
        return SQL.insertOrThrow(TABLE_NV, null, values);

    }
    public int update(NhanVien nv ){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NV_PASS, nv.Password);
        values.put(COLUMN_NV_HOTEN, nv.HoTen);
        values.put(COLUMN_NV_EMAIL, nv.Email);
        values.put(COLUMN_NV_HA, nv.HinhAnh);
        return SQL.update(TABLE_NV, values,
                COLUMN_NV_MA + " = ? ",
                new String[]{String.valueOf(nv.MaNV)});
    }

    public int delete(String id){
        return SQL.delete(TABLE_NV,
                COLUMN_NV_MA  + " = ? ",
                new String[]{String.valueOf(id)});
    }

    public List<NhanVien> getAll(){
        String sql = " SELECT * FROM " + TABLE_NV;
        return getData(sql);
    }
    public NhanVien getID(String id){
        String sql = " SELECT * FROM " + TABLE_NV + " WHERE " + COLUMN_NV_MA + " = ? ";
        List<NhanVien> list = getData(sql, id);
        return list.get(0);
    }
    public NhanVien getByID(String _id) {
        NhanVien nhanVien = null;
        SQL = db.getReadableDatabase();
        Cursor c = SQL.rawQuery("select * from " + TABLE_NV + " WHERE " + COLUMN_NV_MA + " = ?", new String[]{String.valueOf(_id)});
        while (c.moveToNext()) {
            String id = c.getString(0);
            String pass = c.getString(1);
            String hoTen = c.getString(2);
            String Email = c.getString(3);
            byte[] hinhAnh = c.getBlob(4);
            nhanVien = new NhanVien(id, pass, hoTen, Email, hinhAnh);
        }
        c.close();
        return nhanVien;
    }
    public List<NhanVien> getData(String sql, String...selectionArgs) {
        List<NhanVien> list = new ArrayList<>();
        Cursor cursor = SQL.rawQuery(sql, selectionArgs);
        cursor.moveToFirst();
        try {
            while (cursor.isAfterLast() == false){
                String id = cursor.getString(0);
                String pass = cursor.getString(1);
                String hoTen = cursor.getString(2);
                String Email = cursor.getString(3);
                byte[] hinhAnh = cursor.getBlob(4);
                NhanVien nhanVien = new NhanVien(id, pass, hoTen, Email, hinhAnh);
                list.add(nhanVien);
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
    // check login
    public int checkLogin(String id, String pass){
        String sql =" select * from " + TABLE_NV + " WHERE " + COLUMN_NV_MA + " = ?" + " AND "+ COLUMN_NV_PASS + " = ?";
        List<NhanVien> list = getData(sql, id, pass);
        if (list.size() == 0){
            return -1;
        }else {
            return 1;
        }
    }

    public NhanVien fogotPass(String _id){
        NhanVien nhanVien = null;
        SQL = db.getReadableDatabase();
        Cursor c = SQL.rawQuery("select * from " + TABLE_NV + " WHERE " + COLUMN_NV_MA + " = ?", new String[]{String.valueOf(_id)});
        while (c.moveToNext()) {
            String id = c.getString(0);
            String pass = c.getString(1);
            String hoTen = c.getString(2);
            String Email = c.getString(3);
            byte[] hinhAnh = c.getBlob(4);
            nhanVien = new NhanVien(id, pass, hoTen, Email, hinhAnh);
        }
        c.close();
        return nhanVien;

    }

}
