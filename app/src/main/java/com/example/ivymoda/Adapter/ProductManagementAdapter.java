package com.example.ivymoda.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ivymoda.Entity.DanhMuc;
import com.example.ivymoda.Entity.SanPham;
import com.example.ivymoda.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductManagementAdapter extends RecyclerView.Adapter<ProductManagementAdapter.ViewHolder> {

    private List<SanPham> productList;
    private List<DanhMuc> categoryList;
    private OnProductActionListener listener;

    public interface OnProductActionListener {
        void onDeleteClick(SanPham product);
        void onEditClick(SanPham product);
        void onInfoClick(SanPham product);
    }

    public ProductManagementAdapter(List<SanPham> productList, List<DanhMuc> categoryList, OnProductActionListener listener) {
        this.productList = productList;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    public void setCategoryList(List<DanhMuc> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPham product = productList.get(position);

        // Hiển thị ảnh từ resource ID
        if (product.getHinhAnh() != 0) {
            holder.ivProductImage.setImageResource(product.getHinhAnh());
        } else {
            holder.ivProductImage.setImageResource(R.drawable.placeholder);
        }

        holder.tvProductName.setText(product.tenSanPham);

        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        holder.tvProductPrice.setText(formatter.format(product.giaBan) + " VNĐ");

        holder.tvProductQuantity.setText("Số lượng: " + product.soLuong);

        String categoryName = getCategoryName(product.maDanhMuc);
        holder.tvProductCategory.setText(categoryName);

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(product);
            }
        });

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(product);
            }
        });

        holder.btnInfo.setOnClickListener(v -> {
            if (listener != null) {
                listener.onInfoClick(product);
            }
        });
    }

    private String getCategoryName(int maDanhMuc) {
        if (categoryList != null) {
            for (DanhMuc dm : categoryList) {
                if (dm.maDanhMuc == maDanhMuc) {
                    return dm.tenDanhMuc;
                }
            }
        }
        return "Chưa phân loại";
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public void updateData(List<SanPham> newList) {
        productList = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvProductQuantity;
        TextView tvProductCategory;
        ImageButton btnDelete;
        ImageButton btnEdit;
        ImageButton btnInfo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvProductCategory = itemView.findViewById(R.id.tvProductCategory);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnInfo = itemView.findViewById(R.id.btnInfo);
        }
    }
}
