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
import com.example.duan1.Adapter.NhanVienAdapter;
import com.example.duan1.Dao.NhanVienDao;
import com.example.duan1.Models.NhanVien;
import com.example.duan1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class NhanVienFragment extends Fragment implements View.OnClickListener{

    NhanVienAdapter nvA;
    NhanVienDao nvD;
    NhanVien nv;
    private ArrayList<NhanVien> list;
    private ListView lv;
    FloatingActionButton fab;
    Dialog dialog;

    //dialog
    EditText edMa, edPass, edTen, edEmail;
    Button btSave, btReset;
    ImageView iv;

    String MaNv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nhan_vien,
                container, false);

        lv =  view.findViewById(R.id.nv_lv);
        fab = view.findViewById(R.id.nv_fab);

        nvD = new NhanVienDao(getActivity());
        capNhatLv();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // Thêm nhân viên mới
                openDialog(getActivity(), 0);
            }
        });


        lv.setOnItemLongClickListener((adapterView, _view, position, id) -> {
            nv = list.get(position);
            setMaNv(nv.getMaNV());
            //update
//            openDialog(getActivity(), 1);
            final PopupMenu menu = new PopupMenu(getActivity(), _view);
            menu.getMenuInflater().inflate(R.menu.menu_item, menu.getMenu());
            if (nv.getMaNV().equals("admin")){
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
                            xoa(getMaNv());
                            return true;
                        default:
                            return onMenuItemClick(menuItem);
                    }
                }
            });
            menu.show();
            return false;
        });

        return  view;
    }

    private void setMaNv(String MaNv){
        this.MaNv = MaNv;
    }
    private String getMaNv(){
        return MaNv;
    }

    public void capNhatLv(){
        list = (ArrayList<NhanVien>) nvD.getAll();
        nvA = new NhanVienAdapter(getActivity(), this,  list);
        lv.setAdapter(nvA);
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
    private byte[] saveImg(){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) iv.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byOut = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byOut);
        byte[] image = byOut.toByteArray();

        return image;
    }

    public void openDialog(final Context context, final int type){

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.diablog_nhan_vien);

        edMa = dialog.findViewById(R.id.nv_diag_ed_ma);
        edPass = dialog.findViewById(R.id.nv_diag_ed_mk);
        edTen = dialog.findViewById(R.id.nv_diag_ed_hoTen);
        edEmail = dialog.findViewById(R.id.nv_diag_ed_email);
        btSave = dialog.findViewById(R.id.nv_diag_bt_save);
        btReset = dialog.findViewById(R.id.nv_diag_bt_reset);
        iv = dialog.findViewById(R.id.nv_diag_iv);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (type != 0){
            edMa.setText(nv.getMaNV());
            edMa.setEnabled(false);
            edTen.setText(nv.getHoTen());
            edPass.setText(nv.getPassword());
            edEmail.setText(nv.getEmail());

            if (nv.getHinhAnh() != null){
                byte[] _img = nv.getHinhAnh();
                Bitmap bitmap = BitmapFactory.decodeByteArray(_img, 0, _img.length);
                iv.setImageBitmap(bitmap);
            }
        }

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // lấy ảnh từ libary
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
                edPass.setText("");
                edTen.setText("");
                edEmail.setText("");
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate(type) > 0){
                    nv = new NhanVien();
                    nv.setMaNV(edMa.getText().toString());
                    nv.setPassword(edPass.getText().toString());
                    nv.setHoTen(edTen.getText().toString());
                    nv.setEmail(edEmail.getText().toString());
                    nv.setHinhAnh(saveImg());

                    if (type == 0){
                        if (nvD.insert(nv) > 0){
                            Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        if (nvD.update(nv) > 0){
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

    public void xoa(final String Id){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete");
        builder.setMessage("Bạn có chắc chắn muốn xóa không");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", (DialogInterface dialog, int id) -> {
            nvD.delete(Id);
            capNhatLv();
            dialog.cancel();
        });
        builder.setNegativeButton("No", (dialog, id) ->{
            dialog.cancel();
        });
        AlertDialog alert = builder.create();
        builder.show();
    }

    public int validate(int type){
        int check = 1;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        String USERNAME = "[A-Za-z0-9]+";
        String ten = edTen.getText().toString();
        String user = edMa.getText().toString();
        if (edMa.getText().length() == 0){
            Toast.makeText(getContext(), "Tên đăng nhập không được để trống ", Toast.LENGTH_SHORT).show();
            check = -1;
        }else if (edPass.getText().length() == 0){
            Toast.makeText(getContext(), "Mật khẩu không được để trống ", Toast.LENGTH_SHORT).show();
            check = -1;
        }else if (edTen.getText().length() == 0){
            Toast.makeText(getContext(), "Không được để trống họ tên", Toast.LENGTH_SHORT).show();
            check = -1;
        }else if (edEmail.getText().length() == 0){
            Toast.makeText(getContext(), "Không được để trống email", Toast.LENGTH_SHORT).show();
            check = -1;
        }else if (iv.getDrawable() == null) {
            Toast.makeText(getContext(), "Không được để trống ảnh", Toast.LENGTH_SHORT).show();
            check = -1;
        }else if (edEmail.getText().toString().matches(EMAIL_PATTERN) == false){
            Toast.makeText(getContext(), "Email sai định dạng", Toast.LENGTH_SHORT).show();
            edEmail.setText("");
            check = -1;
        } else if (ten.endsWith(" ") || ten.startsWith(" ")){
            Toast.makeText(getContext(), "Không được phép để khoảng trắng đầu dòng hoặc cuối dòng", Toast.LENGTH_SHORT).show();
            edTen.setText("");
            check = -1;
        } else if (ten.length() < 3 || ten.length() > 30){
            Toast.makeText(getContext(), "Tên đăng nhập không được ít hơn 3 kí tự và lớn hơn 30 kí tự", Toast.LENGTH_SHORT).show();
            edTen.setText("");
            check = -1;
        }else if (edPass.getText().length() < 6){
            Toast.makeText(getContext(), "Mật khẩu không được ít hơn 6 kí tự", Toast.LENGTH_SHORT).show();
            edPass.setText("");
            check = -1;
        }else if (edMa.getText().toString().matches(USERNAME) == false){
            Toast.makeText(getContext(), "Tên đăng nhập không được chứa các kí tự đặc biệt", Toast.LENGTH_SHORT).show();
            edMa.setText("");
            check = -1;
        } else if (edPass.getText().toString().matches(USERNAME) == false){
            Toast.makeText(getContext(), "Mật khẩu không được chứa các kí tự đặc biệt", Toast.LENGTH_SHORT).show();
            edPass.setText("");
            check = -1;
        }

        if (type == 0){
            if (nvD.getByID(edMa.getText().toString()) != null){
                Toast.makeText(getContext(), "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                edMa.setText("");
                check = -1;
            }
        }

        return check;
    }
}
