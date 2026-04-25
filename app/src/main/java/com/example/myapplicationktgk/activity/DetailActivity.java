package com.example.myapplicationktgk.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationktgk.R;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private TextView txtTen;
    private TextView txtLoai;
    private TextView txtGia;
    private TextView txtPhi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        txtTen = findViewById(R.id.txtTen);
        txtLoai = findViewById(R.id.txtLoai);
        txtGia = findViewById(R.id.txtGia);
        txtPhi = findViewById(R.id.txtPhi);

        String id = getIntent().getStringExtra("id"); // nhận dữ liệu từ intent bên Main sang
        String ten = getIntent().getStringExtra("ten");
        String loai = getIntent().getStringExtra("loai");
        double gia = getIntent().getDoubleExtra("gia", 0);
        double phi = getIntent().getDoubleExtra("phi", 0);
        double extra = getIntent().getDoubleExtra("extra", 0);

        txtTen.setText(String.format(Locale.ROOT, "%s - %s", id, ten));
        txtLoai.setText(String.format(Locale.ROOT, "Loại tài liệu: %s", loai));
        if ("Ebook".equals(loai)) {
            txtGia.setText(String.format(Locale.ROOT, "Dung lượng file: %.1f MB", extra));
        } else {
            txtGia.setText(String.format(Locale.ROOT, "Giá tiền: %,.0f đ", gia));
        }
        txtPhi.setText(String.format(Locale.ROOT, "Phí mượn: %,.0f đ", phi));
    }
}
