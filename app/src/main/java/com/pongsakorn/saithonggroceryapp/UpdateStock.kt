package com.pongsakorn.saithonggroceryapp

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.pongsakorn.saithonggroceryapp.databinding.UpdateStockBinding

/**
 * คลาส UpdateStock: จัดการ Logic ของหน้าการเพิ่ม/ลดจำนวนสินค้าในสต็อก
 */
class UpdateStock(view: View, private val onBackClick: () -> Unit) {

    private val binding = UpdateStockBinding.bind(view)
    
    private var currentProduct: Product? = null
    private var currentQty = 0
    private var addQty = 0
    private var isUpdatingText = false

    init {
        setupListeners()
        updateDisplay()
    }

    fun setProduct(product: Product?) {
        currentProduct = product ?: ProductData.products.firstOrNull()
        addQty = 0
        updateDisplay()
    }

    private fun setupListeners() {
        binding.etAddQtyVal.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdatingText) return
                val inputStr = s?.toString() ?: ""
                addQty = when {
                    inputStr.isEmpty() || inputStr == "-" -> 0
                    else -> inputStr.toIntOrNull() ?: 0
                }
                updateSummaryDisplay()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnMinus.setOnClickListener {
            if (currentQty + addQty > 0) {
                addQty--
                updateQtyEditText()
            }
        }

        binding.btnPlus.setOnClickListener {
            addQty++
            updateQtyEditText()
        }

        binding.btnUpdateStockCancel.setOnClickListener {
            onBackClick()
        }
        
        binding.btnConfirmUpdateStock.setOnClickListener {
            currentProduct?.let { targetProduct ->
                val newStock = (currentQty + addQty).coerceAtLeast(0)
                ProductData.updateStock(targetProduct.id, newStock)
                targetProduct.stock = newStock

                ActivityData.logAction("อัปเดตจำนวนสต็อก", "${targetProduct.name} เป็น $newStock ชิ้น", LogType.UPDATE)
            }
            Toast.makeText(binding.root.context, "อัปเดตสต็อกเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show()
            onBackClick()
        }
    }

    private fun updateQtyEditText() {
        isUpdatingText = true
        binding.etAddQtyVal.setText(addQty.toString())
        binding.etAddQtyVal.setSelection(binding.etAddQtyVal.text?.length ?: 0)
        isUpdatingText = false
        updateSummaryDisplay()
    }

    private fun updateSummaryDisplay() {
        val afterStock = (currentQty + addQty).coerceAtLeast(0)
        binding.tvAfterStockVal.text = binding.root.context.getString(R.string.unit_pieces, afterStock)
    }

    private fun updateDisplay() {
        (currentProduct ?: ProductData.products.firstOrNull())?.let { targetProduct ->
            currentProduct = targetProduct
            currentQty = targetProduct.stock
            binding.tvUpdateProductName.text = targetProduct.name
            binding.tvUpdateProductCategory.text = binding.root.context.getString(R.string.category_label, targetProduct.category)
        }

        isUpdatingText = true
        binding.etAddQtyVal.setText(addQty.toString())
        isUpdatingText = false

        binding.tvCurrentStockVal.text = currentQty.toString()
        updateSummaryDisplay()
    }
}
