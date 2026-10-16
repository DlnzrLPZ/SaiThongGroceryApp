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

            val style = log.type.style()
            ivLogIcon.setImageResource(style.iconRes)
            ivLogIcon.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, style.bgRes))
            ivLogIcon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, style.tintRes))
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
