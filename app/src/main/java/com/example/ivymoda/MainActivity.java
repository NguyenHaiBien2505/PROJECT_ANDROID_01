package com.example.ivymoda;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ivymoda.Adapter.SanPhamAdapter;
import com.example.ivymoda.DAO.DanhMucDao;
import com.example.ivymoda.DAO.SanPhamDao;
import com.example.ivymoda.DAO.TaiKhoanDao;
import com.example.ivymoda.DAO.VaiTroDao;
import com.example.ivymoda.Entity.DanhMuc;
import com.example.ivymoda.Entity.SanPham;
import com.example.ivymoda.Entity.TaiKhoan;
import com.example.ivymoda.Entity.VaiTro;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SanPhamAdapter adapter;
    private List<SanPham> sanPhamList = new ArrayList<>();
    private List<SanPham> originalList = new ArrayList<>(); // Danh sách gốc
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getDatabase(this);

        // === RecyclerView: Grid 2 cột ===
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new SanPhamAdapter(sanPhamList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        // === Bottom Navigation ===
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_product);

        // === NÚT BỘ LỌC ===
        TextView tvFilter = findViewById(R.id.tvFilter);
        tvFilter.setOnClickListener(v -> showFilterDialog());

        // Trong onCreate() — sau khi khởi tạo các view
        TextView tvSort = findViewById(R.id.tvSort);
        tvSort.setOnClickListener(v -> showSortDialog());

        // Tải dữ liệu sản phẩm
        loadSanPham();
    }

    // Biến để lưu trạng thái sắp xếp hiện tại
    private String currentSort = "none"; // "low_to_high", "high_to_low", "none"

    private void loadSanPham() {
        DatabaseAsync.getAllSanPham(db.sanPhamDao(), list -> {
            originalList.clear();
            originalList.addAll(list);
            sanPhamList.clear();
            sanPhamList.addAll(list);
            runOnUiThread(() -> adapter.notifyDataSetChanged());
        });
    }

    // ================== BỘ LỌC ==================
    private void showFilterDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filter, null);

        GridLayout gridColors = dialogView.findViewById(R.id.gridColorsFilter);
        LinearLayout layoutSizeNumber = dialogView.findViewById(R.id.layoutSizeNumber); // Cột số
        LinearLayout layoutSizeLetter = dialogView.findViewById(R.id.layoutSizeLetter); // Cột chữ
        Button btnApply = dialogView.findViewById(R.id.btnApplyFilter);
        ImageView ivClose = dialogView.findViewById(R.id.ivClose);

        // Dữ liệu
        String[] colorNames = { "Trắng", "Đen", "Xanh", "Hồng", "Vàng", "Xám", "Đỏ", "Tím", "Nâu", "Cam" };
        int[] colorValues = {
                Color.parseColor("#FEFEFE"), Color.BLACK, Color.parseColor("#4CAF50"),
                Color.parseColor("#FF4081"), Color.parseColor("#FFC107"), Color.parseColor("#9E9E9E"),
                Color.parseColor("#ba0202"), Color.parseColor("#9C27B0"), Color.parseColor("#795548"),
                Color.parseColor("#FF5722")
        };

        String[] sizeNumbers = { "28", "29", "30", "31", "32", "33", "34", "35", "36" };
        String[] sizeLetters = { "XS", "S", "M", "L", "XL", "XXL" };

        Set<String> selectedColors = new HashSet<>();
        Set<String> selectedSizes = new HashSet<>();

        // ==================== TẠO NÚT MÀU (2 hàng tự động) ====================
        for (int i = 0; i < colorNames.length; i++) {
            String name = colorNames[i];
            int value = colorValues[i];
            MaterialButton btn = new MaterialButton(this); // Không cần attr outlined

            if (name == "Trắng") {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setColor(value); // nền trắng
                drawable.setStroke(2, Color.parseColor("#DDDDDD")); // viền xám
                drawable.setCornerRadius(12);
            }

            btn.setBackgroundColor(value);
            btn.setTextColor(getContrastColor(value)); // tự động đen/trắng cho dễ đọc
            btn.setCornerRadius(12);
            btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);

            GridLayout.LayoutParams p = new GridLayout.LayoutParams();
            p.width = 0;
            p.height = GridLayout.LayoutParams.WRAP_CONTENT;
            p.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            p.setMargins(8, 8, 8, 8);
            btn.setLayoutParams(p);

            btn.setOnClickListener(v -> {
                if (selectedColors.contains(name)) {
                    selectedColors.remove(name);
                    btn.setIcon(null);
                } else {
                    selectedColors.add(name);
                    btn.setIconResource(R.drawable.ic_check);
                    if (name == "Trắng") {
                        btn.setIconTintResource(android.R.color.black);
                    } else {
                        btn.setIconTintResource(android.R.color.white);
                    }
                }
            });

            gridColors.addView(btn);
        }

        // ==================== TẠO SIZE SỐ ====================
        for (String size : sizeNumbers) {
            View item = createFilterItem(size,
                    () -> selectedSizes.add(size),
                    () -> selectedSizes.remove(size));
            layoutSizeNumber.addView(item);
        }

        // ==================== TẠO SIZE CHỮ ====================
        for (String size : sizeLetters) {
            View item = createFilterItem(size,
                    () -> selectedSizes.add(size),
                    () -> selectedSizes.remove(size));
            layoutSizeLetter.addView(item);
        }

        // ==================== HIỆN DIALOG ====================
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        ivClose.setOnClickListener(v -> dialog.dismiss());
        btnApply.setOnClickListener(v -> {
            filterProducts(selectedColors, selectedSizes);
            dialog.dismiss();
        });

        dialog.show();
    }

    // Hỗ trợ: tự động chọn màu chữ đen hoặc trắng cho dễ đọc
    private int getContrastColor(int color) {
        double luminance = (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return luminance > 0.5 ? Color.BLACK : Color.WHITE;
    }

    private View createFilterItem(String text, Runnable onSelect, Runnable onDeselect) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setPadding(16, 8, 16, 8);

        AppCompatCheckBox cb = new AppCompatCheckBox(this);
        cb.setText(text);
        cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                onSelect.run();
            else
                onDeselect.run();
        });

        layout.addView(cb);
        return layout;
    }

    private void filterProducts(Set<String> colors, Set<String> sizes) {
        List<SanPham> filtered = new ArrayList<>();

        for (SanPham sp : originalList) {
            boolean matchColor = colors.isEmpty() || containsAny(sp.mauSac, colors);
            boolean matchSize = sizes.isEmpty() || containsAny(sp.size, sizes);
            if (matchColor && matchSize) {
                filtered.add(sp);
            }
        }

        sanPhamList.clear();
        sanPhamList.addAll(filtered);
        adapter.notifyDataSetChanged();

        Toast.makeText(this, "Đã lọc: " + filtered.size() + " sản phẩm", Toast.LENGTH_SHORT).show();
    }

    private boolean containsAny(String source, Set<String> targets) {
        if (source == null)
            return false;
        for (String t : targets) {
            if (source.contains(t))
                return true;
        }
        return false;
    }

    private void showSortDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_sort, null);

        TextView tvLowToHigh = dialogView.findViewById(R.id.tvPriceLowToHigh);
        TextView tvHighToLow = dialogView.findViewById(R.id.tvPriceHighToLow);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        // Đặt dialog hiện từ dưới lên + bo góc
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        }

        tvLowToHigh.setOnClickListener(v -> {
            sortProductsByPrice(true); // thấp → cao
            currentSort = "low_to_high";
            dialog.dismiss();
        });

        tvHighToLow.setOnClickListener(v -> {
            sortProductsByPrice(false); // cao → thấp
            currentSort = "high_to_low";
            dialog.dismiss();
        });

        dialog.show();
    }

    // Hàm sắp xếp sản phẩm theo giá
    private void sortProductsByPrice(boolean lowToHigh) {
        List<SanPham> currentList = new ArrayList<>(sanPhamList); // lấy danh sách hiện tại (đã lọc)

        currentList.sort((sp1, sp2) -> {
            if (lowToHigh) {
                return Double.compare(sp1.giaBan, sp2.giaBan);
            } else {
                return Double.compare(sp2.giaBan, sp1.giaBan);
            }
        });

        sanPhamList.clear();
        sanPhamList.addAll(currentList);
        adapter.notifyDataSetChanged();

        Toast.makeText(this,
                lowToHigh ? "Đã sắp xếp: Giá thấp đến cao" : "Đã sắp xếp: Giá cao đến thấp",
                Toast.LENGTH_SHORT).show();
    }
}