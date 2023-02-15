package com.example.duan1.Adapter;

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
import com.example.duan1.Dao.HangDao;
import com.example.duan1.Fragment.TopFragment;
import com.example.duan1.Models.DienThoai;
import com.example.duan1.Models.HangDt;
import com.example.duan1.Models.Top;
import com.example.duan1.R;

import java.util.ArrayList;

public class TopAdapter extends ArrayAdapter<Top> {
    private Context context;
    TopFragment fragment;
    private ArrayList<Top> list;
    TextView tvTenD, tvS;
    ImageView iv;

    public TopAdapter(Context context, TopFragment fragment, ArrayList<Top> lists) {
        super(context, 0, lists);
        this.context = context;
        this.fragment = fragment;
        this.list = lists;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null){
            LayoutInflater inf=(LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.items_top, null);
        }

        final Top top = list.get(position);
        if (top != null){
            tvTenD = v.findViewById(R.id.items_top_tv_ten);
            tvS = v.findViewById(R.id.items_top_tv_sl);
            iv = v.findViewById(R.id.items_top_iv);

            DienThoaiDao dtD = new DienThoaiDao(context);
            int sl = top.getSoLuong();
            tvS.setText("số lượng: "+sl);
            Log.d("TAG", "getView: "+top.getSoLuong());
            DienThoai dt = new DienThoai();
            try {
                dt = dtD.getID(String.valueOf(top.getMadt()));
            }catch (Exception e){

            }
            tvTenD.setText(dt.getTenDT()+"");


            if (dt.getImg() != null){ //nếu mà file có ảnh ms lấy
                byte[] _img = dt.getImg();
                Bitmap bitmap = BitmapFactory.decodeByteArray(_img, 0, _img.length);
                iv.setImageBitmap(bitmap);
            }
        }
        return v;
    }

}
