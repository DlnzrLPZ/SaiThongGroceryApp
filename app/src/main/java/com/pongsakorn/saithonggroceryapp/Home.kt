package com.pongsakorn.saithonggroceryapp

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pongsakorn.saithonggroceryapp.databinding.HomeBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * คลาส Home: จัดการ Logic ของหน้าจอหลัก (Dashboard) พร้อม RecyclerView กิจกรรมล่าสุด
 */
class Home(
    view: View,
    private val repo: ProductRepository,
    private val onSeeAllClick: () -> Unit,
    private val onSeeHistoryClick: () -> Unit = {},
) {

    private val binding = HomeBinding.bind(view)
    private val activityAdapter = ActivityAdapter()

    init {
        setupRecyclerView()
        setupListeners()
        updateDashboardCounters()
    }

    private fun setupRecyclerView() {
        binding.rvRecentActivities.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rvRecentActivities.adapter = activityAdapter
    }

    private fun setupListeners() {
        binding.btnSeeAllCategories.setOnClickListener {
            onSeeAllClick()
        }
        binding.btnSeeAllHistory.setOnClickListener {
            onSeeHistoryClick()
        }
    }

    fun updateDashboardCounters() {
        val products = repo.getAll()

        binding.tvHomeTotalVal.text = products.size.toString()
        binding.tvHomeLowStockVal.text = products.count { it.isLowStock }.toString()
        binding.tvHomeCatVal.text = products.map { it.category }.distinct().size.toString()

        updateGreeting()
        activityAdapter.submitList(ActivityData.recentLogs(5))
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
