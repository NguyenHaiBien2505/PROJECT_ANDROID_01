package com.example.ivymoda;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ivymoda.Entity.TaiKhoan;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTaiKhoanActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword, edtHoTen, edtEmail,
            edtSoDT, edtDiaChi, edtNgaySinh, edtNgayTao, edtVaiTro;
    private RadioGroup rgGioiTinh;
    private RadioButton rbNam, rbNu;

    private Button btnLuu, btnHuy;
    private ImageView btnDate, btnNgay;
    private TextView tvTitle;

    private TaiKhoan existingTaiKhoan = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_tai_khoan);

        mapping();

        int editId = getIntent().getIntExtra("edit_id", -1);

        if (editId != -1) {
            tvTitle.setText("Sửa tài khoản");
            btnLuu.setText("Cập nhật");

            existingTaiKhoan = AppDatabase.getDatabase(this).taiKhoanDao().getById(editId);

            if (existingTaiKhoan != null) {
                edtUsername.setText(existingTaiKhoan.getTenDangNhap());
                edtPassword.setText(existingTaiKhoan.getMatKhau());
                edtHoTen.setText(existingTaiKhoan.getHoTen());
                edtEmail.setText(existingTaiKhoan.getEmail());
                edtSoDT.setText(existingTaiKhoan.getSoDienThoai());
                edtDiaChi.setText(existingTaiKhoan.getDiaChi());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                if (existingTaiKhoan.getNgaySinh() != null) {
                    edtNgaySinh.setText(sdf.format(existingTaiKhoan.getNgaySinh()));
                }
                if (existingTaiKhoan.getNgayTao() != null) {
                    edtNgayTao.setText(sdf.format(existingTaiKhoan.getNgayTao()));
                }
                edtVaiTro.setText(String.valueOf(existingTaiKhoan.getMaVaiTro()));

                // Thiết lập RadioGroup giới tính
                if ("Nam".equalsIgnoreCase(existingTaiKhoan.getGioiTinh())) {
                    rbNam.setChecked(true);
                } else if ("Nữ".equalsIgnoreCase(existingTaiKhoan.getGioiTinh())) {
                    rbNu.setChecked(true);
                }
            }

        } else {
            tvTitle.setText("Thêm tài khoản mới");
            btnLuu.setText("Lưu");
        }

        btnDate.setOnClickListener(v -> showDate());
        btnNgay.setOnClickListener(v -> showDateTao());
        btnLuu.setOnClickListener(v -> saveTaiKhoan());
        btnHuy.setOnClickListener(v -> finish());
    }

    // ============================= DATE PICKER =============================
    private void showDate() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog d = new DatePickerDialog(
                this,
                (DatePicker datePicker, int y, int m, int day) ->
                        edtNgaySinh.setText(day + "/" + (m + 1) + "/" + y),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        );
        d.show();
    }

    private void showDateTao() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog d = new DatePickerDialog(
                this,
                (DatePicker datePicker, int y, int m, int day) ->
                        edtNgayTao.setText(day + "/" + (m + 1) + "/" + y),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        );
        d.show();
    }

    // ============================= SAVE/UPDATE =============================
    private void saveTaiKhoan() {

        String tenDangNhap = edtUsername.getText().toString().trim();
        String matKhau = edtPassword.getText().toString().trim();
        String hoTen = edtHoTen.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String soDT = edtSoDT.getText().toString().trim();
        String diaChi = edtDiaChi.getText().toString().trim();
        String ngaySinhStr = edtNgaySinh.getText().toString().trim();
        String ngayTaoStr = edtNgayTao.getText().toString().trim();
        String vaiTroStr = edtVaiTro.getText().toString().trim();

        // Lấy giới tính từ RadioGroup
        int selectedId = rgGioiTinh.getCheckedRadioButtonId();
        String gioiTinh = null;
        if (selectedId == rbNam.getId()) {
            gioiTinh = "Nam";
        } else if (selectedId == rbNu.getId()) {
            gioiTinh = "Nữ";
        }

        // Kiểm tra bắt buộc nhập đầy đủ
        if (tenDangNhap.isEmpty() || matKhau.isEmpty() || hoTen.isEmpty() ||
                email.isEmpty() || soDT.isEmpty() || diaChi.isEmpty() ||
                ngaySinhStr.isEmpty() || gioiTinh == null || ngayTaoStr.isEmpty() || vaiTroStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ các trường!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra định dạng email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra số điện thoại 10 số
        if (!soDT.matches("\\d{10}")) {
            Toast.makeText(this, "Số điện thoại phải gồm 10 chữ số!", Toast.LENGTH_SHORT).show();
            return;
        }

        int maVaiTro;
        try {
            maVaiTro = Integer.parseInt(vaiTroStr);
        } catch (Exception e) {
            Toast.makeText(this, "Mã vai trò phải là số!", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date ngaySinh = null;
        Date ngayTao = null;
        try {
            ngaySinh = sdf.parse(ngaySinhStr);
            ngayTao = sdf.parse(ngayTaoStr);
        } catch (ParseException e) {
            Toast.makeText(this, "Định dạng ngày không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        AppDatabase db = AppDatabase.getDatabase(this);

        // Kiểm tra username trùng
        TaiKhoan existingByUsername = db.taiKhoanDao().getByTenDangNhap(tenDangNhap);
        if (existingByUsername != null) {
            if (existingTaiKhoan == null || existingByUsername.getMaTaiKhoan() != existingTaiKhoan.getMaTaiKhoan()) {
                Toast.makeText(this, "Username đã tồn tại!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Kiểm tra email trùng
        TaiKhoan existingByEmail = db.taiKhoanDao().getByEmail(email);
        if (existingByEmail != null) {
            if (existingTaiKhoan == null || existingByEmail.getMaTaiKhoan() != existingTaiKhoan.getMaTaiKhoan()) {
                Toast.makeText(this, "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (existingTaiKhoan != null) {
            // Cập nhật tài khoản
            existingTaiKhoan.setTenDangNhap(tenDangNhap);
            existingTaiKhoan.setMatKhau(matKhau);
            existingTaiKhoan.setHoTen(hoTen);
            existingTaiKhoan.setEmail(email);
            existingTaiKhoan.setSoDienThoai(soDT);
            existingTaiKhoan.setDiaChi(diaChi);
            existingTaiKhoan.setNgaySinh(ngaySinh);
            existingTaiKhoan.setGioiTinh(gioiTinh);
            existingTaiKhoan.setNgayTao(ngayTao);
            existingTaiKhoan.setMaVaiTro(maVaiTro);

            db.taiKhoanDao().update(existingTaiKhoan);
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();

        } else {
            // Thêm mới tài khoản
            TaiKhoan tk = new TaiKhoan(
                    0, matKhau, tenDangNhap, email, soDT,
                    ngaySinh, hoTen, diaChi, gioiTinh, maVaiTro, ngayTao
            );
            db.taiKhoanDao().insert(tk);
            Toast.makeText(this, "Thêm tài khoản thành công!", Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_OK);
        finish();
    }

    // ============================= MAPPING =============================
    private void mapping() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtHoTen = findViewById(R.id.edtHoTen);
        edtEmail = findViewById(R.id.edtEmail);
        edtSoDT = findViewById(R.id.edtSoDT);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtNgaySinh = findViewById(R.id.edtNgaySinh);
        edtNgayTao = findViewById(R.id.edtNgayTao);
        edtVaiTro = findViewById(R.id.edtVaiTro);

        rgGioiTinh = findViewById(R.id.rgGioiTinh);
        rbNam = findViewById(R.id.rbNam);
        rbNu = findViewById(R.id.rbNu);

        btnLuu = findViewById(R.id.btn_luu);
        btnHuy = findViewById(R.id.btn_huy);
        btnDate = findViewById(R.id.btnSelectedDate);
        btnNgay = findViewById(R.id.btnDate);

        tvTitle = findViewById(R.id.tvTitle);
    }
}
