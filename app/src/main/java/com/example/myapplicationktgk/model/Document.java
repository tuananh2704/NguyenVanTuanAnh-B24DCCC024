package com.example.myapplicationktgk.model;

public abstract class Document {
    private final String maTaiLieu;
    private final String tenTaiLieu;
    private double giaTien;

    public Document(String maTaiLieu, String tenTaiLieu, double giaTien) {
        this.maTaiLieu = maTaiLieu;
        this.tenTaiLieu = tenTaiLieu;
        setGiaTien(giaTien);
    }

    public String getMaTaiLieu() {
        return maTaiLieu;
    }

    public String getTenTaiLieu() {
        return tenTaiLieu;
    }

    public double getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(double giaTien) {
        if (giaTien > 0) {
            this.giaTien = giaTien;
        } else {
            throw new IllegalArgumentException("Giá tiền phải lớn hơn 0");
        }
    }

    public abstract double tinhPhiMuon();

    public void hienThiThongTin() {
        System.out.println("Mã: " + maTaiLieu
                + " | Tên: " + tenTaiLieu
                + " | Giá: " + giaTien);
    }
}
