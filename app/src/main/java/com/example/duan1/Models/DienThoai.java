package com.example.duan1.Models;

public class DienThoai {
    private int maDT;
    private String tenDT;
    private int maHdt;
    private int soLuong;
    private Double gia;
    private byte[] img;

    public DienThoai() {
    }

    public DienThoai(int maDT, String tenDT, int maHdt, int soLuong, Double gia, byte[] img) {
        this.maDT = maDT;
        this.tenDT = tenDT;
        this.maHdt = maHdt;
        this.soLuong = soLuong;
        this.gia = gia;
        this.img = img;
    }

    public int getMaDT() {
        return maDT;
    }

    public void setMaDT(int maDT) {
        this.maDT = maDT;
    }

    public String getTenDT() {
        return tenDT;
    }

    public void setTenDT(String tenDT) {
        this.tenDT = tenDT;
    }

    public int getMaHdt() {
        return maHdt;
    }

    public void setMaHdt(int maHdt) {
        this.maHdt = maHdt;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public Double getGia() {
        return gia;
    }

    public void setGia(Double gia) {
        this.gia = gia;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
}
