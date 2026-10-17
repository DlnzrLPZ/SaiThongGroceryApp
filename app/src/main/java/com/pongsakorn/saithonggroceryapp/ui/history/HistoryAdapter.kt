package com.pongsakorn.saithonggroceryapp.ui.history

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pongsakorn.saithonggroceryapp.R
import com.pongsakorn.saithonggroceryapp.data.ActivityData
import com.pongsakorn.saithonggroceryapp.databinding.ItemHistoryHeaderBinding
import com.pongsakorn.saithonggroceryapp.databinding.ItemHistoryRowBinding
import com.pongsakorn.saithonggroceryapp.model.ActivityLog
import com.pongsakorn.saithonggroceryapp.model.style

/** รายการในหน้าประวัติ: หัวข้อวัน หรือ log 1 รายการ */
sealed class HistoryItem {
    data class DayHeader(val key: String, val label: String) : HistoryItem()
    data class Entry(val log: ActivityLog) : HistoryItem()
}

/**
 * HistoryAdapter: แสดงประวัติกิจกรรมแบบจัดกลุ่มตามวัน (2 view types: หัวข้อวัน / รายการ)
 */
class HistoryAdapter : ListAdapter<HistoryItem, RecyclerView.ViewHolder>(DiffCallback) {

    class HeaderViewHolder(val binding: ItemHistoryHeaderBinding) : RecyclerView.ViewHolder(binding.root)
    class RowViewHolder(val binding: ItemHistoryRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is HistoryItem.DayHeader -> TYPE_HEADER
        is HistoryItem.Entry -> TYPE_ROW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            HeaderViewHolder(ItemHistoryHeaderBinding.inflate(inflater, parent, false))
        } else {
            RowViewHolder(ItemHistoryRowBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is HistoryItem.DayHeader -> (holder as HeaderViewHolder).binding.tvHistoryDay.text = item.label
            is HistoryItem.Entry -> bindRow(holder as RowViewHolder, item.log)
        }
    }

    private fun bindRow(holder: RowViewHolder, log: ActivityLog) {
        val context = holder.itemView.context
        with(holder.binding) {
            tvHistoryTitle.text = log.title
            tvHistoryDetail.text = log.detail
            tvHistoryTime.text = ActivityData.formatClock(log.timeMillis)

            val style = log.type.style()
            ivHistoryIcon.setImageResource(style.iconRes)
            ivHistoryIcon.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, style.bgRes))
            ivHistoryIcon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, style.tintRes))

            if (log.remark.isBlank()) {
                tvHistoryRemark.visibility = View.GONE
            } else {
                tvHistoryRemark.visibility = View.VISIBLE
                tvHistoryRemark.text = context.getString(R.string.remark_format, log.remark)
            }
        }
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ROW = 1

        val DiffCallback = object : DiffUtil.ItemCallback<HistoryItem>() {
            override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean = when {
                oldItem is HistoryItem.DayHeader && newItem is HistoryItem.DayHeader -> oldItem.key == newItem.key
                oldItem is HistoryItem.Entry && newItem is HistoryItem.Entry -> oldItem.log.id == newItem.log.id
                else -> false
            }

            override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean = oldItem == newItem
        }
    }
}
