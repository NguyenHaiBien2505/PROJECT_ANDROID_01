package com.example.ivymoda.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ivymoda.Entity.SanPham;
import com.example.ivymoda.R;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private SanPham sanPham;
    private String selectedColor = "";
    private String selectedSize = "";
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Nhận sản phẩm từ Intent
        sanPham = (SanPham) getIntent().getSerializableExtra("sanPham");
        if (sanPham == null) {
            Toast.makeText(this, "Không tải được sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        loadData(); // Bây giờ đã có hàm này rồi
    }

    private void initViews() {
        // Không cần khai báo lại ở đây nữa, vì loadData() sẽ làm hết
    }

    // HÀM CHÍNH – TẢI TOÀN BỘ DỮ LIỆU VÀO GIAO DIỆN
    private void loadData() {
        // Ảnh + Tên + Giá
        ImageView img = findViewById(R.id.imgProductDetail);
        TextView tvTen = findViewById(R.id.tvTenSPDetail);
        TextView tvGia = findViewById(R.id.tvGiaDetail);

        img.setImageResource(sanPham.hinhAnh != 0 ? sanPham.hinhAnh : R.drawable.placeholder);
        tvTen.setText(sanPham.tenSanPham.toUpperCase(Locale.getDefault()));
        tvGia.setText(formatPrice(sanPham.giaBan));

        // Tải màu sắc
        loadColors();

        // Tải size
        loadSizes();

        // Thiết lập số lượng +/-
        setupQuantityButtons();

        // Nút MUA NGAY
        setupMuaNgayButton();

        // Yêu thích
        setupFavoriteButton();
    }

    private void loadColors() {
        LinearLayout layoutColors = findViewById(R.id.layoutColors);
        layoutColors.removeAllViews();

        // Giả sử bạn lưu màu trong field mauSac kiểu: "Trắng, Đen, Nâu"
        String[] colors = sanPham.mauSac != null ? sanPham.mauSac.split(",\\s*") : new String[]{"Trắng", "Đen"};

        for (String colorName : colors) {
            String color = colorName.trim();
            View colorView = createColorButton(color);
            layoutColors.addView(colorView);
        }
    }

    private void loadSizes() {
        LinearLayout layoutSizes = findViewById(R.id.layoutSizes);
        layoutSizes.removeAllViews();

        String[] sizes = sanPham.size != null ? sanPham.size.split(",\\s*") : new String[]{"S", "M", "L", "XL"};

        for (String size : sizes) {
            View sizeView = createSizeButton(size.trim());
            layoutSizes.addView(sizeView);
        }
    }

    private View createColorButton(String colorName) {
        FrameLayout container = new FrameLayout(this);
        int size = (int) (60 * getResources().getDisplayMetrics().density);
        container.setLayoutParams(new LinearLayout.LayoutParams(size + 32, size + 32));
        container.setPadding(16, 16, 16, 16);

        View circle = new View(this);
        circle.setLayoutParams(new FrameLayout.LayoutParams(size, size));

        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.OVAL);
        int colorValue = getColorValue(colorName);
        bg.setColor(colorValue);
        bg.setStroke(3, Color.parseColor("#999999"));
        circle.setBackground(bg);

        ImageView tick = new ImageView(this);
        tick.setImageResource(R.drawable.ic_check_white_24);
        tick.setVisibility(View.GONE);
        tick.setId(View.generateViewId()); // Dùng cách này → không cần ids.xml
        FrameLayout.LayoutParams tickParams = new FrameLayout.LayoutParams(size / 2, size / 2);
        tickParams.gravity = Gravity.CENTER;
        tick.setLayoutParams(tickParams);

        container.addView(circle);
        container.addView(tick);

        container.setOnClickListener(v -> {
            selectedColor = colorName;

            LinearLayout parent = findViewById(R.id.layoutColors);
            for (int i = 0; i < parent.getChildCount(); i++) {
                View item = parent.getChildAt(i);
                ImageView otherTick = item.findViewWithTag("tick_tag"); // dùng tag thay vì id
                if (otherTick != null) otherTick.setVisibility(View.GONE);
            }

            tick.setTag("tick_tag"); // đánh dấu đây là tick hiện tại
            tick.setVisibility(View.VISIBLE);
        });

        return container;
    }

    private View createSizeButton(String size) {
        TextView btn = new TextView(this);
        btn.setText(size);
        btn.setTextColor(Color.BLACK);
        btn.setTextSize(16);
        btn.setGravity(Gravity.CENTER);
        btn.setPadding(40, 24, 40, 24);
        btn.setBackgroundResource(R.drawable.bg_size_unselected);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 140);
        params.setMarginEnd(20);
        btn.setLayoutParams(params);

        btn.setOnClickListener(v -> {
            selectedSize = size;
            LinearLayout parent = (LinearLayout) v.getParent();
            for (int i = 0; i < parent.getChildCount(); i++) {
                parent.getChildAt(i).setBackgroundResource(R.drawable.bg_size_unselected);
            }
            v.setBackgroundResource(R.drawable.bg_size_selected);
            ((TextView) v).setTextColor(Color.WHITE);
        });

        return btn;
    }

    private void setupQuantityButtons() {
        TextView tvQty = findViewById(R.id.tvQuantity);
        findViewById(R.id.btnMinus).setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQty.setText(String.valueOf(quantity));
            }
        });
        findViewById(R.id.btnPlus).setOnClickListener(v -> {
            quantity++;
            tvQty.setText(String.valueOf(quantity));
        });
    }

    private void setupMuaNgayButton() {
        findViewById(R.id.btnMuaNgay).setOnClickListener(v -> {
            if (selectedSize.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn kích thước", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedColor.isEmpty()) selectedColor = "Mặc định";
            Toast.makeText(this,
                    "Đã thêm vào giỏ hàng!\n" +
                            sanPham.tenSanPham + "\n" +
                            "Màu: " + selectedColor + " | Size: " + selectedSize + " | SL: " + quantity,
                    Toast.LENGTH_LONG).show();
        });
    }

    private void setupFavoriteButton() {
        ImageView ivFav = findViewById(R.id.ivFavorite);
        ivFav.setOnClickListener(v -> {
            if (ivFav.getColorFilter() == null) {
                ivFav.setColorFilter(Color.RED);
                Toast.makeText(this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
            } else {
                ivFav.clearColorFilter();
                Toast.makeText(this, "Đã bỏ yêu thích", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper: chuyển tên màu → mã màu
    private int getColorValue(String name) {
        switch (name.toLowerCase()) {
            case "trắng":
                return Color.parseColor("#FFFFFF");

            case "đen":
                return Color.parseColor("#000000");

            case "xanh":
                return Color.parseColor("#4CAF50");

            case "hồng":
                return Color.parseColor("#FF4081");

            case "vàng":
                return Color.parseColor("#FFC107");

            case "xám":
                return Color.parseColor("#9E9E9E");

            case "đỏ":
                return Color.parseColor("#F44336");

            case "tím":
                return Color.parseColor("#9C27B0");

            case "nâu":
                return Color.parseColor("#795548");

            case "cam":
                return Color.parseColor("#FF5722");

            default:
                return Color.parseColor("#DDDDDD"); // màu mặc định
        }

    }

    private String formatPrice(double price) {
        NumberFormat fmt = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        return fmt.format(price) + "đ";
    }
}