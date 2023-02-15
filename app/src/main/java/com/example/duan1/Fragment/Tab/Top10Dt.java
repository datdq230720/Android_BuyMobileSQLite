package com.example.duan1.Fragment.Tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duan1.Adapter.TabAdapter.TopNvAdapter;
import com.example.duan1.Dao.ThongKeDao;
import com.example.duan1.Models.TopNv;
import com.example.duan1.R;

import java.util.ArrayList;

public class Top10Dt extends Fragment {

    private ListView lv;
    ArrayList<TopNv> list2;
    TopNvAdapter adapter2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_top10_dt,
                container, false);

        lv = view.findViewById(R.id.tk_topdt_lv);
        ThongKeDao tkD = new ThongKeDao(getActivity());

        list2 = (ArrayList<TopNv>) tkD.getTopNV();
        adapter2 = new TopNvAdapter(getActivity(), list2);
        lv.setAdapter(adapter2);

        return view;
    }
}
