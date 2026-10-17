package com.pongsakorn.saithonggroceryapp

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Sale: การขายสินค้า 1 รายการ
 * เก็บชื่อและราคา ณ ตอนขายไว้ในตัว เพื่อให้ยอดขายเก่าไม่เปลี่ยน แม้ภายหลังจะแก้ราคาหรือลบสินค้า
 */
data class Sale(
    val id: String,
    val productId: String,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val timeMillis: Long,
) {
    val total: Double get() = unitPrice * quantity
}

/** 1286.0 -> "1,286", 12.5 -> "12.5" */
fun Double.toMoneyText(): String =
    DecimalFormat("#,##0.##", DecimalFormatSymbols(Locale.US)).format(this)
