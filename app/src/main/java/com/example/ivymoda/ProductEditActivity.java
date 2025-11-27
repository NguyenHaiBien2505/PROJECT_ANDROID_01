package com.example.ivymoda;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ivymoda.DAO.DanhMucDao;
import com.example.ivymoda.DAO.SanPhamDao;
import com.example.ivymoda.Entity.DanhMuc;
import com.example.ivymoda.Entity.SanPham;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductEditActivity extends AppCompatActivity {

    private ImageView ivProductImage;
    private TextView tvTitle;
    private TextView tvMaSanPhamLabel;
    private TextView tvMaSanPham;
    private EditText etTenSanPham;
    private EditText etMoTa;
    private EditText etGiaBan;
    private EditText etSoLuong;
    private Spinner spinnerDanhMuc;
    private Button btnHuy;
    private Button btnLuu;

    private AppDatabase db;
    private List<DanhMuc> danhMucList;
    private ArrayAdapter<DanhMuc> spinnerAdapter;

    private SanPham currentProduct;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        db = AppDatabase.getDatabase(this);

        initViews();
        setupClickListeners();
        loadDanhMucData();
        loadProductData();
    }

    private void initViews() {
        ivProductImage = findViewById(R.id.ivProductImage);
        tvTitle = findViewById(R.id.tvTitle);
        tvMaSanPhamLabel = findViewById(R.id.tvMaSanPhamLabel);
        tvMaSanPham = findViewById(R.id.tvMaSanPham);
        etTenSanPham = findViewById(R.id.etTenSanPham);
        etMoTa = findViewById(R.id.etMoTa);
        etGiaBan = findViewById(R.id.etGiaBan);
        etSoLuong = findViewById(R.id.etSoLuong);
        spinnerDanhMuc = findViewById(R.id.spinnerDanhMuc);
        btnHuy = findViewById(R.id.btnHuy);
        btnLuu = findViewById(R.id.btnLuu);

        // Vô hiệu hóa các nút chọn ảnh
        findViewById(R.id.btnChangeImage).setVisibility(View.GONE);
        findViewById(R.id.btnTakePhoto).setVisibility(View.GONE);
    }

    private void setupClickListeners() {
        btnHuy.setOnClickListener(v -> finish());
        btnLuu.setOnClickListener(v -> saveProduct());
    }

    private void loadDanhMucData() {
        new Thread(() -> {
            DanhMucDao danhMucDao = db.danhMucDao();
            List<DanhMuc> categories = danhMucDao.getAllCategories();
            runOnUiThread(() -> {
                if (categories != null && !categories.isEmpty()) {
                    danhMucList = new ArrayList<>(categories);
                    setupSpinner();
                    if (isEditMode && currentProduct != null) {
                        displayProductData();
                    }
                }
            });
        }).start();
    }

    private void setupSpinner() {
        spinnerAdapter = new ArrayAdapter<DanhMuc>(this, android.R.layout.simple_spinner_item, danhMucList) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setText(getItem(position).tenDanhMuc);
                return textView;
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setText(getItem(position).tenDanhMuc);
                return textView;
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDanhMuc.setAdapter(spinnerAdapter);
    }

    private void loadProductData() {
        if (getIntent() != null && getIntent().hasExtra("PRODUCT")) {
            isEditMode = true;
            currentProduct = (SanPham) getIntent().getSerializableExtra("PRODUCT");
            tvTitle.setText("CHỈNH SỬA SẢN PHẨM");
        } else {
            isEditMode = false;
            tvTitle.setText("THÊM SẢN PHẨM MỚI");
            tvMaSanPhamLabel.setVisibility(View.GONE);
            tvMaSanPham.setVisibility(View.GONE);
        }
    }

    private void displayProductData() {
        if (currentProduct == null) return;

        tvMaSanPham.setText(String.valueOf(currentProduct.getMaSanPham()));
        etTenSanPham.setText(currentProduct.getTenSanPham());
        etMoTa.setText(currentProduct.getMoTa());

        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.getDefault());
        etGiaBan.setText(formatter.format(currentProduct.getGiaBan()));

        etSoLuong.setText(String.valueOf(currentProduct.getSoLuong()));

        // Hiển thị ảnh từ resource ID
        if (currentProduct.getHinhAnh() != 0) {
            ivProductImage.setImageResource(currentProduct.getHinhAnh());
        } else {
            ivProductImage.setImageResource(R.drawable.placeholder);
        }

        if (danhMucList != null) {
            for (int i = 0; i < danhMucList.size(); i++) {
                if (danhMucList.get(i).getMaDanhMuc() == currentProduct.getMaDanhMuc()) {
                    spinnerDanhMuc.setSelection(i);
                    break;
                }
            }
        }
    }

    private void saveProduct() {
        if (!validateData()) {
            return;
        }

        if (isEditMode) {
            updateProduct();
        } else {
            createNewProduct();
        }
    }

    private boolean validateData() {
        // ... (Giữ nguyên phần validate)
        return true;
    }

    private void updateProduct() {
        if (currentProduct == null) return;

        currentProduct.setTenSanPham(etTenSanPham.getText().toString().trim());
        currentProduct.setMoTa(etMoTa.getText().toString().trim());

        String giaBanStr = etGiaBan.getText().toString().replaceAll("[.,]", "");
        currentProduct.setGiaBan(Double.parseDouble(giaBanStr));

        currentProduct.setSoLuong(Integer.parseInt(etSoLuong.getText().toString().trim()));

        DanhMuc selectedDanhMuc = (DanhMuc) spinnerDanhMuc.getSelectedItem();
        currentProduct.setMaDanhMuc(selectedDanhMuc.maDanhMuc);

        // Vì không chọn ảnh mới, giữ nguyên ảnh cũ hoặc ảnh mặc định
        if (currentProduct.getHinhAnh() == 0) {
            currentProduct.setHinhAnh(R.drawable.placeholder);
        }

        new Thread(() -> {
            SanPhamDao sanPhamDao = db.sanPhamDao();
            sanPhamDao.update(currentProduct);
            runOnUiThread(() -> {
                Toast.makeText(this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }

    private void createNewProduct() {
        SanPham newProduct = new SanPham();
        newProduct.setTenSanPham(etTenSanPham.getText().toString().trim());
        newProduct.setMoTa(etMoTa.getText().toString().trim());

        String giaBanStr = etGiaBan.getText().toString().replaceAll("[.,]", "");
        newProduct.setGiaBan(Double.parseDouble(giaBanStr));

        newProduct.setSoLuong(Integer.parseInt(etSoLuong.getText().toString().trim()));

        DanhMuc selectedDanhMuc = (DanhMuc) spinnerDanhMuc.getSelectedItem();
        newProduct.setMaDanhMuc(selectedDanhMuc.maDanhMuc);

        // Luôn dùng ảnh mặc định khi tạo mới
        newProduct.setHinhAnh(R.drawable.placeholder);

        newProduct.setMauSac("Trắng, Đen");
        newProduct.setSize("S, M, L");
        newProduct.setNgayTao(new Date());

        new Thread(() -> {
            SanPhamDao sanPhamDao = db.sanPhamDao();
            sanPhamDao.insert(newProduct);
            runOnUiThread(() -> {
                Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}
