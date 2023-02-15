package com.example.duan1.Adapter.SpAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.duan1.Models.HangDt;
import com.example.duan1.R;

import java.util.ArrayList;

public class HangSpinnerAdapter extends ArrayAdapter<HangDt> {

    private Context context;
    private ArrayList<HangDt> list;
    private TextView tv;

    public HangSpinnerAdapter(Context context, ArrayList<HangDt> list){
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
            v = inf.inflate(R.layout.items_spinner_hang, null);

            final HangDt hdt = list.get(position);
            if (hdt != null){
                tv = v.findViewById(R.id.sp_h_tv);

                tv.setText("Tên hãng: "+hdt.getTenH());
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
            v = inf.inflate(R.layout.items_spinner_hang, null);

            final HangDt hdt = list.get(position);
            if (hdt != null){
                tv = v.findViewById(R.id.sp_h_tv);

                tv.setText("Tên hãng: "+hdt.getTenH());
            }
        }
        return v;
    }
}
