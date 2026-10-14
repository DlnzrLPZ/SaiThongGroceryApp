package com.patcharawadee.main_project

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.patcharawadee.main_project.databinding.ItemStockBinding

/**
 * StockAdapter: อะแดปเตอร์ประสิทธิภาพสูงใช้ ListAdapter และ DiffUtil สำหรับหน้าจัดการสต็อก
 */
class StockAdapter(
    private val onUpdateClick: (Product) -> Unit,
) : ListAdapter<Product, StockAdapter.StockViewHolder>(DiffCallback) {

    class StockViewHolder(val binding: ItemStockBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = ItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val product = getItem(position)
        val context = holder.itemView.context

        with(holder.binding) {
            tvStockName.text = product.name
            tvStockCategory.text = context.getString(R.string.category_label, product.category)
            tvStockQty.text = context.getString(R.string.stock_remaining, product.stock)

            val iconRes = ProductResources.getIconByCategory(product.category)
            val bgRes = ProductResources.getBackgroundColorByCategory(product.category)
            val tintRes = ProductResources.getTintByCategory(product.category)

            ivStockIcon.setImageResource(iconRes)
            ivStockIcon.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, bgRes))
            ivStockIcon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, tintRes))

            val qtyColorRes = when {
                product.stock == 0 -> R.color.brand_red
                product.stock <= 10 -> R.color.brand_orange
                else -> R.color.brand_green
            }
            tvStockQty.setTextColor(ContextCompat.getColor(context, qtyColorRes))

            pbStockLevel.progress = product.stock.coerceAtMost(100)
            pbStockLevel.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(context, qtyColorRes))

            btnUpdateStockItem.setOnClickListener { onUpdateClick(product) }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}
