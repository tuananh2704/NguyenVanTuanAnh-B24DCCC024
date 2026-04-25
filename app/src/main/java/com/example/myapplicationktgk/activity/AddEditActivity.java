package com.example.myapplicationktgk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationktgk.R;
import com.google.android.material.textfield.TextInputLayout;

public class AddEditActivity extends AppCompatActivity {

    private EditText edtMa;
    private EditText edtTen;
    private EditText edtGia;
    private EditText edtExtra;
    private TextInputLayout layoutGia;
    private Spinner spLoai;
    private String oldId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        edtMa = findViewById(R.id.edtMa);
        edtTen = findViewById(R.id.edtTen);
        edtGia = findViewById(R.id.edtGia);
        edtExtra = findViewById(R.id.edtExtra);
        layoutGia = findViewById(R.id.layoutGia);
        spLoai = findViewById(R.id.spLoai);
        Button btnSave = findViewById(R.id.btnSave);

        String[] loai = {"Book", "Magazine", "Ebook"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                loai
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLoai.setAdapter(spinnerAdapter);

        spLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                updateFieldsForType(spLoai.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        loadEditData();
        btnSave.setOnClickListener(v -> save());
    }

    private void updateFieldsForType(String type) {
        layoutGia.setVisibility(type.equals("Ebook") ? View.GONE : View.VISIBLE);

        if (type.equals("Book")) {
            edtExtra.setHint("Số trang");
        } else if (type.equals("Magazine")) {
            edtExtra.setHint("Số kỳ phát hành");
        } else {
            edtExtra.setHint("Dung lượng file (MB)");
        }
    }

    private void loadEditData() {
        Intent intent = getIntent();
        oldId = intent.getStringExtra("oldId");

        if (intent.hasExtra("id")) {
            edtMa.setText(intent.getStringExtra("id"));
        }

        if (intent.hasExtra("ten")) {
            edtTen.setText(intent.getStringExtra("ten"));
            edtGia.setText(String.valueOf(intent.getDoubleExtra("gia", 1)));
            edtExtra.setText(String.valueOf(intent.getDoubleExtra("extra", 1)));

            String type = intent.getStringExtra("type");
            if ("Magazine".equals(type)) {
                spLoai.setSelection(1);
            } else if ("Ebook".equals(type)) {
                spLoai.setSelection(2);
            } else {
                spLoai.setSelection(0);
            }
            updateFieldsForType(type);
        }
    }

    private void save() {
        String id = edtMa.getText().toString().trim();
        String name = edtTen.getText().toString().trim();
        String type = spLoai.getSelectedItem().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên tài liệu", Toast.LENGTH_SHORT).show();
            return;
        }

        double extra = parsePositiveDouble(edtExtra, 1);
        double price;
        if (type.equals("Ebook")) {
            price = 1;
        } else {
            String priceText = edtGia.getText().toString().trim();
            if (priceText.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập giá tiền", Toast.LENGTH_SHORT).show();
                return;
            }

            price = parsePositiveDouble(edtGia, -1);
            if (price <= 0) {
                Toast.makeText(this, "Giá tiền phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Intent data = new Intent();  // intent dùng để gửi dữ liệu từ add về main
        data.putExtra("oldId", oldId);
        data.putExtra("id", id);
        data.putExtra("ten", name);
        data.putExtra("gia", price);
        data.putExtra("extra", extra);
        data.putExtra("type", type);

        setResult(RESULT_OK, data);
        finish();
    }

    private double parsePositiveDouble(EditText editText, double fallback) {
        try {
            double value = Double.parseDouble(editText.getText().toString().trim());
            return value > 0 ? value : fallback;
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }
}
