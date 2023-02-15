package com.example.duan1.Fragment.Tab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duan1.Adapter.TabAdapter.TopNvAdapter;
import com.example.duan1.Adapter.TopAdapter;
import com.example.duan1.Dao.ThongKeDao;
import com.example.duan1.Models.Top;
import com.example.duan1.Models.TopNv;
import com.example.duan1.R;

import java.util.ArrayList;

public class Top10 extends Fragment {
    private ListView lv;
    ArrayList<Top> list;

    TopAdapter adapter;


    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_top10,
                container, false);
        lv = view.findViewById(R.id.tk_top_lv);
        ThongKeDao tkD = new ThongKeDao(getActivity());
            list = (ArrayList<Top>) tkD.getTop();
            adapter = new TopAdapter(getActivity(), null, list);
            lv.setAdapter(adapter);


        return view;
    }
}
