package com.example.duan1.Models;

import java.io.Serializable;

public class NhanVien implements Serializable {

    public String MaNV;
    public String Password;
    public String HoTen;
    public String Email;
    public byte[] HinhAnh;

    public NhanVien() {
    }

    public NhanVien(String maNV, String password, String hoTen, String Email,byte[] hinhAnh ) {
        this.MaNV = maNV;
        this.Password = password;
        this.HoTen = hoTen;
        this.Email = Email;
        this.HinhAnh = hinhAnh;
    }

    public String getMaNV() {
        return MaNV;
    }

    public void setMaNV(String maNV) {
        MaNV = maNV;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public byte[] getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        HinhAnh = hinhAnh;
    }
}
