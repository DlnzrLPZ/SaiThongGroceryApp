package com.pongsakorn.saithonggroceryapp

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pongsakorn.saithonggroceryapp.databinding.ItemActivityLogBinding

/**
 * ActivityAdapter: อะแดปเตอร์ประสิทธิภาพสูงใช้ ListAdapter และ DiffUtil สำหรับรายการกิจกรรมล่าสุด
 */
class ActivityAdapter : ListAdapter<ActivityLog, ActivityAdapter.ActivityViewHolder>(DiffCallback) {

    class ActivityViewHolder(val binding: ItemActivityLogBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ItemActivityLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val log = getItem(position)
        val context = holder.itemView.context

        with(holder.binding) {
            tvLogTitle.text = log.title
            tvLogDetail.text = log.detail
            tvLogTime.text = ActivityData.formatTime(log.timeMillis)

            if (log.remark.isBlank()) {
                tvLogRemark.visibility = View.GONE
            } else {
                tvLogRemark.visibility = View.VISIBLE
                tvLogRemark.text = context.getString(R.string.remark_format, log.remark)
            }

            val (bgRes, tintRes, iconRes) = when (log.type) {
                LogType.ADD -> Triple(R.color.light_green, R.color.brand_green, android.R.drawable.ic_input_add)
                LogType.UPDATE -> Triple(R.color.light_blue, R.color.brand_blue, android.R.drawable.ic_menu_edit)
                LogType.DELETE -> Triple(R.color.light_red, R.color.brand_red, android.R.drawable.ic_menu_delete)
                LogType.ALERT -> Triple(R.color.light_orange, R.color.brand_orange, android.R.drawable.ic_dialog_alert)
            }

            ivLogIcon.setImageResource(iconRes)
            ivLogIcon.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, bgRes))
            ivLogIcon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, tintRes))
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ActivityLog>() {
        override fun areItemsTheSame(oldItem: ActivityLog, newItem: ActivityLog): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ActivityLog, newItem: ActivityLog): Boolean {
            return oldItem == newItem
        }
    }
}
