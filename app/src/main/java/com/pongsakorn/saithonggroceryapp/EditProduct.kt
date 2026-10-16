package com.pongsakorn.saithonggroceryapp

import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.pongsakorn.saithonggroceryapp.databinding.EditProductBinding

/**
 * คลาส EditProduct: จัดการ Logic ของหน้าแก้ไขข้อมูลสินค้า
 * เรียก setProduct() ก่อนแสดงหน้านี้ เพื่อใส่ข้อมูลเดิมของสินค้าที่ถูกกด
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

    private var currentProductId: String? = null
    private var selectedCategory: String = ""

    init {
        setupListeners()
    }

    /** ใส่ชื่อ หมวดหมู่ และราคาปัจจุบันของสินค้าลงในฟอร์ม */
    fun setProduct(product: Product) {
        currentProductId = product.id
        binding.etEditProductName.setText(product.name)
        binding.etEditPrice.setText(product.price.toPriceText())

        val tag = tags.find { it.text.toString() == product.category } ?: tags.first()
        updateTags(tag)
    }

    private fun setupListeners() {
        tags.forEach { tag ->
            tag.setOnClickListener { updateTags(tag) }
        }

        binding.btnEditCancel.setOnClickListener {
            onBackClick()
        }

        binding.btnUpdateProduct.setOnClickListener {
            val productId = currentProductId ?: return@setOnClickListener
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

            ProductData.updateProduct(productId, name, selectedCategory, price)
            ActivityData.logAction(
                "แก้ไขข้อมูลสินค้า",
                "$name · $selectedCategory · ฿${price.toPriceText()}",
                LogType.UPDATE,
            )

            Toast.makeText(binding.root.context, "อัปเดต $name เรียบร้อยแล้ว", Toast.LENGTH_SHORT).show()
            currentProductId = null
            onBackClick()
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
}
