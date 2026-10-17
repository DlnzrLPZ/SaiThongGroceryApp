package com.pongsakorn.saithonggroceryapp.ui.home

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pongsakorn.saithonggroceryapp.R
import com.pongsakorn.saithonggroceryapp.data.ActivityData
import com.pongsakorn.saithonggroceryapp.data.ProductRepository
import com.pongsakorn.saithonggroceryapp.data.SaleRepository
import com.pongsakorn.saithonggroceryapp.data.SalesPeriod
import com.pongsakorn.saithonggroceryapp.data.SalesStats
import com.pongsakorn.saithonggroceryapp.databinding.HomeBinding
import com.pongsakorn.saithonggroceryapp.model.toMoneyText
import com.pongsakorn.saithonggroceryapp.ui.history.ActivityAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

/**
 * คลาส Home: จัดการ Logic ของหน้าจอหลัก (Dashboard)
 * การ์ดยอดขายวันนี้ (เทียบเมื่อวาน + กราฟ 7 วัน) สถานะสต็อก และกิจกรรมล่าสุด
 */
class Home(
    view: View,
    private val repo: ProductRepository,
    private val saleRepo: SaleRepository,
    private val onSeeSalesClick: () -> Unit,
    private val onSeeHistoryClick: () -> Unit,
) {

    private val binding = HomeBinding.bind(view)
    private val activityAdapter = ActivityAdapter()

    init {
        setupRecyclerView()
        setupChart()
        setupListeners()
        updateDashboardCounters()
    }

    private fun setupRecyclerView() {
        binding.rvRecentActivities.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rvRecentActivities.adapter = activityAdapter
    }

    /** กราฟบนการ์ดสีส้ม: แท่งสีขาวโปร่ง แท่งวันนี้สีขาวเต็ม ไม่แสดงตัวอักษร */
    private fun setupChart() {
        val context = binding.root.context
        binding.chartHomeSales.barColor = context.getColor(R.color.hero_bar)
        binding.chartHomeSales.highlightColor = context.getColor(R.color.white)
        binding.chartHomeSales.showLabels = false
    }

    private fun setupListeners() {
        binding.btnHomeSeeSales.setOnClickListener {
            onSeeSalesClick()
        }
        binding.btnSeeAllHistory.setOnClickListener {
            onSeeHistoryClick()
        }
    }

    fun updateDashboardCounters() {
        val products = repo.getAll()

        binding.tvHomeTotalVal.text = products.size.toString()
        binding.tvHomeLowStockVal.text = products.count { it.isLowStock }.toString()
        binding.tvHomeOutOfStockVal.text = products.count { it.isOutOfStock }.toString()

        updateGreeting()
        updateSales()
        activityAdapter.submitList(ActivityData.recentLogs(5))
    }

    /** ยอดขายวันนี้ เทียบกับเมื่อวาน และกราฟยอดขาย 7 วันล่าสุด */
    private fun updateSales() {
        val context = binding.root.context
        val sales = saleRepo.getAll()
        val today = SalesStats.summarize(sales, SalesPeriod.TODAY)
        val yesterdayRevenue = SalesStats.revenueOnDay(sales, 1)
        val diff = today.revenue - yesterdayRevenue

        binding.tvHomeSalesTotal.text = context.getString(R.string.price_format, today.revenue.toMoneyText())
        binding.tvHomeSalesSub.text = if (today.pieces == 0 && yesterdayRevenue == 0.0) {
            context.getString(R.string.home_sales_none)
        } else {
            val compare = when {
                abs(diff) < 0.005 -> context.getString(R.string.home_sales_same)
                diff > 0 -> context.getString(R.string.home_sales_higher, diff.toMoneyText())
                else -> context.getString(R.string.home_sales_lower, (-diff).toMoneyText())
            }
            context.getString(R.string.home_sales_sub, today.pieces, compare)
        }

        val week = SalesStats.summarize(sales, SalesPeriod.WEEK)
        binding.chartHomeSales.setData(week.barValues, week.barLabels, week.highlightIndex)
    }

    /** ทักทายตามช่วงเวลา และแสดงวันที่ปัจจุบัน (ปี พ.ศ.) */
    private fun updateGreeting() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        binding.tvHomeGreeting.setText(
            when {
                hour < 12 -> R.string.greeting_morning
                hour < 17 -> R.string.greeting_afternoon
                else -> R.string.greeting_evening
            },
        )
        val cal = Calendar.getInstance()
        val yearBE = cal.get(Calendar.YEAR) + 543
        val dayMonth = SimpleDateFormat("EEEEที่ d MMM", Locale.forLanguageTag("th-TH")).format(cal.time)
        binding.tvHomeDate.text = "$dayMonth - $yearBE"
    }
}
