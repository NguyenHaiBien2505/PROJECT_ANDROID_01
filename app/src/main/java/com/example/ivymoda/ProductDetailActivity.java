package com.example.ivymoda;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ivymoda.DAO.DanhMucDao;
import com.example.ivymoda.Entity.DanhMuc;
import com.example.ivymoda.Entity.SanPham;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProductImage;
    private TextView tvMaSanPham;
    private TextView tvTenSanPham;
    private TextView tvMoTa;
    private TextView tvGiaBan;
    private TextView tvSoLuong;
    private TextView tvDanhMuc;
    private TextView tvNgayTao;
    private Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_admin);

        initViews();
        setupClickListeners();
        loadProductData();
    }

    private void initViews() {
        ivProductImage = findViewById(R.id.ivProductImage);
        tvMaSanPham = findViewById(R.id.tvMaSanPham);
        tvTenSanPham = findViewById(R.id.tvTenSanPham);
        tvMoTa = findViewById(R.id.tvMoTa);
        tvGiaBan = findViewById(R.id.tvGiaBan);
        tvSoLuong = findViewById(R.id.tvSoLuong);
        tvDanhMuc = findViewById(R.id.tvDanhMuc);
        tvNgayTao = findViewById(R.id.tvNgayTao);
        btnOK = findViewById(R.id.btnOK);
    }

    private void setupClickListeners() {
        btnOK.setOnClickListener(v -> {
            finish(); // Đóng activity khi bấm OK
        });
    }

    private void loadProductData() {
        // Lấy dữ liệu sản phẩm từ Intent
        if (getIntent() != null && getIntent().hasExtra("PRODUCT")) {
            SanPham product = (SanPham) getIntent().getSerializableExtra("PRODUCT");
            if (product != null) {
                displayProductData(product);
            }
        }
    }

    private void displayProductData(SanPham product) {
        // Hiển thị ảnh sản phẩm
        if (product.getHinhAnh() != 0) {
            ivProductImage.setImageResource(product.getHinhAnh());
        } else {
            ivProductImage.setImageResource(R.drawable.placeholder);
        }

        // Hiển thị thông tin sản phẩm
        tvMaSanPham.setText(String.valueOf(product.getMaSanPham()));
        tvTenSanPham.setText(product.getTenSanPham());
        tvMoTa.setText(product.getMoTa() != null ? product.getMoTa() : "");

        // Format giá tiền
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        tvGiaBan.setText(formatter.format(product.getGiaBan()) + " đ");

        tvSoLuong.setText(String.valueOf(product.getSoLuong()));

        // Lấy tên danh mục từ database
        loadTenDanhMuc(product.getMaDanhMuc());

        // Hiển thị ngày tạo
        if (product.getNgayTao() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            tvNgayTao.setText(dateFormat.format(product.getNgayTao()));
        } else {
            tvNgayTao.setText("Chưa có thông tin");
        }
    }

    private void loadTenDanhMuc(int maDanhMuc) {
        new Thread(() -> {
            DanhMucDao danhMucDao = AppDatabase.getDatabase(this).danhMucDao();
            List<DanhMuc> danhMucList = danhMucDao.getAllCategories();
            String tenDanhMuc = "Chưa phân loại";

            for (DanhMuc dm : danhMucList) {
                if (dm.maDanhMuc == maDanhMuc) {
                    tenDanhMuc = dm.tenDanhMuc;
                    break;
                }
            }

            final String finalTenDanhMuc = tenDanhMuc;
            runOnUiThread(() -> tvDanhMuc.setText(finalTenDanhMuc));
        }).start();
    }
}
