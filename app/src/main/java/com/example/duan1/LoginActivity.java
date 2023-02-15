package com.example.duan1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan1.Dao.NhanVienDao;
import com.example.duan1.Models.NhanVien;

public class LoginActivity extends AppCompatActivity {

    EditText ed_name, ed_pass;
    Button bt_dn;
    CheckBox ckb;
    TextView foget;
    String user, pass;
    NhanVienDao nvD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ed_name = findViewById(R.id.ed_maNV);
        ed_pass = findViewById(R.id.ed_pass);
        bt_dn = findViewById(R.id.bt_login);
        ckb = findViewById(R.id.checkBox);
        foget = findViewById(R.id.tv_foget);

        nvD = new NhanVienDao(LoginActivity.this);
        SharedPreferences pref = getSharedPreferences("userFile", MODE_PRIVATE);
        ed_name.setText(pref.getString("userName", ""));
        ed_pass.setText(pref.getString("userPass", ""));
        ckb.setChecked(pref.getBoolean("remember", false));

        bt_dn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckLogin();
            }
        });

        foget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(i);
            }
        });
    }
    public void CheckLogin(){
        user = ed_name.getText().toString();
        pass = ed_pass.getText().toString();
        NhanVien nhanVien;

        if (user.isEmpty() || pass.isEmpty()){
            Toast.makeText(getApplicationContext(), "Tên đăng nhập và mật khẩu không được bỏ trống", Toast.LENGTH_SHORT).show();
        }else {

            if (nvD.checkLogin(user, pass) > 0){
                Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                rememberUser(user, pass, ckb.isChecked());
                Log.d("DNTC", "Đăng nhập thành công");

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("user", user);
                startActivity(i);
                finish();
            }else {
                Log.d("DNTC", "Đăng nhập thất bại");
                ed_name.setText("");
                ed_pass.setText("");
                Toast.makeText(getApplicationContext(), "Tên đăng nhập hoặc mật khẩu không đúng ", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void rememberUser(String user, String pass, Boolean status){
        SharedPreferences pref = getSharedPreferences("userFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (!status){
            // xóa lưu trữ trước đó
            editor.clear();
        }else {
            editor.putString("userName", user);
            editor.putString("userPass", pass);
            editor.putBoolean("remember", status);
        }
        editor.commit();
    }
}