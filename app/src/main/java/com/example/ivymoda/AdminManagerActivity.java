package com.example.ivymoda;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminManagerActivity extends AppCompatActivity {

    private TextView tvQuanLySanPham;
    private TextView tvQuanLyDanhMuc;
    private TextView tvQuanLyTaiKhoan;
    private ImageView iconUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manager);

        iconUser = findViewById(R.id.iconUser);
        tvQuanLySanPham = findViewById(R.id.tvQuanLySanPham);
        tvQuanLyDanhMuc = findViewById(R.id.tvQuanLyDanhMuc);
        tvQuanLyTaiKhoan = findViewById(R.id.tvQuanLyTaiKhoan);

        // Xử lý click icon user
        iconUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserPopupMenu(v);
            }
        });

        // Xử lý click Quản lý danh mục
        tvQuanLyDanhMuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryManagement();
            }
        });

        // Xử lý click Quản lý sản phẩm
        tvQuanLySanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductManagement();
            }
        });

        // Xử lý click Quản lý tài khoản
        tvQuanLyTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserManager();
            }
        });
    }

    private void showUserPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.user_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_profile) {
                    Toast.makeText(AdminManagerActivity.this, "Mở thông tin tài khoản", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.menu_update) {
                    Toast.makeText(AdminManagerActivity.this, "Cập nhật thông tin", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.menu_logout) {
                    Toast.makeText(AdminManagerActivity.this, "Đăng xuất", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }

    private void openCategoryManagement() {
        // Mở màn hình quản lý danh mục
        Intent intent = new Intent(AdminManagerActivity.this, CategoryManagementActivity.class);
        startActivity(intent);
    }

    private void openProductManagement() {
        // Mở màn hình quản lý sản phẩm
        Intent intent = new Intent(AdminManagerActivity.this, ProductManagementActivity.class);
        startActivity(intent);
    }

    private void openUserManager() {
        // Mở màn hình quản lý tài khoản
        Intent intent = new Intent(AdminManagerActivity.this, UserManagerActivity.class);
        startActivity(intent);
    }
}