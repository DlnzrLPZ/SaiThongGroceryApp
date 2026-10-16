package com.pongsakorn.saithonggroceryapp

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pongsakorn.saithonggroceryapp.databinding.HistoryBinding

/**
 * History: หน้าประวัติกิจกรรมทั้งหมด (เก็บย้อนหลัง 3 เดือน)
 * ทุกครั้งที่เปิดหน้า (refresh) จะลบ log ที่เก่ากว่า 3 เดือนก่อนแสดงผล
 */
class History(view: View, private val onBackClick: () -> Unit) {

    private val binding = HistoryBinding.bind(view)
    private val adapter = ActivityAdapter()

    init {
        binding.rvHistory.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rvHistory.adapter = adapter
        binding.btnHistoryBack.setOnClickListener { onBackClick() }
        refresh()
    }

    fun refresh() {
        val logs = ActivityData.allLogs()
        adapter.submitList(logs)
        binding.tvHistoryEmpty.visibility = if (logs.isEmpty()) View.VISIBLE else View.GONE
    }
}
