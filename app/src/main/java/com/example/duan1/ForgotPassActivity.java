package com.example.duan1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan1.Dao.NhanVienDao;
import com.example.duan1.Models.NhanVien;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class ForgotPassActivity extends AppCompatActivity {

    EditText ed_email, ed_account;
    TextView tv, tv_send;
    Button bt_send;
    NhanVienDao nvD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        ed_account = findViewById(R.id.ed_acount);
        ed_email = findViewById(R.id.ed_email);
        tv = findViewById(R.id.tv_login);
        bt_send = findViewById(R.id.bt_send);
        tv_send = findViewById(R.id.textView2);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForgotPassActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
        nvD = new NhanVienDao(ForgotPassActivity.this);
        bt_send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (validate() > 0){
                    // code gửi email
                    String passnew = newPass();

                    NhanVien nv = nvD.getID(ed_account.getText().toString());
                    nv.setPassword(passnew);
                    if (nvD.update(nv) > 0){
                        Toast.makeText(ForgotPassActivity.this, "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        tv_send.setText("Mật khẩu mới của bạn là: "+passnew);
                    }else {
                        Toast.makeText(ForgotPassActivity.this, "Thay đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                    //
                    Toast.makeText(ForgotPassActivity.this, "Mk đã được gửi đến email của bạn", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public int validate(){
        int check = 1;
        if (ed_account.getText().length() == 0){
            Toast.makeText(this, "Không được để trống tên đăng nhập", Toast.LENGTH_SHORT).show();
            check = -1;
        }else if (ed_email.getText().length() == 0){
            Toast.makeText(this, "Không được để trống eamil", Toast.LENGTH_SHORT).show();
            check = -1;
        }
        try {
            nvD.fogotPass(ed_account.getText().toString()).getMaNV();
        }catch (Exception e){
            Toast.makeText(this, "Tên đăng nhập không tồn tại", Toast.LENGTH_SHORT).show();
            check = -1;
        }
        try {
            String a = nvD.fogotPass(ed_account.getText().toString()).getEmail();
            Log.d("aaaaaa", "validate: "+a);
            if (!ed_email.getText().equals(a)){
                Toast.makeText(this, "Tên email không đúng", Toast.LENGTH_SHORT).show();
                check = -1;
            }
        }catch (Exception e){
        }
        return check;
    }

    private static final String alpha = "abcdefghijklmnopqrstuvwxyz"; // a-z
    private static final String alphaUpperCase = alpha.toUpperCase(); // A-Z
    private static final String digits = "0123456789"; // 0-9
    private static final String ALL = alpha + alphaUpperCase + digits;

    private Random generator = new Random();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String randomPassword(int numberOfCharactor) {
        List<String> result = new ArrayList<>();
        Consumer<String> appendChar = s -> {
            int number = randomNumber(0, s.length() - 1);
            result.add("" + s.charAt(number));
        };
        appendChar.accept(digits);
        while (result.size() < numberOfCharactor) {
            appendChar.accept(ALL);
        }
        Collections.shuffle(result, generator);
        return String.join("", result);
    }

    public int randomNumber(int min, int max) {
        return generator.nextInt((max - min) + 1) + min;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String newPass(){
        int numberOfCharactor = 8;
        String PassWord = randomPassword(numberOfCharactor);
        return PassWord;
    }
}