package com.example.myapplicationktgk.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationktgk.R;
import com.example.myapplicationktgk.model.Document;

import java.util.ArrayList;
import java.util.Locale;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Document> list;
    private final DocumentActionListener listener;

    public DocumentAdapter(Context context, ArrayList<Document> list, DocumentActionListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTen;
        TextView txtLoai;
        TextView txtPhi;
        TextView txtId;
        Button btnEdit;
        Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTen = itemView.findViewById(R.id.txtTen);
            txtLoai = itemView.findViewById(R.id.txtLoai);
            txtPhi = itemView.findViewById(R.id.txtPhi);
            txtId = itemView.findViewById(R.id.txtId);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_document, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Document document = list.get(position);

        holder.txtTen.setText(document.getTenTaiLieu());
        holder.txtLoai.setText(String.format(Locale.ROOT, "Loại: %s", document.getClass().getSimpleName()));
        holder.txtPhi.setText(String.format(Locale.ROOT, "Phí mượn: %,.0f đ", document.tinhPhiMuon()));
        holder.txtId.setText(String.format(Locale.ROOT, "Mã: %s", document.getMaTaiLieu()));

        holder.itemView.setOnClickListener(v -> listener.onOpen(document));

        holder.btnEdit.setOnClickListener(v -> {
            if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                listener.onEdit(document);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (holder.getAdapterPosition() == RecyclerView.NO_POSITION) {
                return;
            }
            new AlertDialog.Builder(context)
                    .setTitle("Xóa tài liệu")
                    .setMessage("Bạn muốn xóa \"" + document.getTenTaiLieu() + "\"?")
                    .setPositiveButton("Xóa", (dialog, which) -> listener.onDelete(document))
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface DocumentActionListener {
        void onOpen(Document document);

        void onEdit(Document document);

        void onDelete(Document document);
    }
}
