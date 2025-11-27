package com.example.ivymoda;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ivymoda.Adapter.UserAdapter;
import com.example.ivymoda.DAO.TaiKhoanDao;
import com.example.ivymoda.DAO.VaiTroDao;
import com.example.ivymoda.Entity.TaiKhoan;
import com.example.ivymoda.Entity.TaiKhoanWithRole;
import com.example.ivymoda.Entity.VaiTro;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserManagerActivity extends AppCompatActivity {

    private ListView lvUsers;
    private UserAdapter adapter;
    private ImageButton btnAdd, btnFilter, btnSearch;
    private EditText edtSearch;
    private TextView tvNoResult;


    private TaiKhoanDao taiKhoanDAO;
    private VaiTroDao vaiTroDAO;
    private ActivityResultLauncher activityResultLauncher;

    private List<TaiKhoan> fullTaiKhoanList; // danh sách gốc tài khoản
    private int selectedRoleId = -1; // -1 = tất cả
    private String selectedGender = "Tất cả";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        mapping();

        AppDatabase db = AppDatabase.getDatabase(this);
        vaiTroDAO = db.vaiTroDao();
        taiKhoanDAO = db.taiKhoanDao();

        // ActivityResultLauncher để reload dữ liệu khi thêm/sửa
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadData();
                    }
                }
        );

        registerForContextMenu(lvUsers);

        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(UserManagerActivity.this, AddTaiKhoanActivity.class);
            activityResultLauncher.launch(intent);
        });

        btnFilter.setOnClickListener(v -> showFilterDialog());

        btnSearch.setOnClickListener(v -> applyFilter());

        // Thêm dữ liệu mẫu nếu trống
        if (vaiTroDAO.getAll().isEmpty()) {
            vaiTroDAO.insert(new VaiTro(1,"Admin"));
            vaiTroDAO.insert(new VaiTro(2,"User"));
        }
        if (taiKhoanDAO.getAll().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date ngaySinh = sdf.parse("01/01/2000");
                Date ngayTao = sdf.parse("10/11/2025");

                TaiKhoan tk = new TaiKhoan(0, "123456", "admin", "admin@gmail.com", "0123456789", ngaySinh, "Nguyễn Văn A", "Hà Nội", "Nam", 1, ngayTao);
                taiKhoanDAO.insert(tk);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        loadData();
    }

    private void mapping() {
        lvUsers = findViewById(R.id.lv_users);
        btnAdd = findViewById(R.id.btnAdd);
        btnFilter = findViewById(R.id.btnFilter);
        btnSearch = findViewById(R.id.btnSearch);
        edtSearch = findViewById(R.id.edtSearch);
        tvNoResult = findViewById(R.id.tvNoResult);

    }

    private void loadData() {
        fullTaiKhoanList = taiKhoanDAO.getAll();
        applyFilter();
    }

    private void applyFilter() {
        String searchText = edtSearch.getText().toString().trim().toLowerCase();
        List<TaiKhoanWithRole> filtered = new ArrayList<>();

        for (TaiKhoan tk : fullTaiKhoanList) {
            boolean matchRole = (selectedRoleId == -1 || tk.getMaVaiTro() == selectedRoleId);
            boolean matchGender = selectedGender.equals("Tất cả") || tk.getGioiTinh().equalsIgnoreCase(selectedGender);
            boolean matchSearch = TextUtils.isEmpty(searchText) ||
                    tk.getTenDangNhap().toLowerCase().contains(searchText) ||
                    tk.getHoTen().toLowerCase().contains(searchText);

            if (matchRole && matchGender && matchSearch) {
                VaiTro vt = vaiTroDAO.getById(tk.getMaVaiTro());
                String roleName = vt != null ? vt.getTenVaiTro() : "Chưa xác định";

                filtered.add(new TaiKhoanWithRole(
                        tk.getMaTaiKhoan(),
                        tk.getTenDangNhap(),
                        tk.getHoTen(),
                        tk.getEmail(),
                        roleName
                ));
            }
        }

        // Hiển thị TextView nếu không có kết quả
        if (filtered.isEmpty()) {
            tvNoResult.setVisibility(View.VISIBLE);
        } else {
            tvNoResult.setVisibility(View.GONE);
        }

        adapter = new UserAdapter(this, filtered);
        lvUsers.setAdapter(adapter);
    }


    private void showFilterDialog() {
        String[] options = {"Tất cả", "Lọc theo vai trò", "Lọc theo giới tính"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn phương thức lọc");
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0: // Tất cả
                    selectedRoleId = -1;
                    selectedGender = "Tất cả";
                    applyFilter();
                    break;
                case 1: // Lọc theo vai trò
                    showRoleSelectionDialog();
                    break;
                case 2: // Lọc theo giới tính
                    showGenderSelectionDialog();
                    break;
            }
        });
        builder.show();
    }

    private void showRoleSelectionDialog() {
        List<VaiTro> roles = vaiTroDAO.getAll();
        String[] roleOptions = new String[roles.size() + 1];
        roleOptions[0] = "Tất cả";
        for (int i = 0; i < roles.size(); i++) {
            roleOptions[i + 1] = roles.get(i).getTenVaiTro();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn vai trò");
        builder.setItems(roleOptions, (dialog, which) -> {
            if (which == 0) {
                selectedRoleId = -1;
            } else {
                selectedRoleId = roles.get(which - 1).getMaVaiTro();
            }
            applyFilter();
        });
        builder.show();
    }

    private void showGenderSelectionDialog() {
        String[] genderOptions = {"Tất cả", "Nam", "Nữ"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn giới tính");
        builder.setItems(genderOptions, (dialog, which) -> {
            selectedGender = genderOptions[which];
            applyFilter();
        });
        builder.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.my_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        TaiKhoanWithRole selected = (TaiKhoanWithRole) lvUsers.getItemAtPosition(info.position);

        int mni = item.getItemId();
        if (mni == R.id.update_mni) {
            showUpdateDialog(selected);
        } else if (mni == R.id.delete_mni) {
            showDeleteDialog(selected);
        }
        return super.onContextItemSelected(item);
    }

    private void showDeleteDialog(TaiKhoanWithRole selected) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa tài khoản '" + selected.tenDangNhap + "' không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    TaiKhoan tk = taiKhoanDAO.getById(selected.maTaiKhoan);
                    if (tk != null) {
                        taiKhoanDAO.delete(tk);
                        Toast.makeText(this, "Đã xóa tài khoản", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showUpdateDialog(TaiKhoanWithRole selected) {
        Intent intent = new Intent(UserManagerActivity.this, AddTaiKhoanActivity.class);
        intent.putExtra("edit_id", selected.maTaiKhoan);
        activityResultLauncher.launch(intent);
    }
}
