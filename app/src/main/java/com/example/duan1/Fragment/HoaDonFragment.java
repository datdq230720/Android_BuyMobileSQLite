package com.example.duan1.Fragment;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.duan1.Adapter.HoaDonAdapter;
import com.example.duan1.Adapter.HoaDonCtAdapter;
import com.example.duan1.Adapter.SpAdapter.DienThoaiSpinnerAdapter;

import com.example.duan1.Dao.DienThoaiDao;

import com.example.duan1.Dao.HoaDonCtDao;
import com.example.duan1.Dao.HoaDonDao;
import com.example.duan1.Dao.KhoDao;
import com.example.duan1.Dao.NhanVienDao;
import com.example.duan1.MainActivity;
import com.example.duan1.Models.DienThoai;

import com.example.duan1.Models.HoaDon;
import com.example.duan1.Models.HoaDonChiTiet;
import com.example.duan1.Models.NhanVien;
import com.example.duan1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HoaDonFragment extends Fragment implements View.OnClickListener{

    private ListView lv, lv2;
    private FloatingActionButton fab;
    private EditText edNV, edKH;
    private TextView tvNgay;
    private Button btSave, btReset;
    // dialog hóa đơn chi tiết
    private Spinner sp;
    private EditText edSl;
    private Button btThem, btLuu;
    private String user1 = "";
    private MainActivity mainActivity;
    ArrayList<DienThoai> listDT;

    ArrayList<HoaDonChiTiet> listCt;
    HoaDonChiTiet hdct;
    HoaDonCtDao hdctD;
    HoaDonCtAdapter hdctA;

    HoaDonDao hdD;
    HoaDon hd;
    HoaDonAdapter hdA;
    private ArrayList<HoaDon> list;
    Dialog dialog;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    String maNV,  TenDT;
    int MaDT, number, MaHD;
    Double giaDT;

    NhanVienDao nvD;
    Context context2;

    int mYear, mMonth, mDay;
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hoa_don,
                container, false);

        lv = v.findViewById(R.id.hd_lv);
        fab = v.findViewById(R.id.hd_fab);
        mainActivity = (MainActivity) getActivity();
        maNV = mainActivity.getUser();

        hdD = new HoaDonDao(getActivity());
        capNhatLv();
        context2 = getActivity();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // new save
                openDialog(getActivity(), 0);
            }
        });
        lv.setOnItemLongClickListener((adapterView, view, position, id) -> {
            hd = list.get(position);
            MaHD = list.get(position).getMaHD();
            setMaHD(MaHD);
            Log.d("", "onCreateView: "+MaHD);
//            openDialog(getActivity(), 1);
            //
            final PopupMenu menu = new PopupMenu(getActivity(), view);
            menu.getMenuInflater().inflate(R.menu.menu_popup, menu.getMenu());
            menu.getMenu().findItem(R.id.menu_xoaHd).setVisible(false);
            if (maNV.equals(list.get(position).getMaNV()) || maNV.equals("admin")){
                menu.getMenu().findItem(R.id.menu_xoaHd).setVisible(true);
            }
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.menu_hd:
                            openDialog(getActivity(), 1);
                            return true;
                        case R.id.menu_hdct:
                            openDialog_hdct(getActivity(), getMaHD());
                            return true;
                        case R.id.menu_xoaHd:
                            xoa_hd(String.valueOf(getMaHD()));
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
    private void setMaHD(int MaHD){
        this.MaHD = MaHD;
    }
    private int getMaHD(){
        return MaHD;
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

        popup.getMenuInflater().inflate(R.menu.menu_popup, popup.getMenu());

        popup.show();
    }

    void capNhatLv(){
        list = (ArrayList<HoaDon>) hdD.getAll();
        hdA = new HoaDonAdapter(getActivity(), this, list);
        lv.setAdapter(hdA);
    }

    protected void openDialog(final Context context, final int type){


        dialog = new Dialog(context);
        dialog.setContentView(R.layout.diablog_hoa_don);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

        edNV = dialog.findViewById(R.id.hd_diag_ed_nv);
        edKH = dialog.findViewById(R.id.hd_diag_ed_kh);
        tvNgay = dialog.findViewById(R.id.hd_diag_tv_day);
        btSave = dialog.findViewById(R.id.hd_diag_bt_save);
        btReset = dialog.findViewById(R.id.hd_diag_bt_reset);

        hdctD = new HoaDonCtDao(context);

        nvD = new NhanVienDao(context);

        NhanVien nv = nvD.getID(maNV);
        edNV.setText(nv.getHoTen());
        edNV.setEnabled(false);

        tvNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(
                        getActivity(), 0, mDate, mYear, mMonth, mDay);
                d.show();
            }
            DatePickerDialog.OnDateSetListener mDate = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfYear) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfYear;
                    GregorianCalendar c = new GregorianCalendar(mYear, mMonth, mDay);
                    tvNgay.setText(sdf.format(c.getTime()));
                }
            };
        });
        Date currentTime = Calendar.getInstance().getTime();
        tvNgay.setText(sdf.format(currentTime));

        if (type != 0){
            edKH.setText(String.valueOf(hd.getTenKH()));
            tvNgay.setText(sdf.format(hd.getNgay()));

        }


        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edKH.setText("");
                tvNgay.setText("");
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate() > 0){
                    hd = new HoaDon();
                    hd.setMaNV(nv.getMaNV());
                    hd.setTienTong(0.00);
                    hd.setTenKH(edKH.getText().toString());
                    try {
                        hd.setNgay(sdf.parse(tvNgay.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (type == 0){
                        MaHD =  (int) hdD.insert(hd);
                        Log.d("", "onClick: "+MaHD);
                        if (MaHD > 0){

                            Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        hd.setMaHD(MaHD);
                        int a = hdctD.getTongTien(MaHD);
                        hd.setTienTong(Double.parseDouble(String.valueOf(a)));
                        if (hdD.update(hd) > 0){
                            Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                            Log.d("Ma hd:", "onClick: "+MaHD);
                            openDialog_hdct(context, MaHD);
                        }else {
                            Toast.makeText(context, "Sửa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                    capNhatLv();
                    dialog.dismiss();
                    openDialog_hdct(context, MaHD);
                }
            }
        });
        dialog.show();
    }

    public void xoa_hd(final String Id){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete");
        builder.setMessage("Bạn có chắc chắn muốn xóa không");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", (DialogInterface dialog, int id) -> {

            // Xóa cả hóa đơn chi tiết
            KhoDao kD = new KhoDao(getActivity());
            hdctD = new HoaDonCtDao(getActivity());
            for (int i = 0; i < hdctD.getAllHD(Id).size(); i++){
                hdct = hdctD.getAllHD(Id).get(i);
                int sl = kD.getSLConLai(hdct.getMaDT()) + hdct.getSoLuong();
                kD.updateSL(sl, hdct.getMaDT());
            }

            hdctD.deleteHD(Id);
            // Xóa hóa đơn
            hdD.delete(Id);
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
        String ten = edKH.getText().toString();
        if (edKH.getText().length() == 0){
            Toast.makeText(getContext(), "Không được để trống tên khách hàng", Toast.LENGTH_SHORT).show();
            check = -1;
        }else if (tvNgay.getText().length() == 0){
            Toast.makeText(getContext(), "Không được để trống ngày tạo hóa đơn", Toast.LENGTH_SHORT).show();
            check = -1;
        }else if (ten.startsWith(" ") || ten.endsWith(" ")) {
            Toast.makeText(getContext(), "Không được để khoảng trắng ở đầu và cuối tên", Toast.LENGTH_SHORT).show();
            check = -1;
            edKH.setText("");
        }
        return check;
    }

    void capNhatLv_hdct(int _maHD){
        listCt = (ArrayList<HoaDonChiTiet>) hdctD.getAllHD(String.valueOf(_maHD));
        hdctA = new HoaDonCtAdapter(getActivity(), this, listCt);
        lv2.setAdapter(hdctA);
    }

    protected void openDialog_hdct(final Context _context, int _type){


        dialog = new Dialog(_context);
        dialog.setContentView(R.layout.diablog_hoa_don_chi_tiet);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

        edSl = dialog.findViewById(R.id.hdct_diag_ed_sl);
        sp = dialog.findViewById(R.id.hdct_diag_sp);
        btLuu = dialog.findViewById(R.id.hdct_diag_bt_luu);
        btThem = dialog.findViewById(R.id.hdct_diag_bt_them);
        lv2 = dialog.findViewById(R.id.hdct_diag_lv);

        hdctD = new HoaDonCtDao(_context);
        capNhatLv_hdct(_type);
        DienThoaiDao dtD = new DienThoaiDao(_context);


        listDT = new ArrayList<DienThoai>();
        listDT = (ArrayList<DienThoai>) dtD.getAll();
        DienThoaiSpinnerAdapter dtSpa = new DienThoaiSpinnerAdapter(_context, listDT);
        sp.setAdapter(dtSpa);




        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MaDT = listDT.get(i).getMaDT();
                TenDT = listDT.get(i).getTenDT();
                giaDT = listDT.get(i).getGia();
                Toast.makeText(_context, "Chọn: "+MaDT+" " +TenDT+ " " +giaDT, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capNhatLv_hdct(_type);
                int a = hdctD.getTongTien(MaHD);
                hd.setTienTong(Double.parseDouble(String.valueOf(a)));
                capNhatLv();
                dialog.dismiss();
            }
        });

        btThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate_hdct() > 0){
                    hdct = new HoaDonChiTiet();
                    hdct.setMaDT(MaDT);
                    Log.d("HDCT: THEM", "onClick: "+MaDT);
                    int sl = Integer.parseInt(edSl.getText().toString());
                    hdct.setSoLuong(sl);
                    //Cập nhật lại số lượng cho kho
                    KhoDao khoDao = new KhoDao(_context);
                    sl = khoDao.getSLConLai(MaDT) - sl;
                    khoDao.updateSL(sl, MaDT);
                    //
//                    Double tien = giaDT*Double.parseDouble(edSl.getText().toString());
//                    hdct.setThanhTien(tien);
                    hdct.setMaHD(_type);
//                    if (hdctD.insert(hdct) > 0) {
//                        Toast.makeText(_context, "Thêm thành công", Toast.LENGTH_SHORT).show();
//                        edSl.setText("");
//                        // Dùng để cập nhập lại tổng tiền hóa đơn
//                        int a = hdctD.getTongTien(MaHD);
//                        hd.setTienTong(Double.parseDouble(String.valueOf(a)));
//                        hdD.updateH(Double.parseDouble(String.valueOf(a)), MaHD);
//                    } else {
//                        Toast.makeText(_context, "Thêm thất bại", Toast.LENGTH_SHORT).show();
//                    }
                    Log.d("TAG", "onClick: "+hdctD.getByIdDt(String.valueOf(MaDT), String.valueOf(MaHD)));
                    setMaHDCT((int) hdctD.getByIdDt(String.valueOf(MaDT), String.valueOf(MaHD)));
                    if (getMaHDCT() < 0){
                        Double tien = giaDT*Double.parseDouble(edSl.getText().toString());
                        hdct.setThanhTien(tien);
                        if (hdctD.insert(hdct) > 0) {
                            Toast.makeText(_context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            edSl.setText("");
                        // Dùng để cập nhập lại tổng tiền hóa đơn
                            int a = hdctD.getTongTien(MaHD);
                            hd.setTienTong(Double.parseDouble(String.valueOf(a)));
                            hdD.updateH(Double.parseDouble(String.valueOf(a)), MaHD);
                        } else {
                            Toast.makeText(_context, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        int slu = hdctD.getIdAll(String.valueOf(getMaHDCT())).getSoLuong() + Integer.parseInt(edSl.getText().toString());
                        Double tienU = hdctD.getIdTien(String.valueOf(getMaHDCT())) + giaDT*Double.parseDouble(edSl.getText().toString());
                        int b = hdctD.updateSL(getMaHDCT(), slu, tienU, getMaHD());
                        Log.d("update"+getMaHDCT(), "onClick: "+slu);
                        if (b > 0) {
                            Toast.makeText(_context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            edSl.setText("");
                            // Dùng để cập nhập lại tổng tiền hóa đơn
                            int a = hdctD.getTongTien(MaHD);
                            hd.setTienTong(Double.parseDouble(String.valueOf(a)));
                            hdD.updateH(Double.parseDouble(String.valueOf(a)), MaHD);
                        } else {
                            Log.d("update", "onClick: "+b);
                            Toast.makeText(_context, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                capNhatLv_hdct(_type);
                capNhatLv();
            }
        });
        dialog.show();
    }
    int mct;
    private void setMaHDCT(int mct){
        this.mct = mct;
    }
    private int getMaHDCT(){
        return mct;
    }

    public void xoa_hdct(final String Id, int _MaHD, int Sl, int _maDT){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete");
        builder.setMessage("Bạn có chắc chắn muốn xóa không");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", (DialogInterface dialog, int id) -> {
            hdctD.delete(Id);
            capNhatLv();
            capNhatLv_hdct(_MaHD);
            KhoDao kD = new KhoDao(getActivity());
            kD.updateSL(Sl, _maDT);
            int a = hdctD.getTongTien(_MaHD);
            hdD.updateH(Double.parseDouble(String.valueOf(a)), MaHD);
            dialog.cancel();
        });
        builder.setNegativeButton("No", (dialog, id) ->{
            dialog.cancel();
        });
        AlertDialog alert = builder.create();
        builder.show();
    }

    public int validate_hdct(){
        KhoDao kD = new KhoDao(getActivity());
        int check = 1;
        if (edSl.getText().length() == 0){
            Toast.makeText(getContext(), "Không được để trống số lượng", Toast.LENGTH_SHORT).show();
            check = -1;
        }else if (sp.getSelectedItem() == null){
            Toast.makeText(getContext(), "Chưa có dữ liệu điện thoại", Toast.LENGTH_SHORT).show();
            check = -1;
        }else if(Integer.parseInt(edSl.getText().toString()) > kD.getSLConLai(MaDT)){
            Toast.makeText(getContext(), "Số lượng trong kho không đủ", Toast.LENGTH_SHORT).show();
            check = -1;
        }
        return check;
    }


}