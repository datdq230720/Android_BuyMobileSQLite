package com.example.duan1.Fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.duan1.Dao.DienThoaiDao;
import com.example.duan1.Models.ModelPieChart;
import com.example.duan1.Dao.ThongKeDao;
import com.example.duan1.Models.DienThoai;
import com.example.duan1.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DoanhThuFragment extends Fragment {

    Button btTo, btFrom, btDt;
    EditText edTo, edFrom;
    TextView tvDoanhThu;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    int mYear, mMonth, mDay;
    ThongKeDao tkD;
    List<ModelPieChart> list;
    DienThoaiDao dtD;
    DienThoai dt;

    String from, to;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_doanh_thu,
                container, false);

        edTo = v.findViewById(R.id.dt_ed_to);
        edFrom = v.findViewById(R.id.dt_ed_from);
        tvDoanhThu = v.findViewById(R.id.dt_tv_dt);
        btTo = v.findViewById(R.id.dt_bt_to);
        btFrom = v.findViewById(R.id.dt_bt_from);
        btDt = v.findViewById(R.id.dt_bt_dt);
        PieChart pieChart = v.findViewById(R.id.dt_piechart);

        edTo.setEnabled(false);
        edFrom.setEnabled(false);

        tkD = new ThongKeDao(getActivity());
        dtD = new DienThoaiDao(getActivity());
        list = tkD.getChart();
        Pie(pieChart);

        btTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(
                        getActivity(), 0, mDateTuNgay, mYear, mMonth, mDay);
                d.show();
            }
        });
        btFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(
                        getActivity(), 0, mDateDenNgay, mYear, mMonth, mDay);
                d.show();
            }
        });
        btDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                to = edTo.getText().toString();
                from = edFrom.getText().toString();
                if (edFrom.getText().length() == 0){
                    Toast.makeText(getActivity(), "Không được để trống ngày bắt đầu ", Toast.LENGTH_SHORT).show();
                }else if (edTo.getText().length() == 0){
                    Toast.makeText(getActivity(), "Không được để trống  ngày kêt thúc", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        Date c = sdf.parse(from);
                        Date b = sdf.parse(to);
                        if (c.after(b)){
                            Toast.makeText(getActivity(), "Ngày bắt đầu không được lớn hơn ngày kêt thúc", Toast.LENGTH_SHORT).show();
                        }else {
                            ThongKeDao tkD = new ThongKeDao(getActivity());
                            int a = tkD.getDoanhThu(from, to);
                            tvDoanhThu.setText(""+a+ " VNĐ");
                            list = tkD.getChart2(from, to);
                            Pie(pieChart);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
//                ThongKeDao tkD = new ThongKeDao(getActivity());
//                int a = tkD.getDoanhThu(from, to);
////                String b = String.format("%,-1f", Double.parseDouble(String.valueOf(a)))+"";
////                Toast.makeText(getActivity(), ""+b, Toast.LENGTH_SHORT).show();
//                tvDoanhThu.setText(""+a+ " VNĐ");
            }
        });

        return  v;
    }

    DatePickerDialog.OnDateSetListener mDateTuNgay = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfYear) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfYear;
            GregorianCalendar c = new GregorianCalendar(mYear, mMonth, mDay);
            edTo.setText(sdf.format(c.getTime()));
        }
    };
    DatePickerDialog.OnDateSetListener mDateDenNgay = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfYear) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfYear;
            GregorianCalendar c = new GregorianCalendar(mYear, mMonth, mDay);
            edFrom.setText(sdf.format(c.getTime()));
        }
    };
    public void Pie(PieChart pieChart){

        ArrayList<PieEntry> visitor = new ArrayList<>();
        for (int i=0; i<list.size(); i++){
            int Tien = list.get(i).PieTien;
            DienThoai dt = new DienThoai();
            try {
                dt = dtD.getID(String.valueOf(list.get(i).PieMaDT));
            }catch (Exception e){

            }
            String TenDT = dt.getTenDT();
            visitor.add(new PieEntry(Tien, TenDT));
        }

        PieDataSet pieDataSet = new PieDataSet(visitor, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Doanh thu");
        pieChart.animate();
    }
}
