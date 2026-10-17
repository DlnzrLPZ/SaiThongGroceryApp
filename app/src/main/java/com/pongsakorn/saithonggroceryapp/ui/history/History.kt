package com.pongsakorn.saithonggroceryapp.ui.history

import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.pongsakorn.saithonggroceryapp.R
import com.pongsakorn.saithonggroceryapp.data.ActivityData
import com.pongsakorn.saithonggroceryapp.databinding.HistoryBinding
import com.pongsakorn.saithonggroceryapp.model.LogType

/**
 * History: ประวัติกิจกรรม (แท็บย่อยในหน้ารายงาน เก็บย้อนหลัง 3 เดือน)
 * - จัดกลุ่มตามวัน (วันนี้ / เมื่อวาน / วันที่)
 * - กรองตามประเภทได้ (null = ทั้งหมด)
 * ทุกครั้งที่ refresh จะลบ log ที่เก่ากว่า 3 เดือนก่อนแสดงผล
 */
class History(view: View) {

    private val binding = HistoryBinding.bind(view)
    private val adapter = HistoryAdapter()
    private var selectedType: LogType? = null

    private val filters: List<Pair<TextView, LogType?>> = listOf(
        binding.filterHistoryAll to null,
        binding.filterHistorySale to LogType.SALE,
        binding.filterHistoryAdd to LogType.ADD,
        binding.filterHistoryUpdate to LogType.UPDATE,
        binding.filterHistoryDelete to LogType.DELETE,
        binding.filterHistoryAlert to LogType.ALERT,
    )

    init {
        binding.rvHistory.setHasFixedSize(true)
        binding.rvHistory.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rvHistory.adapter = adapter
        filters.forEach { (chip, type) ->
            chip.setOnClickListener {
                selectedType = type
                refresh()
            }
        }
        refresh()
    }

    fun refresh() {
        val context = binding.root.context
        val allLogs = ActivityData.allLogs()
        val logs = allLogs.filter { selectedType == null || it.type == selectedType }

        // จัดกลุ่มตามวัน (logs เรียงใหม่สุดก่อนอยู่แล้ว จึงรักษาลำดับกลุ่มไว้ได้)
        val items = mutableListOf<HistoryItem>()
        logs.groupBy { ActivityData.dayKey(it.timeMillis) }.forEach { (dayKey, dayLogs) ->
            val label = ActivityData.dayLabel(
                dayLogs.first().timeMillis,
                context.getString(R.string.day_today),
                context.getString(R.string.day_yesterday),
            )
            items.add(HistoryItem.DayHeader(dayKey, label))
            dayLogs.forEach { items.add(HistoryItem.Entry(it)) }
        }
        adapter.submitList(items)

        binding.tvHistoryEmpty.setText(
            if (allLogs.isEmpty()) R.string.history_empty else R.string.history_empty_filtered,
        )
        binding.tvHistoryEmpty.visibility = if (logs.isEmpty()) View.VISIBLE else View.GONE

        filters.forEach { (chip, type) -> styleChip(chip, type == selectedType) }
    }

    private fun styleChip(chip: TextView, selected: Boolean) {
        val context = binding.root.context
        chip.setBackgroundResource(if (selected) R.drawable.bg_tag_orange else R.drawable.bg_rounded_white)
        chip.setTextColor(context.getColor(if (selected) R.color.brand_orange else R.color.text_secondary))
        chip.setTypeface(null, if (selected) Typeface.BOLD else Typeface.NORMAL)
    }
}
