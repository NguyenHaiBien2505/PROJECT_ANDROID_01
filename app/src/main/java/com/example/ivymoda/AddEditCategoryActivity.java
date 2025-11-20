package com.example.ivymoda;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ivymoda.Entity.DanhMuc;

public class AddEditCategoryActivity extends AppCompatActivity {

    private EditText edtName;
    private TextView tvTitle;
    private DanhMuc mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_category);

        edtName = findViewById(R.id.edtCategoryName);
        Button btnSave = findViewById(R.id.btnSave);
        tvTitle = findViewById(R.id.tvTitle);

        Intent intent = getIntent();
        if (intent.hasExtra("object_category")) {
            mCategory = (DanhMuc) intent.getSerializableExtra("object_category");
            edtName.setText(mCategory.tenDanhMuc);
            tvTitle.setText("Sửa danh mục");
        } else {
            tvTitle.setText("Thêm danh mục mới");
        }

        btnSave.setOnClickListener(v -> saveCategory());
    }

    private void saveCategory() {
        String name = edtName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mCategory == null) {
            DanhMuc newCat = new DanhMuc();
            newCat.tenDanhMuc = name;
            AppDatabase.getDatabase(this).danhMucDao().insertCategory(newCat);
            Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
        } else {
            mCategory.tenDanhMuc = name;
            AppDatabase.getDatabase(this).danhMucDao().updateCategory(mCategory);
            Toast.makeText(this, "Sửa thành công", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}