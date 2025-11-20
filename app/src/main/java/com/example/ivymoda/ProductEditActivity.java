package com.example.ivymoda;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ivymoda.DAO.DanhMucDao;
import com.example.ivymoda.DAO.SanPhamDao;
import com.example.ivymoda.Entity.DanhMuc;
import com.example.ivymoda.Entity.SanPham;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductEditActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    private ImageView ivProductImage;
    private TextView tvTitle;
    private TextView tvMaSanPhamLabel;
    private TextView tvMaSanPham;
    private EditText etTenSanPham;
    private EditText etMoTa;
    private EditText etGiaBan;
    private EditText etSoLuong;
    private Spinner spinnerDanhMuc;
    private Button btnChangeImage;
    private Button btnTakePhoto;
    private Button btnHuy;
    private Button btnLuu;

    private AppDatabase db;
    private List<DanhMuc> danhMucList;
    private ArrayAdapter<DanhMuc> spinnerAdapter;

    private SanPham currentProduct;
    private boolean isEditMode = false;
    private int selectedImageResource = 0;

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
        btnChangeImage = findViewById(R.id.btnChangeImage);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnHuy = findViewById(R.id.btnHuy);
        btnLuu = findViewById(R.id.btnLuu);
    }

    private void setupClickListeners() {
        btnChangeImage.setOnClickListener(v -> {
            openImagePicker();
        });

        btnTakePhoto.setOnClickListener(v -> {
            takePhoto();
        });

        btnHuy.setOnClickListener(v -> {
            finish();
        });

        btnLuu.setOnClickListener(v -> {
            saveProduct();
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "Không thể mở camera", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null) {
                // Xử lý ảnh từ thư viện
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        // Resize ảnh để tránh kích thước quá lớn
                        bitmap = resizeBitmap(bitmap, 800, 800);
                        ivProductImage.setImageBitmap(bitmap);
                        // Note: Với SanPham dùng int hinhAnh (resource ID), cần chọn resource hoặc dùng placeholder
                        selectedImageResource = R.drawable.placeholder;
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi khi chọn ảnh", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                // Xử lý ảnh từ camera
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    if (imageBitmap != null) {
                        // Resize ảnh
                        imageBitmap = resizeBitmap(imageBitmap, 800, 800);
                        // Lưu tạm vào ImageView, sẽ cần chọn resource ID hoặc lưu vào file
                        ivProductImage.setImageBitmap(imageBitmap);
                        // Note: Với SanPham dùng int hinhAnh (resource ID), cần chọn resource hoặc dùng placeholder
                        selectedImageResource = R.drawable.placeholder;
                    }
                }
            }
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width > maxWidth || height > maxHeight) {
            float ratio = Math.min((float) maxWidth / width, (float) maxHeight / height);
            width = Math.round(width * ratio);
            height = Math.round(height * ratio);

            return Bitmap.createScaledBitmap(bitmap, width, height, true);
        }

        return bitmap;
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

            if (currentProduct != null) {
                tvTitle.setText("CHỈNH SỬA SẢN PHẨM");
            }
        } else if (getIntent() != null && getIntent().hasExtra("sanPham")) {
            isEditMode = true;
            currentProduct = (SanPham) getIntent().getSerializableExtra("sanPham");

            if (currentProduct != null) {
                tvTitle.setText("CHỈNH SỬA SẢN PHẨM");
            }
        } else {
            isEditMode = false;
            tvTitle.setText("THÊM SẢN PHẨM MỚI");
            tvMaSanPhamLabel.setVisibility(View.GONE);
            tvMaSanPham.setVisibility(View.GONE);
        }
    }

    private void displayProductData() {
        if (currentProduct == null)
            return;

        tvMaSanPham.setText(String.valueOf(currentProduct.getMaSanPham()));
        etTenSanPham.setText(currentProduct.getTenSanPham());
        etMoTa.setText(currentProduct.getMoTa());

        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.getDefault());
        etGiaBan.setText(formatter.format(currentProduct.getGiaBan()));

        etSoLuong.setText(String.valueOf(currentProduct.getSoLuong()));

        // Hiển thị ảnh từ resource ID
        int hinhAnh = currentProduct.getHinhAnh();
        if (hinhAnh != 0) {
            ivProductImage.setImageResource(hinhAnh);
            selectedImageResource = hinhAnh;
        } else {
            ivProductImage.setImageResource(R.drawable.placeholder);
            selectedImageResource = R.drawable.placeholder;
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
        String tenSanPham = etTenSanPham.getText().toString().trim();
        String giaBanStr = etGiaBan.getText().toString().replaceAll("[.,]", "");
        String soLuongStr = etSoLuong.getText().toString().trim();

        if (tenSanPham.isEmpty()) {
            etTenSanPham.setError("Vui lòng nhập tên sản phẩm");
            return false;
        }

        if (giaBanStr.isEmpty()) {
            etGiaBan.setError("Vui lòng nhập giá bán");
            return false;
        }

        if (soLuongStr.isEmpty()) {
            etSoLuong.setError("Vui lòng nhập số lượng");
            return false;
        }

        DanhMuc selectedDanhMuc = (DanhMuc) spinnerDanhMuc.getSelectedItem();
        if (selectedDanhMuc == null) {
            Toast.makeText(this, "Vui lòng chọn danh mục", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void updateProduct() {
        if (currentProduct == null)
            return;

        currentProduct.setTenSanPham(etTenSanPham.getText().toString().trim());
        currentProduct.setMoTa(etMoTa.getText().toString().trim());

        String giaBanStr = etGiaBan.getText().toString().replaceAll("[.,]", "");
        currentProduct.setGiaBan(Double.parseDouble(giaBanStr));

        currentProduct.setSoLuong(Integer.parseInt(etSoLuong.getText().toString().trim()));

        DanhMuc selectedDanhMuc = (DanhMuc) spinnerDanhMuc.getSelectedItem();
        currentProduct.setMaDanhMuc(selectedDanhMuc.maDanhMuc);

        // Lưu ảnh resource ID (nếu đã chọn ảnh mới)
        if (selectedImageResource != 0) {
            currentProduct.setHinhAnh(selectedImageResource);
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

        // Lưu ảnh resource ID (mặc định placeholder nếu chưa chọn)
        if (selectedImageResource != 0) {
            newProduct.setHinhAnh(selectedImageResource);
        } else {
            newProduct.setHinhAnh(R.drawable.placeholder);
        }
        
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