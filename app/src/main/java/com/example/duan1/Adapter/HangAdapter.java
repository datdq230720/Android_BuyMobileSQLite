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


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.duan1.Fragment.HangFragment;
import com.example.duan1.Models.HangDt;
import com.example.duan1.R;

import java.util.ArrayList;

public class HangAdapter extends ArrayAdapter<HangDt> {

    private Context context;
    private ArrayList<HangDt> list;
    HangFragment fragment;

    TextView tvTen;
    ImageView iv;
    Button bt;


    public HangAdapter( Context _context, HangFragment _fragment, ArrayList<HangDt> _list) {
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
            v = inf.inflate(R.layout.items_hang, null);
        }

        final HangDt h = list.get(position);
        if (h != null){
            tvTen = v.findViewById(R.id.items_hang_tv_ten);
            iv = v.findViewById(R.id.items_hang_iv);
//            bt = v.findViewById(R.id.items_hang_bt_delete);

            tvTen.setText(h.getTenH()+"");
            Log.d("TEn:    ",""+h.getTenH());

            if (h.getImg() != null){ //nếu mà file có ảnh ms lấy
                byte[] _img = h.getImg();
                Bitmap bitmap = BitmapFactory.decodeByteArray(_img, 0, _img.length);
                iv.setImageBitmap(bitmap);
            }
        }
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fragment.xoa(String.valueOf(h.getMaH()));
//            }
//        });
        return v;
    }
}
