package com.example.duan1.Models;

import java.util.Date;

public class HoaDon {

    private int maHD;
    private String MaNV;
    private String tenKH;
    private Date ngay;
    private Double tienTong;

    public HoaDon() {
    }

    public HoaDon(int maHD, String maNV, String tenKH, Date ngay, Double tienTong) {
        this.maHD = maHD;
        MaNV = maNV;
        this.tenKH = tenKH;
        this.ngay = ngay;
        this.tienTong = tienTong;
    }

    public int getMaHD() {
        return maHD;
    }

    public void setMaHD(int maHD) {
        this.maHD = maHD;
    }

    public String getMaNV() {
        return MaNV;
    }

    public void setMaNV(String maNV) {
        MaNV = maNV;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public Date getNgay() {
        return ngay;
    }

    public void setNgay(Date ngay) {
        this.ngay = ngay;
    }

    public Double getTienTong() {
        return tienTong;
    }

    public void setTienTong(Double tienTong) {
        this.tienTong = tienTong;
    }
}
