package com.example.duan1.Adapter.TabAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.duan1.Dao.DienThoaiDao;
import com.example.duan1.Dao.NhanVienDao;
import com.example.duan1.Fragment.TopFragment;
import com.example.duan1.Models.DienThoai;
import com.example.duan1.Models.NhanVien;
import com.example.duan1.Models.Top;
import com.example.duan1.Models.TopNv;
import com.example.duan1.R;

import java.util.ArrayList;

public class TopNvAdapter extends ArrayAdapter<TopNv> {

    private Context context;
    private ArrayList<TopNv> list;

    ImageView iv;
    TextView tvTen, tvMa, tvTien;

    public TopNvAdapter(Context context, ArrayList<TopNv> list){
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null){
            LayoutInflater inf=(LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.items_top_nv, null);
        }

        final TopNv topNv = list.get(position);
        if (topNv != null){
            tvTen = v.findViewById(R.id.topnv_ten);
            tvMa = v.findViewById(R.id.topnv_ma);
            tvTien = v.findViewById(R.id.topnv_tien);
            iv = v.findViewById(R.id.topnv_img);

            NhanVienDao nvD = new NhanVienDao(context);
            NhanVien nv = nvD.getByID(topNv.getManv());
            Log.d("TAG", "getView: ");
            DienThoai dt = new DienThoai();
            tvTen.setText(nv.getHoTen()+"");
            tvMa.setText(topNv.getManv()+"");
            tvTien.setText(String.format("%,.0f",topNv.getTien())+"$");


            if (nv.getHinhAnh() != null){ //nếu mà file có ảnh ms lấy
                byte[] _img = nv.getHinhAnh();
                Bitmap bitmap = BitmapFactory.decodeByteArray(_img, 0, _img.length);
                iv.setImageBitmap(bitmap);
            }
        }
        return v;

    }
}
