package com.pongsakorn.saithonggroceryapp.data

import com.pongsakorn.saithonggroceryapp.model.Sale
import java.util.Calendar

/**
 * SaleData: ยอดขายในหน่วยความจำ (In-Memory) ยอดขายที่เก่ากว่า 3 เดือนจะถูกลบทิ้งอัตโนมัติ
 */
object SaleData : SaleRepository {

    private const val RETENTION_MONTHS = 3

    private var nextId = 0L
    private val sales = mutableListOf<Sale>()

    override fun add(productId: String, productName: String, quantity: Int, unitPrice: Double) {
        val now = System.currentTimeMillis()
        sales.add(Sale("$now-${nextId++}", productId, productName, quantity, unitPrice, now))
        purgeExpired()
    }

    override fun getAll(): List<Sale> {
        purgeExpired()
        return sales.toList()
    }

    private fun purgeExpired() {
        val cutoff = Calendar.getInstance().apply { add(Calendar.MONTH, -RETENTION_MONTHS) }.timeInMillis
        sales.removeAll { it.timeMillis < cutoff }
    }
}
