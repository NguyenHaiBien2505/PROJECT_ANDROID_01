package com.example.ivymoda;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ivymoda.Adapter.DanhMucAdapter;
import com.example.ivymoda.Entity.DanhMuc;

import java.util.ArrayList;
import java.util.List;

public class CategoryManagementActivity extends AppCompatActivity {

    private RecyclerView rcvCategory;
    private DanhMucAdapter categoryAdapter;
    private List<DanhMuc> mListCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_management);

        Button btnAdd = findViewById(R.id.btnAddCategory);
        rcvCategory = findViewById(R.id.rcvCategory);

        mListCategory = new ArrayList<>();
        categoryAdapter = new DanhMucAdapter(mListCategory, new DanhMucAdapter.IClickItemListener() {
            @Override
            public void onUpdateClick(DanhMuc category) {
                clickUpdateCategory(category);
            }

            @Override
            public void onDeleteClick(DanhMuc category) {
                clickDeleteCategory(category);
            }
        });

        rcvCategory.setLayoutManager(new LinearLayoutManager(this));
        rcvCategory.setAdapter(categoryAdapter);

        loadData();

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(CategoryManagementActivity.this, AddEditCategoryActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        mListCategory = AppDatabase.getDatabase(this).danhMucDao().getAllCategories();
        categoryAdapter.setData(mListCategory);
    }

    private void clickUpdateCategory(DanhMuc category) {
        Intent intent = new Intent(CategoryManagementActivity.this, AddEditCategoryActivity.class);
        intent.putExtra("object_category", category);
        startActivity(intent);
    }

    private void clickDeleteCategory(DanhMuc category) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa: " + category.tenDanhMuc + "?")
                .setPositiveButton("Có", (dialog, which) -> {
                    // Pass the category's ID instead of the object
                    AppDatabase.getDatabase(this).danhMucDao().deleteCategory(category.maDanhMuc);
                    loadData();
                })
                .setNegativeButton("Không", null)
                .show();
    }
}