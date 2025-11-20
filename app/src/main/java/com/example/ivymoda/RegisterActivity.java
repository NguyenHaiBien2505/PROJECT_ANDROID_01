package com.example.ivymoda;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ivymoda.Entity.TaiKhoan;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText edtName, edtUser, edtPass, edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtName = findViewById(R.id.edtName);
        edtUser = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPassword);
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView tvBackToLogin = findViewById(R.id.tvBackToLogin);

        btnRegister.setOnClickListener(v -> registerUser());
        tvBackToLogin.setOnClickListener(v -> finish());
    }

    private void registerUser() {
        String name = edtName.getText().toString().trim();
        String username = edtUser.getText().toString().trim();
        String password = edtPass.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        // 1. Kiểm tra bỏ trống
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. --- MỚI: Kiểm tra định dạng Email ---
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
            edtEmail.requestFocus(); // Đưa con trỏ về ô Email
            return;
        }

        // 3. Kiểm tra Username đã tồn tại chưa
        TaiKhoan existingUser = AppDatabase.getDatabase(this).taiKhoanDao().checkUsernameExist(username);
        if (existingUser != null) {
            Toast.makeText(this, "Tên đăng nhập đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. --- MỚI: Kiểm tra Email đã tồn tại chưa ---
        TaiKhoan existingEmail = AppDatabase.getDatabase(this).taiKhoanDao().checkEmailExist(email);
        if (existingEmail != null) {
            Toast.makeText(this, "Email này đã được đăng ký!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 5. Logic tạo Admin (như cũ)
        boolean isAdmin = "admin".equals(username);
        java.util.Date currentDate = new java.util.Date();
        // 6. Lưu vào Database
        TaiKhoan newUser = new TaiKhoan();
        newUser.tenDangNhap = username;
        newUser.matKhau = password;
        newUser.hoTen = name;
        newUser.email = email;
        newUser.soDienThoai = ""; // soDienThoai (Tạm thời để trống)
        newUser.ngaySinh = null; // ngaySinh (Tạm thời để null)
        newUser.diaChi = ""; // diaChi (Tạm thời để trống)
        newUser.gioiTinh = "Nam"; // gioiTinh (Mặc định là Nam)
        newUser.ngayTao = currentDate; // ngayTao (Lấy ngày hôm nay)
        newUser.maVaiTro = isAdmin ? 1 : 2; // maVaiTro (1 cho admin, 2 cho user)

        AppDatabase.getDatabase(this).taiKhoanDao().registerUser(newUser);

        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
        finish();
    }
}