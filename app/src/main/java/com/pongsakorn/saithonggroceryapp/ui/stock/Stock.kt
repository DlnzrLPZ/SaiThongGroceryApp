package com.pongsakorn.saithonggroceryapp.ui.stock

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pongsakorn.saithonggroceryapp.data.ProductRepository
import com.pongsakorn.saithonggroceryapp.databinding.StockBinding
import com.pongsakorn.saithonggroceryapp.model.Product

/**
 * Stock: คลาสจัดการตรรกะหน้าจัดการสต็อกสินค้าด้วย RecyclerView ประสิทธิภาพสูง
 */
class Stock(
    view: View,
    private val repo: ProductRepository,
    private val onReportClick: (showOutTab: Boolean) -> Unit = {},
    private val onUpdateClick: (Product) -> Unit,
    private val onSellClick: (Product) -> Unit,
) {

    private val binding = StockBinding.bind(view)
    private lateinit var adapter: StockAdapter

    init {
        binding.btnOpenReport.setOnClickListener { onReportClick(false) }
        binding.cardSummaryLow.setOnClickListener { onReportClick(false) }
        binding.cardSummaryOut.setOnClickListener { onReportClick(true) }
        setupRecyclerView()
        updateCounters()
    }

    private fun setupRecyclerView() {
        adapter = StockAdapter(onUpdateClick = onUpdateClick, onSellClick = onSellClick)
        binding.rvStock.setHasFixedSize(true)
        binding.rvStock.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rvStock.adapter = adapter
    }

    fun updateCounters() {
        val products = repo.getAll()
        val totalCount = products.size
        val lowStockCount = products.count { it.isLowStock }
        val outOfStockCount = products.count { it.isOutOfStock }

        binding.tvTotalVal.text = totalCount.toString()
        binding.tvLowStockVal.text = lowStockCount.toString()
        binding.tvOutOfStockVal.text = outOfStockCount.toString()

        binding.tvStockEmpty.visibility = if (products.isEmpty()) View.VISIBLE else View.GONE

        // ใช้ .map { it.copy() } เพื่อสร้างสำเนาอ็อบเจกต์ใหม่ ทำให้ DiffUtil ตรวจพบการเปลี่ยนแปลงของสต็อกได้อย่างถูกต้อง
        adapter.submitList(products.map { it.copy() })
    }
}
