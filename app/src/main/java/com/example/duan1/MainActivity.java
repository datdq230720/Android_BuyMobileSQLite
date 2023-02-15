package com.example.duan1;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan1.Dao.NhanVienDao;
import com.example.duan1.Fragment.DienThoaiFragment;
import com.example.duan1.Fragment.DoanhThuFragment;
import com.example.duan1.Fragment.DoiMatKhauFragment;
import com.example.duan1.Fragment.HangFragment;
import com.example.duan1.Fragment.HoaDonFragment;
import com.example.duan1.Fragment.NhanVienFragment;
import com.example.duan1.Fragment.TopFragment;
import com.example.duan1.Models.NhanVien;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    ImageView avatar;
    View v;
    TextView tv_nameUser;
    NhanVienDao nvD;
    NhanVien nv;
    private String user;
    int a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_navigation);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        drawerLayout.addDrawerListener(drawerToggle);

        navigationView = (NavigationView) findViewById(R.id.drawer_navigation_view);


        Intent i = getIntent();
        user = i.getStringExtra("user");
        Log.d("", "onCreate: "+user);
        Toast.makeText(this, ""+user, Toast.LENGTH_SHORT).show();




        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {

                selectDrawerItem(item);
                return true;
            }
        });

        if (savedInstanceState == null) {
            Class fragmentClass = HoaDonFragment.class;
            navigationView.getMenu().getItem(0).setChecked(true);
            v = navigationView.getHeaderView(0);




            nvD = new NhanVienDao(this);
            nv = nvD.getID(user);
            String Name = nv.getHoTen();


            tv_nameUser = v.findViewById(R.id.header_nameUser);
            avatar = v.findViewById(R.id.img_avatar);



            // set hình ảnh và tên ở navigation header
            tv_nameUser.setText(Name);

            if (nv.getHinhAnh() != null){ //nếu mà file có ảnh ms lấy
                byte[] _img = nv.getHinhAnh();
                Bitmap bitmap = BitmapFactory.decodeByteArray(_img, 0, _img.length);
                avatar.setImageBitmap(bitmap);
            }

            // Thay avatar của bản thân
            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getAvatar();
                }
            });

            // admin có quyền add user
            navigationView.getMenu().findItem(R.id.action_qlThanhVien).setVisible(false);
            if (user.equals("admin")) {
                navigationView.getMenu().findItem(R.id.action_qlThanhVien).setVisible(true);
            }
            try {
                Fragment fragment = (Fragment) fragmentClass.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.drawer_Framelayout,
                        fragment).commit();
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_OK) {
            Uri test = data.getData();
            System.out.println(test);
            if(data.getExtras()!=null)
            {

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                avatar.setImageBitmap(imageBitmap);
                Log.d("Ma 1 là: ", "_____" +a);
                SaveImg(String.valueOf(a));
            }
            else{

                Uri uri=data.getData();
                avatar.setImageURI(uri);
                SaveImg(String.valueOf(a));
                Log.d("Ma 2 là: ", "_____" +a);

            }

        }

    }

    // dùng để lưu ảnh
    private void SaveImg(String id){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) avatar.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byOut = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byOut);
        byte[] image = byOut.toByteArray();


        nv.setHinhAnh(image);
        nvD.update(nv);

    }

    private void getAvatar(){
        // lấy từ library
        Intent pick=new Intent(Intent.ACTION_GET_CONTENT);
        pick.setType("image/*");
        // lấy từ camera
        Intent pho=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Tích hợp
        Intent chosser=Intent.createChooser(pick, "chon");
        chosser.putExtra(Intent.EXTRA_INITIAL_INTENTS,new Intent[]{pho});
        startActivityForResult(chosser, 999);
    }

    private void selectDrawerItem(MenuItem item){
        Fragment fragment = null;
        Class fragmentClass;
        switch (item.getItemId()){
            case R.id.action_quanly:
                setTitle("Quản lý hóa đơn");
                fragmentClass = HoaDonFragment.class;
                break;
            case R.id.action_qlHang:
                setTitle("Quản lý hãng điện thoại");
                fragmentClass = HangFragment.class;
                break;
            case R.id.action_qlDt:
                setTitle("Quản lý điện thoại");
                fragmentClass = DienThoaiFragment.class;
                break;
            case R.id.action_qlThanhVien:
                setTitle("Quản lý nhân viên");
                fragmentClass = NhanVienFragment.class;
                break;
            case R.id.action_tMuon:
                setTitle("Top 10");
                fragmentClass = TopFragment.class;
                break;
            case R.id.action_doanhThu:
                setTitle("Doanh thu");
                fragmentClass = DoanhThuFragment.class;
                break;
            case R.id.action_doiPass:
                setTitle("Đổi mật khẩu");
                fragmentClass = DoiMatKhauFragment.class;
                break;
            case R.id.action_logout:
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                return;
            default:
                fragmentClass = HoaDonFragment.class;
                break;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        }catch (Exception e){
            Toast.makeText(this, "Lỗi: "+e, Toast.LENGTH_SHORT).show();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.drawer_Framelayout, fragment).commit();

        item.setChecked(true);
        setTitle(item.getTitle());
        drawerLayout.closeDrawers();

    }
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistableBundle) {
        super.onPostCreate(savedInstanceState, persistableBundle);
        drawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public String getUser(){
        return user;
    }
}