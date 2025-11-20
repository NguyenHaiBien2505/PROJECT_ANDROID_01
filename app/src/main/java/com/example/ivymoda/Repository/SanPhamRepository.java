package com.example.ivymoda.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.ivymoda.AppDatabase;
import com.example.ivymoda.DAO.DanhMucDao;
import com.example.ivymoda.DAO.SanPhamDao;
import com.example.ivymoda.Entity.DanhMuc;
import com.example.ivymoda.Entity.SanPham;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SanPhamRepository {

    private SanPhamDao productDao;
    private DanhMucDao danhMucDao;
    private LiveData<List<SanPham>> allProducts;
    private LiveData<List<DanhMuc>> allCategories;

    private ExecutorService executorService;

    public SanPhamRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        productDao = database.sanPhamDao();
        danhMucDao = database.danhMucDao();
        allProducts = productDao.getAllSanPhams();
        allCategories = (LiveData<List<DanhMuc>>) danhMucDao.getAllCategories();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Product operations
    public void insertProduct(SanPham product) {
        executorService.execute(() -> {
            try {
                long id = productDao.insert(product);
                // Cập nhật mã sản phẩm với ID được tạo tự động
                product.setMaSanPham((int) id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void updateProduct(SanPham product) {
        executorService.execute(() -> {
            try {
                productDao.update(product);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteProduct(SanPham product) {
        executorService.execute(() -> {
            try {
                productDao.delete(product);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteProductById(int maSanPham) {
        executorService.execute(() -> {
            try {
                productDao.deleteById(maSanPham);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LiveData<List<SanPham>> getAllProducts() {
        return allProducts;
    }

    public LiveData<SanPham> getProductById(int maSanPham) {
        return productDao.getSanPhamById(maSanPham);
    }

    // Phương thức tìm kiếm sản phẩm
    public LiveData<List<SanPham>> searchProducts(String query) {
        return productDao.searchSanPhams(query);
    }

    // Category operations
    public void insertCategory(DanhMuc danhMuc) {
        executorService.execute(() -> {
            try {
                danhMucDao.insertCategory(danhMuc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void updateCategory(DanhMuc danhMuc) {
        executorService.execute(() -> {
            try {
                danhMucDao.updateCategory(danhMuc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteCategory(DanhMuc danhMuc) {
        executorService.execute(() -> {
            try {
                danhMucDao.deleteCategory(danhMuc.maDanhMuc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LiveData<List<DanhMuc>> getAllCategories() {
        return allCategories;
    }

    public DanhMuc getCategoryById(int maDanhMuc) {
        return danhMucDao.getCategoryById(maDanhMuc);
    }
}