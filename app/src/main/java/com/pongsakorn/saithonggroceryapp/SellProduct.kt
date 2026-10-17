package com.pongsakorn.saithonggroceryapp

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.pongsakorn.saithonggroceryapp.databinding.SellProductBinding

/**
 * SellProduct: หน้าขายสินค้า พิมพ์จำนวนได้โดยตรง หรือกดปุ่ม − / +
 * เมื่อยืนยัน: บันทึกยอดขาย ตัดสต็อก และเพิ่มประวัติกิจกรรม
 */
class SellProduct(
    view: View,
    private val repo: ProductRepository,
    private val saleRepo: SaleRepository,
    private val onBackClick: () -> Unit,
) {

    private val binding = SellProductBinding.bind(view)
    private val context get() = binding.root.context

    private var currentProduct: Product? = null
    private var isUpdatingText = false

    init {
        setupListeners()
    }

    /** เรียกก่อนแสดงหน้านี้ เพื่อใส่ข้อมูลสินค้าที่เลือก (อ่านสต็อกล่าสุดจากข้อมูลจริง) */
    fun setProduct(product: Product) {
        val fresh = repo.getAll().find { it.id == product.id } ?: product
        currentProduct = fresh
        binding.tvSellProductName.text = fresh.name
        binding.tvSellProductCategory.text = context.getString(R.string.category_label, fresh.category)
        binding.tvSellStockVal.text = fresh.stock.toString()
        binding.tvSellUnitPrice.text = context.getString(R.string.price_format, fresh.price.toPriceText())
        setQuantity(if (fresh.isOutOfStock) 0 else 1)
    }

    private fun setupListeners() {
        binding.etSellQty.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isUpdatingText) render()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnSellMinus.setOnClickListener {
            setQuantity((quantity() - 1).coerceAtLeast(1))
        }

        binding.btnSellPlus.setOnClickListener {
            val stock = currentProduct?.stock ?: return@setOnClickListener
            setQuantity((quantity() + 1).coerceAtMost(stock))
        }

        binding.btnSellCancel.setOnClickListener { onBackClick() }
        binding.btnConfirmSell.setOnClickListener { confirmSale() }
    }

    private fun quantity(): Int = binding.etSellQty.text.toString().toIntOrNull() ?: 0

    private fun setQuantity(qty: Int) {
        isUpdatingText = true
        binding.etSellQty.setText(qty.toString())
        binding.etSellQty.setSelection(binding.etSellQty.text?.length ?: 0)
        isUpdatingText = false
        render()
    }

    /** แสดงข้อผิดพลาด (ถ้ามี) ยอดรวม และสต็อกที่จะเหลือ ตามจำนวนที่กรอกอยู่ */
    private fun render() {
        val product = currentProduct ?: return
        val qty = quantity()
        val error = when {
            product.isOutOfStock -> context.getString(R.string.sell_error_out_of_stock)
            qty < 1 -> context.getString(R.string.sell_error_qty)
            qty > product.stock -> context.getString(R.string.sell_error_over_stock, product.stock)
            else -> null
        }
        binding.tvSellError.text = error
        binding.tvSellError.visibility = if (error == null) View.GONE else View.VISIBLE

        val validQty = if (error == null) qty else 0
        binding.tvSellTotalVal.text = context.getString(R.string.price_format, (product.price * validQty).toMoneyText())
        binding.tvSellAfterStock.text = context.getString(R.string.sell_stock_after, product.stock - validQty)
    }

    private fun confirmSale() {
        val product = currentProduct ?: return
        val qty = quantity()
        if (product.isOutOfStock || qty < 1 || qty > product.stock) {
            render()
            return
        }

        // product เป็นอ็อบเจกต์จริงในข้อมูล ต้องจำสต็อกเดิมไว้ก่อนตัดสต็อก
        val oldStock = product.stock
        val newStock = oldStock - qty

        saleRepo.add(product.id, product.name, qty, product.price)
        repo.updateStock(product.id, newStock)
        ActivityData.logAction(
            title = "ขายสินค้า",
            detail = "${product.name} $qty ชิ้น · ฿${(product.price * qty).toMoneyText()}",
            type = LogType.SALE,
        )
        ActivityData.logStockAlert(product.name, oldStock, newStock)

        Toast.makeText(context, context.getString(R.string.sell_success, product.name), Toast.LENGTH_SHORT).show()
        currentProduct = null
        onBackClick()
    }
}
