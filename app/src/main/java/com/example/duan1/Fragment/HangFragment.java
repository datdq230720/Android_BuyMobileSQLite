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
                // Th??m nh??n vi??n m???i
                openDialog(getActivity(), 0);
            }
        });
        DienThoaiDao dtD = new DienThoaiDao(getActivity());
        lv.setOnItemLongClickListener((adapterView, view, position, id) -> {
            hdt = list.get(position);
            setMaH(hdt.getMaH());
            // S???a d??? li???u
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

    // nh???n d??? li???u h??nh ???nh
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


        // n???u l?? s???a m???i ????a d??? li???u n??n
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
                // l???y t??? camera
                Intent pho=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // T??ch h???p
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
                    // d??nh cho s???a

                    hdt.setTenH(edTen.getText().toString());
                    hdt.setImg(saveImg());
                    if (type == 0){
                        if (hD.insert(hdt) > 0){
                            Toast.makeText(getContext(), "Th??m th??nh c??ng", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), "Th??m th???t b???i", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        hdt.setMaH(getMaH());
                        if (hD.update(hdt) > 0){
                            Toast.makeText(getContext(), "S???a th??nh c??ng", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), "S???a th???t b???i", Toast.LENGTH_SHORT).show();
                        }
                    }

                    capNhatLv();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    // nh???n d??? li???u h??nh ???nh ????? l??u
    private byte[] saveImg(){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) iv.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byOut = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byOut);
        byte[] image = byOut.toByteArray();

        return image;
    }
    // x??a d??? li???u ???????c g???i b??n adapter
    public void xoa(final String Id){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete");
        builder.setMessage("B???n c?? ch???c ch???n mu???n x??a kh??ng");
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
            Toast.makeText(getContext(), "Kh??ng ???????c ????? tr???ng t??n h??ng", Toast.LENGTH_SHORT).show();
            check = -1;
        }else if (iv.getDrawable() == null){
            Toast.makeText(getContext(), "Kh??ng ???????c ????? tr???ng ???nh", Toast.LENGTH_SHORT).show();
            check = -1;
        }else if (ten.startsWith(" ") || ten.endsWith(" ")){
            Toast.makeText(getContext(), "Kh??ng ???????c ????? kho???ng tr???ng ??? ?????u v?? cu???i t??n", Toast.LENGTH_SHORT).show();
            check = -1;
            edTen.setText("");
        }
        if (TYPE != 1) {
            Toast.makeText(getContext(), "TYPE: " + TYPE, Toast.LENGTH_SHORT).show();
            if (hD.checkTen(edTen.getText().toString()).equals(edTen.getText().toString())) {
                Toast.makeText(getContext(), "T??n c???a h??ng ???? t???n t???i", Toast.LENGTH_SHORT).show();
                check = -1;
                edTen.setText("");
            }
        }

        return check;
    }


}
