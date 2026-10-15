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
        )

        setupScreens()
        setupBottomNavigation()
    }

    private fun setupScreens() {
        homeScreen = Home(binding.layoutHome.root) {
            binding.bottomNavigation.selectedItemId = R.id.navigation_products
        }

        val updateStockScreen = UpdateStock(binding.layoutUpdateStock.root) {
            productsScreen.refreshProductList()
            stockScreen.updateCounters()
            homeScreen.updateDashboardCounters()
            showLayout(binding.layoutStock.root)
        }
        
        stockScreen = Stock(binding.layoutStock.root) { product ->
            updateStockScreen.setProduct(product)
            showLayout(binding.layoutUpdateStock.root)
        }
        
        productsScreen = Products(
            binding.layoutProducts.root, 
            onAddClick = {
                showLayout(binding.layoutAddProduct.root)
            },
            onEditClick = {
                showLayout(binding.layoutEditProduct.root)
            },
            onProductListChanged = {
                stockScreen.updateCounters()
                homeScreen.updateDashboardCounters()
            },
        )

        AddProduct(binding.layoutAddProduct.root) {
            productsScreen.refreshProductList()
            stockScreen.updateCounters()
            homeScreen.updateDashboardCounters()
            showLayout(binding.layoutProducts.root)
        }

        EditProduct(binding.layoutEditProduct.root) {
            productsScreen.refreshProductList()
            stockScreen.updateCounters()
            homeScreen.updateDashboardCounters()
            showLayout(binding.layoutProducts.root)
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    homeScreen.updateDashboardCounters()
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
