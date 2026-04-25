package com.example.myapplicationktgk;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationktgk.activity.AddEditActivity;
import com.example.myapplicationktgk.activity.DetailActivity;
import com.example.myapplicationktgk.adapter.DocumentAdapter;
import com.example.myapplicationktgk.model.Book;
import com.example.myapplicationktgk.model.Document;
import com.example.myapplicationktgk.model.Ebook;
import com.example.myapplicationktgk.model.Magazine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TYPE_BOOK = "Book";
    private static final String TYPE_MAGAZINE = "Magazine";
    private static final String TYPE_EBOOK = "Ebook";

    private Button btnAdd;
    private RecyclerView rvDocument;
    private EditText edtSearch;

    private final ArrayList<Document> displayedDocuments = new ArrayList<>();
    private final ArrayList<Document> allDocuments = new ArrayList<>();
    private DocumentAdapter adapter;
    private ActivityResultLauncher<Intent> addEditLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        rvDocument = findViewById(R.id.rvDocument);
        edtSearch = findViewById(R.id.edtSearch);

        addEditLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        saveDocumentFromResult(result.getData());
                    }
                }
        );

        seedDocuments();

        adapter = new DocumentAdapter(this, displayedDocuments, new DocumentAdapter.DocumentActionListener() {
            @Override
            public void onOpen(Document document) {
                openDetail(document);
            }

            @Override
            public void onEdit(Document document) {
                openEdit(document);
            }

            @Override
            public void onDelete(Document document) {
                deleteDocument(document);
            }
        });

        rvDocument.setLayoutManager(new LinearLayoutManager(this));
        rvDocument.setAdapter(adapter);
        refreshDocuments();

        btnAdd.setOnClickListener(v -> addEditLauncher.launch(
                new Intent(MainActivity.this, AddEditActivity.class)
        ));

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                refreshDocuments();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void seedDocuments() {
        allDocuments.add(new Book("B1", "Lập trình Java", 100_000, 220));
        allDocuments.add(new Book("B2", "Android cơ bản", 150_000, 300));
        allDocuments.add(new Magazine("M1", "Công nghệ số", 80_000, 12));
        allDocuments.add(new Ebook("E1", "Thiết kế UI Mobile", 1, 8.5));
    }

    private void refreshDocuments() {
        String keyword = edtSearch.getText().toString().trim().toLowerCase(Locale.ROOT);

        displayedDocuments.clear();
        for (Document document : allDocuments) {
            if (keyword.isEmpty()
                    || document.getTenTaiLieu().toLowerCase(Locale.ROOT).contains(keyword)
                    || document.getMaTaiLieu().toLowerCase(Locale.ROOT).contains(keyword)
                    || document.getClass().getSimpleName().toLowerCase(Locale.ROOT).contains(keyword)) {
                displayedDocuments.add(document);
            }
        }

        Collections.sort(displayedDocuments,
                Comparator.comparingDouble(Document::tinhPhiMuon).reversed());
        adapter.notifyDataSetChanged();
    }

    private void saveDocumentFromResult(Intent data) { // Hàm nhận dữ liệu từ Add sau khi bấm lưu
        String oldId = data.getStringExtra("oldId");
        String type = valueOrDefault(data.getStringExtra("type"), TYPE_BOOK);
        String id = valueOrDefault(data.getStringExtra("id"), createDocumentId(type));
        String name = valueOrDefault(data.getStringExtra("ten"), "Chưa đặt tên");
        double price = data.getDoubleExtra("gia", 1);
        double extra = data.getDoubleExtra("extra", 1);

        if (!id.equals(oldId) && isDuplicateId(id)) {
            Toast.makeText(this, "Mã tài liệu đã tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }

        Document document = createDocument(id, name, price, extra, type);
        int index = findDocumentIndex(oldId);
        if (index >= 0) {
            allDocuments.set(index, document);
        } else {
            allDocuments.add(document);
        }

        refreshDocuments();
    }

    private Document createDocument(String id, String name, double price, double extra, String type) {
        switch (type) {
            case TYPE_MAGAZINE:
                return new Magazine(id, name, price, (int) Math.max(1, Math.round(extra)));
            case TYPE_EBOOK:
                return new Ebook(id, name, 1, extra);
            case TYPE_BOOK:
            default:
                return new Book(id, name, price, (int) Math.max(1, Math.round(extra)));
        }
    }

    private void openDetail(Document document) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("id", document.getMaTaiLieu());
        intent.putExtra("ten", document.getTenTaiLieu());
        intent.putExtra("loai", document.getClass().getSimpleName());
        intent.putExtra("gia", document.getGiaTien());
        intent.putExtra("phi", document.tinhPhiMuon());
        intent.putExtra("extra", getExtraValue(document));
        startActivity(intent);
    }

    private void openEdit(Document document) {
        Intent intent = new Intent(this, AddEditActivity.class);
        intent.putExtra("oldId", document.getMaTaiLieu());
        intent.putExtra("id", document.getMaTaiLieu());
        intent.putExtra("ten", document.getTenTaiLieu());
        intent.putExtra("gia", document.getGiaTien());
        intent.putExtra("extra", getExtraValue(document));
        intent.putExtra("type", document.getClass().getSimpleName());
        addEditLauncher.launch(intent);
    }

    private void deleteDocument(Document document) {
        int index = findDocumentIndex(document.getMaTaiLieu());
        if (index >= 0) {
            allDocuments.remove(index);
            refreshDocuments();
        }
    }

    private int findDocumentIndex(String id) {
        if (id == null) {
            return -1;
        }
        for (int i = 0; i < allDocuments.size(); i++) {
            if (id.equals(allDocuments.get(i).getMaTaiLieu())) {
                return i;
            }
        }
        return -1;
    }

    private boolean isDuplicateId(String id) {
        return findDocumentIndex(id) >= 0;
    }

    private String createDocumentId(String type) {
        String prefix;
        switch (valueOrDefault(type, TYPE_BOOK)) {
            case TYPE_MAGAZINE:
                prefix = "M";
                break;
            case TYPE_EBOOK:
                prefix = "E";
                break;
            case TYPE_BOOK:
            default:
                prefix = "B";
                break;
        }
        return prefix + System.currentTimeMillis();
    }

    private double getExtraValue(Document document) {
        if (document instanceof Book) {
            return ((Book) document).getSoTrang();
        }
        if (document instanceof Magazine) {
            return ((Magazine) document).getSoKyPhatHanh();
        }
        if (document instanceof Ebook) {
            return ((Ebook) document).getDungLuongFile();
        }
        return 1;
    }

    private String valueOrDefault(String value, String defaultValue) {
        return value == null || value.trim().isEmpty() ? defaultValue : value.trim();
    }
}
