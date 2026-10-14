package com.patcharawadee.main_project

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.patcharawadee.main_project.databinding.HomeBinding

/**
 * คลาส Home: จัดการ Logic ของหน้าจอหลัก (Dashboard) พร้อม RecyclerView กิจกรรมล่าสุด
 */
class Home(view: View, private val onSeeAllClick: () -> Unit) {
    
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
    }

    fun updateDashboardCounters() {
        val total = ProductData.products.size
        val lowStock = ProductData.products.count { ((it.stock in 0..10)) }
        
        binding.tvHomeTotalVal.text = total.toString()
        binding.tvHomeLowStockVal.text = lowStock.toString()

        activityAdapter.submitList(ActivityData.logs.toList())
    }
}
