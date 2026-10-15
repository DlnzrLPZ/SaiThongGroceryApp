package com.pongsakorn.saithonggroceryapp

import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.pongsakorn.saithonggroceryapp.databinding.EditProductBinding

/**
 * คลาส EditProduct: จัดการ Logic ของหน้าแก้ไขข้อมูลสินค้า
 */
class EditProduct(view: View, private val onBackClick: () -> Unit) {

    private val binding = EditProductBinding.bind(view)

    private val tags = listOf(
        binding.editTagSnack,
        binding.editTagDrink,
        binding.editTagMama,
        binding.editTagMilk,
        binding.editTagFrozen,
    )

    init {
        setupListeners()
    }

    private fun setupListeners() {
        tags.forEach { tag ->
            tag.setOnClickListener { updateTags(tag) }
        }

        binding.btnEditCancel.setOnClickListener {
            onBackClick()
        }
        
        binding.btnUpdateProduct.setOnClickListener {
            val name = binding.etEditProductName.text.toString().trim()
            val priceStr = binding.etEditPrice.text.toString().trim()

            if (name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(binding.root.context, R.string.error_fill_all_fields, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val price = priceStr.toDoubleOrNull()
            if ((price == null) || (price <= 0)) {
                Toast.makeText(binding.root.context, R.string.error_invalid_price, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ActivityData.logAction("แก้ไขข้อมูลสินค้า", name, LogType.UPDATE)

            Toast.makeText(binding.root.context, "อัปเดต $name เรียบร้อยแล้ว", Toast.LENGTH_SHORT).show()
            onBackClick()
        }
    }

    private fun updateTags(selectedTag: TextView) {
        tags.forEach { tag ->
            val isSelected = tag == selectedTag
            tag.setBackgroundResource(if (isSelected) R.drawable.bg_tag_orange else R.drawable.bg_rounded_gray)
            tag.setTextColor(binding.root.context.getColor(if (isSelected) R.color.text_primary else R.color.text_secondary))
            tag.setTypeface(null, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
        }
    }
}
