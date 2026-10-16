package com.pongsakorn.saithonggroceryapp

import android.graphics.Typeface
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pongsakorn.saithonggroceryapp.databinding.ReportBinding

/**
 * Report: รายงานสินค้าใกล้หมด (1-10 ชิ้น) และหมดสต็อก (0 ชิ้น)
 * อ่านจากข้อมูลสินค้าปัจจุบัน และกดอัปเดตสต็อกของสินค้าแต่ละรายการได้ทันที
 */
class Report(
    view: View,
    private val repo: ProductRepository,
    private val onBackClick: () -> Unit,
    private val onUpdateClick: (Product) -> Unit,
) {

    private enum class Tab { LOW, OUT }

    private val binding = ReportBinding.bind(view)
    private val adapter = StockAdapter { product -> onUpdateClick(product) }
    private var currentTab = Tab.LOW

    init {
        binding.rvReport.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rvReport.adapter = adapter
        binding.btnReportBack.setOnClickListener { onBackClick() }
        binding.tabReportLow.setOnClickListener { select(Tab.LOW) }
        binding.tabReportOut.setOnClickListener { select(Tab.OUT) }
        refresh()
    }

    private fun select(tab: Tab) {
        currentTab = tab
        refresh()
    }

    fun refresh() {
        val context = binding.root.context
        val products = repo.getAll()
        val lowItems = products.filter { it.isLowStock }.sortedBy { it.stock }
        val outItems = products.filter { it.isOutOfStock }

        binding.tabReportLow.text = context.getString(R.string.report_low_stock_tab, lowItems.size)
        binding.tabReportOut.text = context.getString(R.string.report_out_of_stock_tab, outItems.size)
        styleTab(binding.tabReportLow, currentTab == Tab.LOW)
        styleTab(binding.tabReportOut, currentTab == Tab.OUT)

        val items = if (currentTab == Tab.LOW) lowItems else outItems
        // สำเนาอ็อบเจกต์ เพื่อให้ DiffUtil เห็นการเปลี่ยนแปลงของสต็อก
        adapter.submitList(items.map { it.copy() })

        binding.tvReportEmpty.setText(
            if (currentTab == Tab.LOW) R.string.report_empty_low else R.string.report_empty_out,
        )
        binding.tvReportEmpty.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun styleTab(tab: android.widget.TextView, selected: Boolean) {
        val context = binding.root.context
        tab.setBackgroundResource(if (selected) R.drawable.bg_tag_orange else R.drawable.bg_rounded_white)
        tab.setTextColor(context.getColor(if (selected) R.color.text_primary else R.color.text_secondary))
        tab.setTypeface(null, if (selected) Typeface.BOLD else Typeface.NORMAL)
    }
}
