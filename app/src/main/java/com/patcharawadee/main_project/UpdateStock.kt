package com.patcharawadee.main_project

import android.view.View
import android.widget.Toast
import com.patcharawadee.main_project.databinding.UpdateStockBinding

/**
 * คลาส UpdateStock: จัดการ Logic ของหน้าการเพิ่ม/ลดจำนวนสินค้าในสต็อก
 */
class UpdateStock(view: View, private val onBackClick: () -> Unit) {

    private val binding = UpdateStockBinding.bind(view)
    
    private var currentQty = 8
    private var addQty = 20

    init {
        setupListeners()
        updateDisplay()
    }

    private fun setupListeners() {
        binding.btnMinus.setOnClickListener {
            if (addQty > 0) {
                addQty--
                updateDisplay()
            }
        }

        binding.btnPlus.setOnClickListener {
            addQty++
            updateDisplay()
        }

        binding.btnUpdateStockCancel.setOnClickListener {
            onBackClick()
        }
        
        binding.btnConfirmUpdateStock.setOnClickListener {
            ProductData.products.firstOrNull()?.let { targetProduct ->
                targetProduct.stock = currentQty + addQty
                ActivityData.logAction("อัปเดตจำนวนสต็อก", "${targetProduct.name} เป็น ${targetProduct.stock} ชิ้น", LogType.UPDATE)
            }
            Toast.makeText(binding.root.context, "อัปเดตสต็อกเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show()
            onBackClick()
        }
    }

    private fun updateDisplay() {
        ProductData.products.firstOrNull()?.let { targetProduct ->
            currentQty = targetProduct.stock
            binding.tvUpdateProductName.text = targetProduct.name
            binding.tvUpdateProductCategory.text = binding.root.context.getString(R.string.category_label, targetProduct.category)
        }

        binding.tvAddQtyVal.text = addQty.toString()
        binding.tvCurrentStockVal.text = currentQty.toString()
        
        val afterStock = currentQty + addQty
        binding.tvAfterStockVal.text = binding.root.context.getString(R.string.unit_pieces, afterStock)
    }
}
