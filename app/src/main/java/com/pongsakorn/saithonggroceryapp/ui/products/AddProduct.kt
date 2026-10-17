package com.pongsakorn.saithonggroceryapp.ui.products

import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.pongsakorn.saithonggroceryapp.R
import com.pongsakorn.saithonggroceryapp.data.ActivityData
import com.pongsakorn.saithonggroceryapp.data.ProductRepository
import com.pongsakorn.saithonggroceryapp.databinding.AddProductBinding
import com.pongsakorn.saithonggroceryapp.model.LogType

/**
 * คลาส AddProduct: จัดการ Logic ของหน้าเพิ่มสินค้าใหม่ พร้อมระบบตรวจสอบข้อมูล และบันทึกกิจกรรม
 */
class AddProduct(
    view: View,
    private val repo: ProductRepository,
    private val onCancelClick: () -> Unit,
) {
    
    private val binding = AddProductBinding.bind(view)
    
    private val tags = listOf(
        binding.addTagMama,
        binding.addTagDrink,
        binding.addTagSnack,
        binding.addTagMilk,
        binding.addTagFrozen,
    )
    
    private var selectedCategory: String = "มาม่า"

    init {
        setupListeners()
    }

    private fun setupListeners() {
        tags.forEach { tag ->
            tag.setOnClickListener { updateTags(tag) }
        }

        binding.btnCancel.setOnClickListener {
            clearFields()
            onCancelClick()
        }
        
        binding.btnSaveProduct.setOnClickListener {
            val name = binding.etProductName.text.toString().trim()
            val priceStr = binding.etPrice.text.toString().trim()
            val stockStr = binding.etStock.text.toString().trim()

            if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
                Toast.makeText(binding.root.context, R.string.error_fill_all_fields, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val price = priceStr.replace(',', '.').toDoubleOrNull()
            if ((price == null) || (price <= 0)) {
                Toast.makeText(binding.root.context, R.string.error_invalid_price, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val stock = stockStr.toIntOrNull()
            if ((stock == null) || (stock < 0)) {
                Toast.makeText(binding.root.context, R.string.error_invalid_stock, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (repo.existsByName(name)) {
                Toast.makeText(binding.root.context, "มีสินค้านี้อยู่แล้ว", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            repo.add(name, selectedCategory, price, stock)
            ActivityData.logAction("เพิ่มสินค้าใหม่", "$name ($stock ชิ้น)", LogType.ADD)
            
            Toast.makeText(binding.root.context, "บันทึก $name เรียบร้อยแล้ว", Toast.LENGTH_SHORT).show()
            
            clearFields()
            onCancelClick()
        }
    }

    private fun updateTags(selectedTag: TextView) {
        selectedCategory = selectedTag.text.toString()
        tags.forEach { tag ->
            val isSelected = tag == selectedTag
            tag.setBackgroundResource(if (isSelected) R.drawable.bg_tag_orange else R.drawable.bg_rounded_gray)
            tag.setTextColor(binding.root.context.getColor(if (isSelected) R.color.text_primary else R.color.text_secondary))
            tag.setTypeface(null, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
        }
    }

    private fun clearFields() {
        binding.etProductName.text?.clear()
        binding.etPrice.text?.clear()
        binding.etStock.text?.clear()
        updateTags(binding.addTagMama)
    }
}
