package com.example.duan1.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.duan1.Adapter.TopAdapter;
import com.example.duan1.Dao.ThongKeDao;
import com.example.duan1.Fragment.Tab.MyPager;
import com.example.duan1.Models.Top;
import com.example.duan1.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class TopFragment extends Fragment {

    private ListView lv;
    ArrayList<Top> list;
    TopAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top,
                container, false);

//        lv = view.findViewById(R.id.tk_lv);
//        ThongKeDao tkD = new ThongKeDao(getActivity());
//        list = (ArrayList<Top>) tkD.getTop();
//        adapter = new TopAdapter(getActivity(), this, list);
//        lv.setAdapter(adapter);


        return  view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] titles = {"Điện Thoại", "Nhân viên"};
        ViewPager2 viewPager = (ViewPager2) view.findViewById(R.id.my_view_pager);
        viewPager.setAdapter(new MyPager(getActivity()));
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.my_tabs);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(titles[position]))
                .attach();
    }
}
