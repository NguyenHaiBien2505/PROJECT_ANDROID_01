// SanPhamAdapter.java
package com.example.ivymoda.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ivymoda.Activity.ProductDetailActivity;
import com.example.ivymoda.Entity.SanPham;
import com.example.ivymoda.R;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {

    private final List<SanPham> list;

    public SanPhamAdapter(List<SanPham> list) {
        this.list = list;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sanpham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int i) {
        SanPham sp = list.get(i);
        h.tvTen.setText(sp.tenSanPham);
        h.tvGia.setText(formatPrice(sp.giaBan));
        h.imgSanPham.setImageResource(sp.hinhAnh != 0 ? sp.hinhAnh : R.drawable.placeholder);
        h.tvNewTag.setVisibility(sp.ngayTao.getTime() > System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000 ? View.VISIBLE : View.GONE);

        // XỬ LÝ NHIỀU MÀU
        h.layoutColors.removeAllViews();
        if (sp.mauSac != null && !sp.mauSac.isEmpty()) {
            String[] colors = sp.mauSac.split(",");
            for (String color : colors) {
                color = color.trim().toLowerCase();
                View dot = new View(h.itemView.getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(24, 24);
                params.setMargins(0, 0, 8, 0);
                dot.setLayoutParams(params);
                dot.setBackgroundResource(R.drawable.bg_color_circle);
                // Gán màu theo tên
                switch (color) {
                    case "trắng":
                        dot.setBackgroundTintList(null);
                        dot.setBackgroundResource(R.drawable.bg_color_white);
                        break;

                    case "đen":
                        dot.setBackgroundTintList(null);
                        dot.setBackgroundResource(R.drawable.bg_color_black);
                        break;

                    case "xanh":
                        dot.setBackgroundTintList(null);
                        dot.setBackgroundResource(R.drawable.bg_color_green);
                        break;

                    case "hồng":
                        dot.setBackgroundTintList(null);
                        dot.setBackgroundResource(R.drawable.bg_color_pink);
                        break;

                    case "vàng":
                        dot.setBackgroundTintList(null);
                        dot.setBackgroundResource(R.drawable.bg_color_yellow);
                        break;

                    case "xám":
                        dot.setBackgroundTintList(null);
                        dot.setBackgroundResource(R.drawable.bg_color_gray);
                        break;

                    case "đỏ":
                        dot.setBackgroundTintList(null);
                        dot.setBackgroundResource(R.drawable.bg_color_red);
                        break;

                    case "tím":
                        dot.setBackgroundTintList(null);
                        dot.setBackgroundResource(R.drawable.bg_color_purple);
                        break;

                    case "nâu":
                        dot.setBackgroundTintList(null);
                        dot.setBackgroundResource(R.drawable.bg_color_brown);
                        break;

                    case "cam":
                        dot.setBackgroundTintList(null);
                        dot.setBackgroundResource(R.drawable.bg_color_orange);
                        break;

                    default:
                        dot.setBackgroundTintList(null);
                        dot.setBackgroundResource(R.drawable.bg_color_gray);
                        break;
                }

                h.layoutColors.addView(dot);
            }
        }
        h.tvSelectSize.setOnClickListener(v -> showSizeSelector(v.getContext(), sp));
        h.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
            intent.putExtra("sanPham", sp); // sp là SanPham hiện tại
            v.getContext().startActivity(intent);
        });
    }

    @Override public int getItemCount() { return list.size(); }

    private String formatPrice(double price) {
        NumberFormat f = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return f.format(price).replace("₫", " đ");
    }

    // Trong SanPhamAdapter.java → class ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSanPham, ivFavorite, ivCart;
        TextView tvTen, tvGia, tvNewTag, tvSelectSize; // THÊM DÒNG NÀY
        LinearLayout layoutColors;

        ViewHolder(@NonNull View v) {
            super(v);
            imgSanPham = v.findViewById(R.id.imgSanPham);
            tvTen = v.findViewById(R.id.tvTenSP);
            tvGia = v.findViewById(R.id.tvGia);
            tvNewTag = v.findViewById(R.id.tvNewTag);
            layoutColors = v.findViewById(R.id.layoutColors);
            ivFavorite = v.findViewById(R.id.ivFavorite);
            ivCart = v.findViewById(R.id.ivCart);
            tvSelectSize = v.findViewById(R.id.tvSelectSize);
        }
    }

    // Trong SanPhamAdapter.java
    private void showSizeSelector(Context context, SanPham sp) {
        if (sp.size == null || sp.size.trim().isEmpty()) {
            Toast.makeText(context, "Không có size", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] sizes = sp.size.split(",");
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = sizes[i].trim();
            if (sizes[i].isEmpty()) continue;
        }

        new AlertDialog.Builder(context)
                .setTitle("Chọn size cho " + sp.tenSanPham)
                .setItems(sizes, (dialog, which) -> {
                    Toast.makeText(context, "Đã chọn size: " + sizes[which], Toast.LENGTH_SHORT).show();
                    // TODO: addToCart(sp, sizes[which]);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private View createColorDot(Context ctx, String colorName) {
        View dot = new View(ctx);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(28, 28);
        params.setMargins(0, 0, 8, 0);
        dot.setLayoutParams(params);
        dot.setBackgroundResource(R.drawable.bg_color_circle);

        int color = Color.GRAY;
        switch (colorName.toLowerCase()) {
            case "trắng": color = Color.WHITE; break;
            case "đen": color = Color.BLACK; break;
            case "xanh": color = Color.parseColor("#4CAF50"); break;
            case "hồng": color = Color.parseColor("#FF4081"); break;
            case "vàng": color = Color.parseColor("#FFC107"); break;
            case "đỏ": color = Color.RED; break;
        }
        dot.getBackground().setTint(color);
        return dot;
    }

    private boolean isNew(Date date) {
        return System.currentTimeMillis() - date.getTime() < 7L * 24 * 60 * 60 * 1000;
    }
}