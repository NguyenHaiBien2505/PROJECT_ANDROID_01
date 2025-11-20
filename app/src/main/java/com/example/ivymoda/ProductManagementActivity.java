package com.example.ivymoda;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ivymoda.Adapter.ProductManagementAdapter;
import com.example.ivymoda.DAO.DanhMucDao;
import com.example.ivymoda.DAO.SanPhamDao;
import com.example.ivymoda.Entity.DanhMuc;
import com.example.ivymoda.Entity.SanPham;

import java.util.ArrayList;
import java.util.List;

public class ProductManagementActivity extends AppCompatActivity
        implements ProductManagementAdapter.OnProductActionListener {

    private RecyclerView rvProducts;
    private ProductManagementAdapter productAdapter;
    private List<SanPham> productList;
    private List<DanhMuc> danhMucList;
    private EditText etSearch;
    private ImageButton btnSearch, btnAddProduct;
    private ImageButton btnBack;

    private AppDatabase db;
    private boolean isSearching = false;
    private String currentSearchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);

        db = AppDatabase.getDatabase(this);

        initViews();
        setupRecyclerView();
        setupClickListeners();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void initViews() {
        rvProducts = findViewById(R.id.rvProducts);
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupRecyclerView() {
        productList = new ArrayList<>();
        danhMucList = new ArrayList<>();
        productAdapter = new ProductManagementAdapter(productList, danhMucList, this);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        rvProducts.setAdapter(productAdapter);
    }

    private void setupClickListeners() {
        btnAddProduct.setOnClickListener(v -> {
            addNewProduct();
        });

        btnSearch.setOnClickListener(v -> {
            performSearch();
        });

        btnBack.setOnClickListener(v -> {
            finish(); // Quay lại MainActivity (Dashboard)
        });

        // Xử lý sự kiện khi nhấn Enter trong ô tìm kiếm
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            performSearch();
            return true;
        });
    }

    private void loadData() {
        // Load categories
        new Thread(() -> {
            DanhMucDao danhMucDao = db.danhMucDao();
            List<DanhMuc> categories = danhMucDao.getAllCategories();

            runOnUiThread(() -> {
                danhMucList.clear();
                if (categories != null) {
                    danhMucList.addAll(categories);
                }
                if (productAdapter != null) {
                    productAdapter.setCategoryList(danhMucList);
                }
            });
        }).start();

        // Load products using LiveData
        if (!isSearching) {
            db.sanPhamDao().getAllSanPhams().observe(this, products -> {
                if (products != null) {
                    updateProductList(products);
                }
            });
        }
    }

    private void performSearch() {
        String query = etSearch.getText().toString().trim();
        currentSearchQuery = query;

        if (query.isEmpty()) {
            isSearching = false;
            db.sanPhamDao().getAllSanPhams().observe(this, products -> {
                if (products != null) {
                    updateProductList(products);
                    showToast("Hiển thị tất cả sản phẩm");
                }
            });
        } else {
            isSearching = true;
            db.sanPhamDao().searchSanPhams(query).observe(this, products -> {
                if (products != null) {
                    updateProductList(products);
                    if (products.isEmpty()) {
                        showToast("Không tìm thấy sản phẩm nào với từ khóa: " + query);
                    } else {
                        showToast("Tìm thấy " + products.size() + " sản phẩm");
                    }
                }
            });
        }
    }

    private void updateProductList(List<SanPham> products) {
        productList.clear();
        if (products != null) {
            productList.addAll(products);
        }
        if (productAdapter != null) {
            productAdapter.updateData(productList);
        }
    }

    private void addNewProduct() {
        // Mở màn hình thêm sản phẩm mới
        Intent intent = new Intent(ProductManagementActivity.this, ProductEditActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(SanPham product) {
        showDeleteConfirmationDialog(product);
    }

    @Override
    public void onEditClick(SanPham product) {
        Intent intent = new Intent(ProductManagementActivity.this, ProductEditActivity.class);
        intent.putExtra("PRODUCT", product);
        startActivity(intent);
    }

    @Override
    public void onInfoClick(SanPham product) {
        Intent intent = new Intent(ProductManagementActivity.this, ProductDetailActivity.class);
        intent.putExtra("PRODUCT", product);
        startActivity(intent);
    }

    private void showDeleteConfirmationDialog(SanPham product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm \"" + product.tenSanPham + "\" không?");

        builder.setPositiveButton("Xóa", (dialog, which) -> {
            new Thread(() -> {
                SanPhamDao sanPhamDao = db.sanPhamDao();
                sanPhamDao.delete(product);
                runOnUiThread(() -> {
                    showToast("Đã xóa sản phẩm thành công");
                    loadData();
                });
            }).start();
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        // Tuỳ chỉnh màu sắc cho nút
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(getResources().getColor(android.R.color.darker_gray));
    }

    // Phương thức hiển thị thông báo
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (isSearching && !currentSearchQuery.isEmpty()) {
            // Nếu đang trong chế độ tìm kiếm, bấm back sẽ quay lại hiển thị tất cả
            etSearch.setText("");
            isSearching = false;
            loadData();
            showToast("Hiển thị tất cả sản phẩm");
        } else {
            super.onBackPressed();
        }
    }
}