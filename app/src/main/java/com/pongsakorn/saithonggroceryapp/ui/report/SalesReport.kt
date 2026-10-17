package com.pongsakorn.saithonggroceryapp.ui.report

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.pongsakorn.saithonggroceryapp.R
import com.pongsakorn.saithonggroceryapp.data.SaleRepository
import com.pongsakorn.saithonggroceryapp.data.SalesPeriod
import com.pongsakorn.saithonggroceryapp.data.SalesStats
import com.pongsakorn.saithonggroceryapp.databinding.ItemTopSellerBinding
import com.pongsakorn.saithonggroceryapp.databinding.SalesReportBinding
import com.pongsakorn.saithonggroceryapp.model.toMoneyText

/**
 * SalesReport: รายงานยอดขาย เลือกช่วงเวลาได้ (วันนี้ / สัปดาห์ / เดือน / 3 เดือน)
 * แสดงยอดรวม จำนวนชิ้น กราฟแท่ง และสินค้าขายดี 3 อันดับ (ยอดขายเก็บย้อนหลัง 3 เดือน)
 */
class SalesReport(
    view: View,
    private val saleRepo: SaleRepository,
) {

    private val binding = SalesReportBinding.bind(view)
    private var period = SalesPeriod.WEEK

    private val chips: List<Pair<TextView, SalesPeriod>> = listOf(
        binding.chipPeriodToday to SalesPeriod.TODAY,
        binding.chipPeriodWeek to SalesPeriod.WEEK,
        binding.chipPeriodMonth to SalesPeriod.MONTH,
        binding.chipPeriodThreeMonths to SalesPeriod.THREE_MONTHS,
    )

    init {
        chips.forEach { (chip, chipPeriod) ->
            chip.setOnClickListener {
                period = chipPeriod
                refresh()
            }
        }
        refresh()
    }

    fun refresh() {
        val context = binding.root.context
        val summary = SalesStats.summarize(saleRepo.getAll(), period)

        binding.tvSalesRevenue.text = context.getString(R.string.price_format, summary.revenue.toMoneyText())
        binding.tvSalesPieces.text = context.getString(R.string.unit_pieces, summary.pieces)

        binding.tvSalesChartTitle.setText(
            when (period) {
                SalesPeriod.TODAY -> R.string.sales_chart_hourly
                SalesPeriod.WEEK, SalesPeriod.MONTH -> R.string.sales_chart_daily
                SalesPeriod.THREE_MONTHS -> R.string.sales_chart_weekly
            },
        )
        binding.chartSales.setData(summary.barValues, summary.barLabels, summary.highlightIndex)

        val hasSales = summary.pieces > 0
        binding.cardSalesChart.visibility = if (hasSales) View.VISIBLE else View.GONE
        binding.cardTopSellers.visibility = if (hasSales) View.VISIBLE else View.GONE
        binding.tvSalesEmpty.visibility = if (hasSales) View.GONE else View.VISIBLE

        binding.llTopSellers.removeAllViews()
        val inflater = LayoutInflater.from(context)
        summary.topSellers.forEach { seller ->
            val row = ItemTopSellerBinding.inflate(inflater, binding.llTopSellers, false)
            row.tvTopRank.text = seller.rank.toString()
            row.tvTopName.text = seller.name
            row.tvTopDetail.text = context.getString(
                R.string.top_seller_detail,
                seller.quantity,
                seller.revenue.toMoneyText(),
            )
            binding.llTopSellers.addView(row.root)
        }

        chips.forEach { (chip, chipPeriod) -> styleChip(chip, chipPeriod == period) }
    }

    private fun styleChip(chip: TextView, selected: Boolean) {
        val context = binding.root.context
        chip.setBackgroundResource(if (selected) R.drawable.bg_tag_orange else R.drawable.bg_rounded_white)
        chip.setTextColor(context.getColor(if (selected) R.color.brand_orange else R.color.text_secondary))
        chip.setTypeface(null, if (selected) Typeface.BOLD else Typeface.NORMAL)
    }
}
