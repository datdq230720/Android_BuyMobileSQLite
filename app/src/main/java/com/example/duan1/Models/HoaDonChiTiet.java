package com.example.duan1.Models;

public class HoaDonChiTiet {

    private int maCT;
    private int maHD;
    private int maDT;
    private int soLuong;
    private Double thanhTien;

    public HoaDonChiTiet() {
    }

    public HoaDonChiTiet(int maCT, int maHD, int maDT, int soLuong, Double thanhTien) {
        this.maCT = maCT;
        this.maHD = maHD;
        this.maDT = maDT;
        this.soLuong = soLuong;
        this.thanhTien = thanhTien;
    }

    public int getMaCT() {
        return maCT;
    }

    public void setMaCT(int maCT) {
        this.maCT = maCT;
    }

    public int getMaHD() {
        return maHD;
    }

    public void setMaHD(int maHD) {
        this.maHD = maHD;
    }

    public int getMaDT() {
        return maDT;
    }

    public void setMaDT(int maDT) {
        this.maDT = maDT;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public Double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(Double thanhTien) {
        this.thanhTien = thanhTien;
    }
}
