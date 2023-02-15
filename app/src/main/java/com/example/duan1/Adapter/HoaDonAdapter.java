package com.example.duan1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.example.duan1.Dao.NhanVienDao;
import com.example.duan1.Fragment.HoaDonFragment;
import com.example.duan1.Models.HoaDon;
import com.example.duan1.Models.NhanVien;
import com.example.duan1.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HoaDonAdapter extends ArrayAdapter<HoaDon> {

    private Context context;
    private ArrayList<HoaDon> list;
    HoaDonFragment fragment;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private TextView tvNV, tvKH, tvMa, tvGia, tvNgay;
    Button bt;

    public HoaDonAdapter(Context context, HoaDonFragment fragment, ArrayList<HoaDon> list){
        super(context, 0, list);
        this.context = context;
        this.fragment = fragment;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null){
            LayoutInflater inf=(LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.items_hoa_don, null);
        }

        tvNV = v.findViewById(R.id.items_hd_tv_nv);
        tvKH = v.findViewById(R.id.items_hd_tv_kh);
//        tvMa = v.findViewById(R.id.items_hd_tv_ma);
        tvGia = v.findViewById(R.id.items_hd_tv_gia);
        tvNgay = v.findViewById(R.id.items_hd_tv_ngay);
//        bt = v.findViewById(R.id.items_hd_bt);

        final HoaDon hd = list.get(position);
        if (hd != null){
            NhanVienDao nvD = new NhanVienDao(context);
            NhanVien nv = nvD.getID(hd.getMaNV());
            tvNV.setText(""+nv.getHoTen());
            tvKH.setText("KH: "+hd.getTenKH());
            tvNgay.setText(""+sdf.format(hd.getNgay()));
            tvGia.setText(String.format("%,.0f",hd.getTienTong())+"$");
//            tvMa.setText("Mã: "+hd.getMaHD());
        }

//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fragment.xoa_hd(String.valueOf(hd.getMaHD()));
//            }
//        });
        return v;
    }
}
