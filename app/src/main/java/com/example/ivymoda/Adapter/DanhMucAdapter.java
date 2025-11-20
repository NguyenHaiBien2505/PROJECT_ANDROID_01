package com.example.ivymoda.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ivymoda.Entity.DanhMuc;
import com.example.ivymoda.R;

import java.util.List;

public class DanhMucAdapter extends RecyclerView.Adapter<DanhMucAdapter.CategoryViewHolder> {

    private List<DanhMuc> mList;
    private IClickItemListener mListener;

    public interface IClickItemListener {
        void onUpdateClick(DanhMuc category);
        void onDeleteClick(DanhMuc category);
    }

    public DanhMucAdapter(List<DanhMuc> list, IClickItemListener listener) {
        this.mList = list;
        this.mListener = listener;
    }

    public void setData(List<DanhMuc> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        DanhMuc category = mList.get(position);
        if (category == null) return;

        holder.tvName.setText(category.tenDanhMuc);
        holder.tvId.setText("ID: " + category.maDanhMuc);
        holder.imgEdit.setOnClickListener(v -> mListener.onUpdateClick(category));
        holder.imgDelete.setOnClickListener(v -> mListener.onDeleteClick(category));
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvId;
        ImageView imgEdit, imgDelete;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCategoryName);
            tvId = itemView.findViewById(R.id.tvCategoryId);
            imgEdit = itemView.findViewById(R.id.btnEdit);
            imgDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}