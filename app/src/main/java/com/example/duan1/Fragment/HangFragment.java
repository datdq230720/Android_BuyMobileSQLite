package com.example.duan1.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.duan1.Adapter.HangAdapter;
import com.example.duan1.Adapter.NhanVienAdapter;
import com.example.duan1.Dao.DienThoaiDao;
import com.example.duan1.Dao.HangDao;
import com.example.duan1.Dao.NhanVienDao;
import com.example.duan1.Models.HangDt;
import com.example.duan1.Models.NhanVien;
import com.example.duan1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class HangFragment extends Fragment implements View.OnClickListener{

    HangAdapter hA;
    HangDao hD;
    HangDt hdt;
    private ArrayList<HangDt> list;
    ListView lv;
    FloatingActionButton fab;
    Dialog dialog;

    //dialog
    EditText edTen;
    Button btSave, btReset;
    ImageView iv;

    int TYPE = 0, MaH;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hang,
                container, false);

        lv =  v.findViewById(R.id.hang_lv);
        fab = v.findViewById(R.id.hang_fab);

        hD = new HangDao(getActivity());
        capNhatLv();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Thêm nhân viên mới
                openDialog(getActivity(), 0);
            }
        });
        DienThoaiDao dtD = new DienThoaiDao(getActivity());
        lv.setOnItemLongClickListener((adapterView, view, position, id) -> {
            hdt = list.get(position);
            setMaH(hdt.getMaH());
            // Sửa dữ liệu
//            openDialog(getActivity(), 1);
            final PopupMenu menu = new PopupMenu(getActivity(), view);
            menu.getMenuInflater().inflate(R.menu.menu_item, menu.getMenu());
            //
            if (dtD.getByID(String.valueOf(hdt.getMaH())) != null){
                menu.getMenu().findItem(R.id.menu_xoa).setVisible(false);
            }
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.menu_sua:
                            openDialog(getActivity(), 1);
                            return true;
                        case R.id.menu_xoa:
                            xoa(String.valueOf(hdt.getMaH()));
                            return true;
                        default:
                            return onMenuItemClick(menuItem);
                    }
                }
            });
            menu.show();
            return false;
        });

        return  v;
    }

    private void setMaH(int MaH){
        this.MaH = MaH;
    }
    private int getMaH(){
        return MaH;
    }


    public void capNhatLv(){
        list = (ArrayList<HangDt>) hD.getAll();
        hA = new HangAdapter(getActivity(), this, list);
        lv.setAdapter(hA);
    }

    @Override
    public void onClick(View viewp) {
        viewp.post(new Runnable() {
            @Override
            public void run() {
                showPopupMenu(viewp);
            }
        });
    }
    private void showPopupMenu(View view) {

        PopupMenu popup = new PopupMenu(getActivity(), view);

        popup.getMenuInflater().inflate(R.menu.menu_item, popup.getMenu());

        popup.show();
    }

    // nhận dữ liệu hình ảnh
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();
                        if(data.getExtras()!=null)
                        {
                            Bundle extras = data.getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            iv.setImageBitmap(imageBitmap);
                        }
                        else{
                            Uri uri=data.getData();
                            iv.setImageURI(uri);
                        }
                    }
                }
            });

    public void openDialog(final Context context, final int type){

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.diablog_hang);

        edTen = dialog.findViewById(R.id.hang_diag_ed_ten);
        btSave = dialog.findViewById(R.id.hang_diag_bt_save);
        btReset = dialog.findViewById(R.id.hang_diag_bt_reset);
        iv = dialog.findViewById(R.id.hang_diag_iv);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);


        // nếu là sửa mới đưa dữ liệu nên
        if (type != 0){
            edTen.setText(hdt.getTenH());

            byte[] _img = hdt.getImg();
            Bitmap bitmap = BitmapFactory.decodeByteArray(_img, 0, _img.length);
            iv.setImageBitmap(bitmap);
        }
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pick=new Intent(Intent.ACTION_GET_CONTENT);
                pick.setType("image/*");
                // lấy từ camera
                Intent pho=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Tích hợp
                Intent chosser=Intent.createChooser(pick, "chon");

                chosser.putExtra(Intent.EXTRA_INITIAL_INTENTS,new Intent[]{pho});
                someActivityResultLauncher.launch(chosser);
            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edTen.setText("");
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TYPE = type;
                if (validate() > 0){
                    hdt = new HangDt();
                    // dành cho sửa

                    hdt.setTenH(edTen.getText().toString());
                    hdt.setImg(saveImg());
                    if (type == 0){
                        if (hD.insert(hdt) > 0){
                            Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        hdt.setMaH(getMaH());
                        if (hD.update(hdt) > 0){
                            Toast.makeText(getContext(), "Sửa thành công", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), "Sửa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    capNhatLv();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    // nhận dữ liệu hình ảnh để lưu
    private byte[] saveImg(){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) iv.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byOut = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byOut);
        byte[] image = byOut.toByteArray();

        return image;
    }
    // xóa dữ liệu được gọi bên adapter
    public void xoa(final String Id){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete");
        builder.setMessage("Bạn có chắc chắn muốn xóa không");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", (DialogInterface dialog, int id) -> {
            hD.delete(Id);
            capNhatLv();
            dialog.cancel();
        });
        builder.setNegativeButton("No", (dialog, id) ->{
            dialog.cancel();
        });
        AlertDialog alert = builder.create();
        builder.show();
    }

    public int validate(){
        int check = 1;
        String ten = edTen.getText().toString();
        if (edTen.getText().length() == 0){
            Toast.makeText(getContext(), "Không được để trống tên hãng", Toast.LENGTH_SHORT).show();
            check = -1;
        }else if (iv.getDrawable() == null){
            Toast.makeText(getContext(), "Không được để trống ảnh", Toast.LENGTH_SHORT).show();
            check = -1;
        }else if (ten.startsWith(" ") || ten.endsWith(" ")){
            Toast.makeText(getContext(), "Không được để khoảng trắng ở đầu và cuối tên", Toast.LENGTH_SHORT).show();
            check = -1;
            edTen.setText("");
        }
        if (TYPE != 1) {
            Toast.makeText(getContext(), "TYPE: " + TYPE, Toast.LENGTH_SHORT).show();
            if (hD.checkTen(edTen.getText().toString()).equals(edTen.getText().toString())) {
                Toast.makeText(getContext(), "Tên của hãng đã tồn tại", Toast.LENGTH_SHORT).show();
                check = -1;
                edTen.setText("");
            }
        }

        return check;
    }


}
