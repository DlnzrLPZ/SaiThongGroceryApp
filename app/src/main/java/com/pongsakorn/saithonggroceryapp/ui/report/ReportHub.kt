package com.pongsakorn.saithonggroceryapp.ui.report

import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import com.pongsakorn.saithonggroceryapp.R
import com.pongsakorn.saithonggroceryapp.data.SaleRepository
import com.pongsakorn.saithonggroceryapp.databinding.ReportHubBinding
import com.pongsakorn.saithonggroceryapp.ui.history.History

/**
 * ReportHub: แท็บ "รายงาน" ในแถบเมนูล่าง รวม 2 หน้าไว้ด้วยกัน
 * - ยอดขาย (SalesReport)
 * - ประวัติกิจกรรม (History)
 * ทั้งสองเก็บข้อมูลย้อนหลัง 3 เดือน
 */
class ReportHub(view: View, saleRepo: SaleRepository) {

    private enum class Tab { SALES, HISTORY }

    private val binding = ReportHubBinding.bind(view)
    private val salesScreen = SalesReport(binding.layoutSalesReport.root, saleRepo)
    private val historyScreen = History(binding.layoutHistoryContent.root)
    private var currentTab = Tab.SALES

    init {
        binding.tabHubSales.setOnClickListener { select(Tab.SALES) }
        binding.tabHubHistory.setOnClickListener { select(Tab.HISTORY) }
        refresh()
    }

    fun showSalesTab() = select(Tab.SALES)

    fun showHistoryTab() = select(Tab.HISTORY)

    fun refresh() {
        binding.layoutSalesReport.root.visibility = if (currentTab == Tab.SALES) View.VISIBLE else View.GONE
        binding.layoutHistoryContent.root.visibility = if (currentTab == Tab.HISTORY) View.VISIBLE else View.GONE
        styleTab(binding.tabHubSales, currentTab == Tab.SALES)
        styleTab(binding.tabHubHistory, currentTab == Tab.HISTORY)
        salesScreen.refresh()
        historyScreen.refresh()
    }

    private fun select(tab: Tab) {
        currentTab = tab
        refresh()
    }

    private fun styleTab(tab: TextView, selected: Boolean) {
        val context = binding.root.context
        tab.setBackgroundResource(if (selected) R.drawable.bg_tag_orange else R.drawable.bg_rounded_white)
        tab.setTextColor(context.getColor(if (selected) R.color.text_primary else R.color.text_secondary))
        tab.setTypeface(null, if (selected) Typeface.BOLD else Typeface.NORMAL)
    }
}
