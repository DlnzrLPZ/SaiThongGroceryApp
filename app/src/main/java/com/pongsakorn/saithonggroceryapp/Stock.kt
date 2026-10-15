package com.pongsakorn.saithonggroceryapp

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pongsakorn.saithonggroceryapp.databinding.StockBinding

/**
 * Stock: คลาสจัดการตรรกะหน้าจัดการสต็อกสินค้าด้วย RecyclerView ประสิทธิภาพสูง
 */
class Stock(view: View, private val onUpdateClick: (Product) -> Unit) {
    
    private val binding = StockBinding.bind(view)
    private lateinit var adapter: StockAdapter

    init {
        setupRecyclerView()
        updateCounters()
    }

    private fun setupRecyclerView() {
        adapter = StockAdapter { product ->
            onUpdateClick(product)
        }
        binding.rvStock.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rvStock.adapter = adapter
    }

    fun updateCounters() {
        val products = ProductData.products
        val totalCount = products.size
        val lowStockCount = products.count { ((it.stock in 1..10)) }
        val outOfStockCount = products.count { it.stock == 0 }

        binding.tvTotalVal.text = totalCount.toString()
        binding.tvLowStockVal.text = lowStockCount.toString()
        binding.tvOutOfStockVal.text = outOfStockCount.toString()

        // ใช้ .map { it.copy() } เพื่อสร้างสำเนาอ็อบเจกต์ใหม่ ทำให้ DiffUtil ตรวจพบการเปลี่ยนแปลงของสต็อกได้อย่างถูกต้อง
        adapter.submitList(products.map { it.copy() })
    }
}
