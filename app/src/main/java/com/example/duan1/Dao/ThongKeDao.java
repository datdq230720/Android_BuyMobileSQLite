package com.example.duan1.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.duan1.Database.MyDatabase;
import com.example.duan1.Models.ModelPieChart;
import com.example.duan1.Models.Top;
import com.example.duan1.Models.TopNv;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ThongKeDao {

    private SQLiteDatabase sql;
    private MyDatabase db;
    private Context context;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public ThongKeDao(Context context){
        this.context = context;
        db = new MyDatabase(context);
        sql = db.getWritableDatabase();
    }

    // Top danh sach diện thoại bán chạy nhất
    public List<Top> getTop(){
        List<Top> list = new ArrayList<>();
        String q = "select MaDT, sum(SoLuong) " +
                "as soLuong from HoaDonChiTiet group by MaDT " +
                "order by soLuong desc limit 10";
        SQLiteDatabase sdb = db.getReadableDatabase();
        Cursor cursor = sdb.rawQuery(q, null);
        cursor.moveToFirst();
        try {
            while (cursor.isAfterLast() == false){
                int a = cursor.getColumnIndex("MaDT");
                int b = cursor.getColumnIndex("soLuong");
                Log.d("TAG", "getTop: "+a+b);
                Top t = new Top();
                t.setMadt(cursor.getInt(a));
                t.setSoLuong(Integer.parseInt(cursor.getString(b)));
                Log.d("TAG", "getTop: "+cursor.getString(b));
                list.add(t);
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

    public List<TopNv> getTopNV(){
        List<TopNv> list = new ArrayList<>();
        String q = "select MaNV, sum(TongTienHD) " +
                "as tongTien from HoaDon group by MaNV " +
                "order by tongTien desc limit 10";
        SQLiteDatabase sdb = db.getReadableDatabase();
        Cursor cursor = sdb.rawQuery(q, null);
        cursor.moveToFirst();
        try {
            while (cursor.isAfterLast() == false){
                int a = cursor.getColumnIndex("MaNV");
                int b = cursor.getColumnIndex("tongTien");
                Log.d("TAG", "getTopNV: "+a+b);
                TopNv topNv = new TopNv();
                topNv.setManv(cursor.getString(a));
                topNv.setTien(cursor.getDouble(b));
                Log.d("TAG", "getTopTien: "+cursor.getString(b));
                list.add(topNv);
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

    // Thống kê doanh thu
    public int getDoanhThu(String to, String from){
        String sqlDT = "select SUM(TongTienHD) " +
                "as doanhThu from HoaDon WHERE NgayMua between ? and ?";
        List<Integer> list = new ArrayList<Integer>();
        SQLiteDatabase sdb = db.getReadableDatabase();
        Cursor cursor = sdb.rawQuery(sqlDT, new String[]{to, from});
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

    public List<ModelPieChart> getChart(){
        List<ModelPieChart> list = new ArrayList<>();
        String sqlDT = "select MaDT, SUM(TongTien) " +
                "as doanhThu from HoaDonChiTiet  group by MaDT " +
                "order by doanhThu";
        Cursor cursor = sql.rawQuery(sqlDT, null);
        cursor.moveToFirst();
        try {
            while (cursor.isAfterLast() == false){
                ModelPieChart pie = new ModelPieChart();
                int a = cursor.getColumnIndex("doanhThu");
                Log.d("Doanh thu", "getChart: ");
                pie.PieTien = (int) cursor.getDouble(a);
                Log.d("Doanh thu", "getChart: "+cursor.getDouble(a));
                int b = cursor.getColumnIndex("MaDT");
                pie.PieMaDT = cursor.getInt(b);
                Log.d("MaDT", "getChart: "+cursor.getInt(b));
                list.add(pie);
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
    public List<ModelPieChart> getChart2(String to, String from){
        List<ModelPieChart> list = new ArrayList<>();
        String sqlDT = "select MaHoaDon, MaDT, SUM(TongTien) " +
                "as doanhThu from HoaDonChiTiet join HoaDon on HoaDonChiTiet.MaHD = HoaDon.MaHoaDon" +
                " where NgayMua " +
                "between ? and ? group by MaDT " +
                "order by doanhThu";
        Cursor cursor = sql.rawQuery(sqlDT, new String[]{to, from});
        cursor.moveToFirst();
        try {
            while (cursor.isAfterLast() == false){
                ModelPieChart pie = new ModelPieChart();
                int a = cursor.getColumnIndex("doanhThu");
                pie.PieTien = (int) cursor.getDouble(a);
                Log.d("Doanh thu2", "getChart: "+cursor.getDouble(a));
                int b = cursor.getColumnIndex("MaDT");
                pie.PieMaDT = cursor.getInt(b);
                Log.d("MaDT2", "getChart: "+cursor.getInt(b));
                list.add(pie);
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
}
