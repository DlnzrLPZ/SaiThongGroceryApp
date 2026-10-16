package com.pongsakorn.saithonggroceryapp

/**
 * Data class แทนข้อมูลสินค้า 1 ชิ้นในระบบ
 */
data class Product(
    val id: String,
    var name: String,
    var category: String,
    var price: Double,
    var stock: Int,
) {
    /** หมดสต็อก (0 ชิ้น) */
    val isOutOfStock: Boolean get() = stock == 0

    /** ใกล้หมด (1 ถึง LOW_STOCK_THRESHOLD ชิ้น) */
    val isLowStock: Boolean get() = stock in 1..LOW_STOCK_THRESHOLD

    companion object {
        /** เกณฑ์สินค้าใกล้หมด: เหลือไม่เกินกี่ชิ้น (แก้ที่เดียวใช้ทั้งแอป) */
        const val LOW_STOCK_THRESHOLD = 10
    }
}

/** 25.0 -> "25", 25.5 -> "25.5" (ไม่ตัดทศนิยมทิ้ง) */
fun Double.toPriceText(): String =
    if (this % 1.0 == 0.0) toInt().toString() else toString()
