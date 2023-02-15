package com.example.duan1.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan1.Dao.DienThoaiDao;
import com.example.duan1.Dao.KhoDao;
import com.example.duan1.Dao.NhanVienDao;
import com.example.duan1.Fragment.HoaDonFragment;
import com.example.duan1.Models.DienThoai;
import com.example.duan1.Models.HoaDon;
import com.example.duan1.Models.HoaDonChiTiet;
import com.example.duan1.Models.NhanVien;
import com.example.duan1.R;

import java.util.ArrayList;

public class HoaDonCtAdapter extends ArrayAdapter<HoaDonChiTiet> {

    private Context context;
    private ArrayList<HoaDonChiTiet> list;
    HoaDonFragment fragment;
    ImageView iv;
    TextView tvTen, tvSl, tvTien;
    Button bt;

    public HoaDonCtAdapter(Context context, HoaDonFragment fragment, ArrayList<HoaDonChiTiet> list){
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
            v = inf.inflate(R.layout.items_hoa_don_chi_tiet, null);
        }

        tvTen = v.findViewById(R.id.items_hdct_tv_tendt);
        tvSl = v.findViewById(R.id.items_hdct_tv_sl);
        tvTien = v.findViewById(R.id.items_hdct_tv_giaTien);
        bt = v.findViewById(R.id.items_hdct_bt);
        iv = v.findViewById(R.id.items_hdct_iv);

        final HoaDonChiTiet hdct = list.get(position);
        if (hdct != null){

            DienThoaiDao dtD = new DienThoaiDao(context);

            String tenDt = dtD.getID(String.valueOf(hdct.getMaDT())).getTenDT();
            Log.d("HDCTADAPTER", "getView: "+tenDt +hdct.getMaDT());
            String tien = String.format("%,.0f",hdct.getThanhTien());
//            Log.d("HDCT adapter: ", "Tên: "+tenDt+" Tiền: "+ tien+" sl: "+hdct.getSoLuong());
            tvTen.setText(""+tenDt);
            tvSl.setText("Số lượng: "+hdct.getSoLuong());
            tvTien.setText(""+tien);
            byte[] _img = dtD.getID(String.valueOf(hdct.getMaDT())).getImg();
            if (_img != null){ //nếu mà file có ảnh ms lấy
                Bitmap bitmap = BitmapFactory.decodeByteArray(_img, 0, _img.length);
                iv.setImageBitmap(bitmap);
            }
        }

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KhoDao kD = new KhoDao(context);
                int sl = kD.getSLConLai(hdct.getMaDT()) + hdct.getSoLuong();
                fragment.xoa_hdct(String.valueOf(hdct.getMaCT()), hdct.getMaHD(), sl, hdct.getMaDT());
            }
        });
        return v;
    }
}
