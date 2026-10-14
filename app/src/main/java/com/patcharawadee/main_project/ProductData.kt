package com.patcharawadee.main_project

/**
 * ProductData: ข้อมูลสินค้าจำลองในหน่วยความจำ (In-Memory Data Store)
 */
object ProductData {

    val products = mutableListOf(
        Product(
            id = "1",
            name = "เลย์สแน็ค",
            category = "ขนม",
            price = 25.0,
            stock = 8,
        ),
        Product(
            id = "2",
            name = "มาม่าจัมโบ้",
            category = "มาม่า",
            price = 12.0,
            stock = 120,
        ),
        Product(
            id = "3",
            name = "น้ำดื่มสิงห์ 600ml",
            category = "เครื่องดื่ม",
            price = 10.0,
            stock = 0,
        ),
    )

    fun addProduct(name: String, category: String, price: Double, stock: Int) {
        val nextId = ((products.maxOfOrNull { it.id.toIntOrNull() ?: 0 } ?: 0) + 1).toString()
        products.add(0, Product(nextId, name, category, price, stock))
    }

    fun deleteProduct(product: Product) {
        products.remove(product)
    }

    fun restoreProduct(product: Product, index: Int = 0) {
        if (!products.contains(product)) {
            val safeIndex = index.coerceIn(0, products.size)
            products.add(safeIndex, product)
        }
    }
}
