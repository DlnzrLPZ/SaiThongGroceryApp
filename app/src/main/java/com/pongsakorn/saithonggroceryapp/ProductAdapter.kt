package com.pongsakorn.saithonggroceryapp

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pongsakorn.saithonggroceryapp.databinding.ItemProductBinding

/**
 * ProductAdapter: อะแดปเตอร์ประสิทธิภาพสูงใช้ ListAdapter และ DiffUtil ในการคำนวณส่วนต่างรายการสินค้า
 */
class ProductAdapter(
    private val onEditClick: (Product) -> Unit,
    private val onDeleteClick: (Product, View) -> Unit,
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(DiffCallback) {

    class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        val context = holder.itemView.context

        with(holder.binding) {
            tvProductName.text = product.name
            tvProductCategory.text = product.category
            tvProductPrice.text = context.getString(R.string.price_format, product.price.toPriceText())

            val iconRes = ProductResources.getIconByCategory(product.category)
            val bgRes = ProductResources.getBackgroundColorByCategory(product.category)
            val tintRes = ProductResources.getTintByCategory(product.category)

            imgProductIcon.setImageResource(iconRes)
            imgProductIcon.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, bgRes))
            imgProductIcon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, tintRes))

            itemDivider.visibility = if (position == (itemCount - 1)) View.GONE else View.VISIBLE

            btnEdit.setOnClickListener { onEditClick(product) }
            btnDelete.setOnClickListener { onDeleteClick(product, holder.itemView) }
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
