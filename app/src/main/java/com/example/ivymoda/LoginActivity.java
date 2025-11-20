package com.example.ivymoda;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ivymoda.Entity.TaiKhoan;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText edtUser, edtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUser = findViewById(R.id.edtUsername);
        edtPass = findViewById(R.id.edtPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvGoToRegister = findViewById(R.id.tvGoToRegister);

        btnLogin.setOnClickListener(v -> handleLogin());

        tvGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String username = edtUser.getText().toString().trim();
        String password = edtPass.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        TaiKhoan user = AppDatabase.getDatabase(this).taiKhoanDao().checkLogin(username, password);

        if (user != null) {
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            
            Intent intent;
            // Kiểm tra vai trò: maVaiTro = 1 là Admin, 2 là User
            if (user.maVaiTro == 1) {
                // Admin: chuyển sang trang quản lý
                intent = new Intent(LoginActivity.this, AdminManagerActivity.class);
            } else {
                // User: chuyển sang trang chủ xem sản phẩm
                intent = new Intent(LoginActivity.this, MainActivity.class);
            }
            
            intent.putExtra("user", user); // Truyền thông tin user nếu cần
            startActivity(intent);
            finish(); // Đóng LoginActivity để không quay lại được
        } else {
            Toast.makeText(this, "Sai tên đăng nhập hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
        }
    }
}