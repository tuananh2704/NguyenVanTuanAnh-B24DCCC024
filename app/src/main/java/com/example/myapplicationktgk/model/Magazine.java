package com.example.myapplicationktgk.model;

public class Magazine extends Document {
    private int soKyPhatHanh;

    public Magazine(String ma, String ten, double gia, int soKy) {
        super(ma, ten, gia);
        setSoKyPhatHanh(soKy);
    }

    public int getSoKyPhatHanh() {
        return soKyPhatHanh;
    }

    public void setSoKyPhatHanh(int soKyPhatHanh) {
        if (soKyPhatHanh > 0) {
            this.soKyPhatHanh = soKyPhatHanh;
        } else {
            throw new IllegalArgumentException("Số kỳ phát hành phải lớn hơn 0");
        }
    }

    @Override
    public double tinhPhiMuon() {
        return getGiaTien() * 0.03;
    }
}
