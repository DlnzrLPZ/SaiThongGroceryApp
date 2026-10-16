package com.pongsakorn.saithonggroceryapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pongsakorn.saithonggroceryapp.databinding.MainContainerBinding

/**
 * MainNavigation: คลาสหลักที่ควบคุมการเปลี่ยนหน้า (Navigation) ทั้งหมดในแอปพลิเคชัน
 * ทำหน้าที่จัดการ BottomNavigationView และสลับการมองเห็น (Visibility) ของแต่ละ Layout
 */
class MainNavigation : AppCompatActivity() {

    private lateinit var binding: MainContainerBinding
    private lateinit var layouts: List<View>

    private lateinit var homeScreen: Home
    private lateinit var stockScreen: Stock
    private lateinit var productsScreen: Products
    private lateinit var historyScreen: History
    private lateinit var reportScreen: Report

    /** หน้าที่ต้องกลับไปหลังอัปเดตสต็อกเสร็จ (หน้าสต็อก หรือหน้ารายงาน) */
    private var updateStockReturnView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        binding = MainContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        layouts = listOf(
            binding.layoutHome.root,
            binding.layoutProducts.root,
            binding.layoutStock.root,
            binding.layoutAddProduct.root,
            binding.layoutEditProduct.root,
            binding.layoutUpdateStock.root,
            binding.layoutHistory.root,
            binding.layoutReport.root,
        )

        setupScreens()
        setupBottomNavigation()
    }

    private fun setupScreens() {
        historyScreen = History(binding.layoutHistory.root) {
            homeScreen.updateDashboardCounters()
            showLayout(binding.layoutHome.root)
        }

        homeScreen = Home(
            binding.layoutHome.root,
            onSeeAllClick = {
                binding.bottomNavigation.selectedItemId = R.id.navigation_products
            },
            onSeeHistoryClick = {
                historyScreen.refresh()
                showLayout(binding.layoutHistory.root)
            },
        )

        val updateStockScreen = UpdateStock(binding.layoutUpdateStock.root) {
            refreshAll()
            showLayout(updateStockReturnView ?: binding.layoutStock.root)
        }

        val openUpdateStock = { product: Product, returnTo: View ->
            updateStockReturnView = returnTo
            updateStockScreen.setProduct(product)
            showLayout(binding.layoutUpdateStock.root)
        }

        reportScreen = Report(
            binding.layoutReport.root,
            onBackClick = {
                stockScreen.updateCounters()
                showLayout(binding.layoutStock.root)
            },
            onUpdateClick = { product -> openUpdateStock(product, binding.layoutReport.root) },
        )

        stockScreen = Stock(
            binding.layoutStock.root,
            onReportClick = {
                reportScreen.refresh()
                showLayout(binding.layoutReport.root)
            },
            onUpdateClick = { product -> openUpdateStock(product, binding.layoutStock.root) },
        )

        val editProductScreen = EditProduct(binding.layoutEditProduct.root) {
            refreshAll()
            showLayout(binding.layoutProducts.root)
        }

        productsScreen = Products(
            binding.layoutProducts.root,
            onAddClick = {
                showLayout(binding.layoutAddProduct.root)
            },
            onEditClick = { product ->
                editProductScreen.setProduct(product)
                showLayout(binding.layoutEditProduct.root)
            },
            onProductListChanged = { refreshAll() },
        )

        AddProduct(binding.layoutAddProduct.root) {
            refreshAll()
            showLayout(binding.layoutProducts.root)
        }
    }

    /** รีเฟรชทุกหน้าที่แสดงข้อมูลสินค้า/กิจกรรม ให้ตรงกับข้อมูลล่าสุด */
    private fun refreshAll() {
        productsScreen.refreshProductList()
        stockScreen.updateCounters()
        reportScreen.refresh()
        homeScreen.updateDashboardCounters()
        historyScreen.refresh()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    homeScreen.updateDashboardCounters()
                    historyScreen.refresh()
                    showLayout(binding.layoutHome.root)
                    true
                }
                R.id.navigation_products -> {
                    productsScreen.refreshProductList()
                    showLayout(binding.layoutProducts.root)
                    true
                }
                R.id.navigation_stock -> {
                    stockScreen.updateCounters()
                    showLayout(binding.layoutStock.root)
                    true
                }
                else -> false
            }
        }
    }

    private fun showLayout(targetLayout: View) {
        layouts.forEach { 
            it.visibility = if (it == targetLayout) View.VISIBLE else View.GONE 
        }
    }
}
