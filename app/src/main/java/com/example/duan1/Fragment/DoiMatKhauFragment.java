package com.example.duan1.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.duan1.Dao.NhanVienDao;
import com.example.duan1.LoginActivity;
import com.example.duan1.Models.NhanVien;
import com.example.duan1.R;

public class DoiMatKhauFragment extends Fragment {

    EditText ed_passOld, ed_pass, ed_pass2;
    Button bt_dmk, bt_h;
    NhanVienDao nvD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doi_mat_khau,
                container, false);

        ed_passOld = view.findViewById(R.id.dmk_ed_oldPass);
        ed_pass = view.findViewById(R.id.dmk_ed_newPass1);
        ed_pass2 = view.findViewById(R.id.dmk_ed_newPass2);
        bt_dmk = view.findViewById(R.id.dmk_bt_save);
        bt_h = view.findViewById(R.id.dmk_bt_reset);

        nvD = new NhanVienDao(getActivity());

        bt_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_passOld.setText("");
                ed_pass.setText("");
                ed_pass2.setText("");
            }
        });
        bt_dmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getActivity().getSharedPreferences("userFile", Context.MODE_PRIVATE);
                String user = pref.getString("userName","");
                Log.d("NAME ____________>", ""+user);
                if (validate() > 0){
                    NhanVien nv = nvD.getID(user);
                    nv.setPassword(ed_pass.getText().toString());
                    if (nvD.update(nv) > 0){
                        Toast.makeText(getActivity(), "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        ed_passOld.setText("");
                        ed_pass.setText("");
                        ed_pass2.setText("");
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    }else {
                        Toast.makeText(getActivity(), "Thay đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        return  view;
    }

    public int validate(){
        int check = 1;
        int check2 = 1;
        String PASS = "[A-Za-z0-9]+";
        if (ed_passOld.getText().length() == 0 || ed_pass.getText().length() == 0 || ed_pass2.getText().length() == 0){
            Toast.makeText(getActivity(), "Không được bỏ trống", Toast.LENGTH_SHORT).show();
            check = -1;
            check2 = -1;
        }else {
            SharedPreferences pref = getActivity().getSharedPreferences("userFile", Context.MODE_PRIVATE);
            String passOld = pref.getString("userPass","");
            String pass = ed_pass.getText().toString();
            String rePass = ed_pass2.getText().toString();
            if (!passOld.equals(ed_passOld.getText().toString())){
                Toast.makeText(getActivity(), "Mật khẩu cũ sai", Toast.LENGTH_SHORT).show();
                ed_passOld.setText("");
                check = -1;
                check2 = -1;
            }else if (!pass.equals(rePass)){
                Toast.makeText(getActivity(), "Nhập lại mật kẩu mới không khớp", Toast.LENGTH_SHORT).show();
                check = -1;
                check2 = -1;
            }else if (passOld.equals(pass)){
                Toast.makeText(getActivity(), "Mật khẩu mới không được trùng với mật khẩu cũ", Toast.LENGTH_SHORT).show();
                check = -1;
                check2 = -1;
            }
        }
        if (ed_pass.getText().length() < 6 && check2 == 1){
            Toast.makeText(getContext(), "Mật khẩu không được ít hơn 6 kí tự", Toast.LENGTH_SHORT).show();
            ed_pass.setText("");
            check = -1;
        }else if (ed_pass.getText().toString().matches(PASS) == false && check2 == 1){
            Toast.makeText(getContext(), "Mật khẩu được chứa các kí tự đặc biệt", Toast.LENGTH_SHORT).show();
            ed_pass.setText("");
            check = -1;
        }
        return check;
    }

}
