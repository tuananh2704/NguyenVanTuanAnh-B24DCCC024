package com.example.myapplicationktgk.model;

public class Book extends Document {
    private int soTrang;

    public Book(String ma, String ten, double gia, int soTrang) {
        super(ma, ten, gia);
        setSoTrang(soTrang);
    }

    public int getSoTrang() {
        return soTrang;
    }

    public void setSoTrang(int soTrang) {
        if (soTrang > 0) {
            this.soTrang = soTrang;
        } else {
            throw new IllegalArgumentException("Số trang phải lớn hơn 0");
        }
    }

    @Override
    public double tinhPhiMuon() {
        return getGiaTien() * 0.05;
    }
}
