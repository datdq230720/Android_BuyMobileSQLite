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
import android.widget.TextView;

import com.example.duan1.Dao.HangDao;
import com.example.duan1.Fragment.DienThoaiFragment;
import com.example.duan1.Fragment.HangFragment;
import com.example.duan1.Models.DienThoai;
import com.example.duan1.Models.HangDt;
import com.example.duan1.R;

import java.util.ArrayList;
import java.util.List;

public class DienThoaiAdapter extends ArrayAdapter<DienThoai> {

    private Context context;
    private ArrayList<DienThoai> list;
    DienThoaiFragment fragment;

    TextView tvTen, tvHang, tvGia, tvSl;
    ImageView iv;
    Button bt;


    public DienThoaiAdapter(Context _context, DienThoaiFragment _fragment, ArrayList<DienThoai> _list) {
        super(_context, 0, _list);
        this.context = _context;
        this.fragment = _fragment;
        this.list = _list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null){
            LayoutInflater inf=(LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.items_dien_thoai, null);
        }

        final DienThoai dt = list.get(position);
        if (dt != null){
            tvTen = v.findViewById(R.id.items_dt_tv_ten);
            tvHang = v.findViewById(R.id.items_dt_tv_hang);
            tvGia = v.findViewById(R.id.items_dt_tv_gia);
            tvSl = v.findViewById(R.id.items_dt_tv_sl);
            iv = v.findViewById(R.id.items_dt_iv);
//            bt = v.findViewById(R.id.items_dt_bt);

            tvTen.setText(dt.getTenDT());
            HangDao hD = new HangDao(context);
            HangDt hdt = hD.getID(String.valueOf(dt.getMaHdt()));
            tvHang.setText(""+hdt.getTenH());
            tvGia.setText(String.format("%,.0f",dt.getGia())+"$");
            tvSl.setText("số lượng: "+dt.getSoLuong());

            if (dt.getImg() != null){ //nếu mà file có ảnh ms lấy
                byte[] _img = dt.getImg();
                Bitmap bitmap = BitmapFactory.decodeByteArray(_img, 0, _img.length);
                iv.setImageBitmap(bitmap);
            }
        }
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fragment.xoa(String.valueOf(dt.getMaDT()));
//            }
//        });
        return v;
    }

}
