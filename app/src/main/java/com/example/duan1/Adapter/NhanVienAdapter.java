package com.example.duan1.Adapter;



import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan1.Dao.NhanVienDao;
import com.example.duan1.Fragment.NhanVienFragment;
import com.example.duan1.Models.NhanVien;
import com.example.duan1.R;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class NhanVienAdapter extends ArrayAdapter<NhanVien> {
    private Context context;
    private ArrayList<NhanVien> list;
    NhanVienFragment fragment;

    //dialog
    TextView tvMa, tvTen, tvEmail;
    ImageView iv;
    Button bt;



    public NhanVienAdapter(Context _context, NhanVienFragment _fragment, ArrayList<NhanVien> _list){
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
            v = inf.inflate(R.layout.items_nhan_vien, null);
        }
        final NhanVien nv = list.get(position);
        if (nv != null){
            tvMa = v.findViewById(R.id.items_tv_maUser);
            tvTen = v.findViewById(R.id.items_tv_hoTen);
            tvEmail = v.findViewById(R.id.items_tv_email);
            iv = v.findViewById(R.id.items_iv_img);
//            bt = v.findViewById(R.id.items_bt_delete);

            tvMa.setText(" "+nv.getMaNV());
            tvTen.setText(" "+nv.getHoTen());
            tvEmail.setText(" "+nv.getEmail());

            if (nv.getHinhAnh() != null){ //nếu mà file có ảnh ms lấy
                byte[] _img = nv.getHinhAnh();
                Bitmap bitmap = BitmapFactory.decodeByteArray(_img, 0, _img.length);
                iv.setImageBitmap(bitmap);
            }
        }
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fragment.xoa(nv.getMaNV());
//            }
//        });

        return v;
    }
}
