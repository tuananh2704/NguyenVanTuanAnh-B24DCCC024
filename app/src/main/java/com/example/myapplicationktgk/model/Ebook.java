package com.example.myapplicationktgk.model;

public class Ebook extends Document {
    private double dungLuongFile;

    public Ebook(String ma, String ten, double gia, double dungLuong) {
        super(ma, ten, gia);
        setDungLuongFile(dungLuong);
    }

    public double getDungLuongFile() {
        return dungLuongFile;
    }

    public void setDungLuongFile(double dungLuongFile) {
        this.dungLuongFile = dungLuongFile > 0 ? dungLuongFile : 1;
    }

    @Override
    public double tinhPhiMuon()
    {
        return dungLuongFile * 1000;
    }
}
