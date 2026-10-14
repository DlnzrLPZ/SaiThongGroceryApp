package com.patcharawadee.main_project

/**
 * Data class แทนข้อมูลสินค้า 1 ชิ้นในระบบ
 */
data class Product(
    val id: String,
    var name: String,
    var category: String,
    var price: Double,
    var stock: Int,
)
