package com.example.duan1.Adapter.SpAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.duan1.Models.DienThoai;
import com.example.duan1.Models.HangDt;
import com.example.duan1.R;

import java.util.ArrayList;

public class DienThoaiSpinnerAdapter extends ArrayAdapter<DienThoai> {

    private Context context;
    private ArrayList<DienThoai> list;
    private TextView tv;

    public DienThoaiSpinnerAdapter(Context context, ArrayList<DienThoai> list){
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null){
            LayoutInflater inf=(LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.items_spinner_dien_thoai, null);

            final DienThoai dt = list.get(position);
            if (dt != null){
                tv = v.findViewById(R.id.sp_dt_tv);

                tv.setText("Tên hãng: "+dt.getTenDT());
            }
        }

        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null){
            LayoutInflater inf=(LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.items_spinner_dien_thoai, null);

            final DienThoai dt = list.get(position);
            if (dt != null){
                tv = v.findViewById(R.id.sp_dt_tv);

                tv.setText("Tên hãng: "+dt.getTenDT());
            }
        }
        return v;
    }
}
