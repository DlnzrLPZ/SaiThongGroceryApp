package com.pongsakorn.saithonggroceryapp

/**
 * ProductData: ข้อมูลสินค้าจำลองในหน่วยความจำ (In-Memory Data Store)
 */
object ProductData : ProductRepository {

    private val products = mutableListOf(
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

    override fun getAll(): List<Product> = products.toList()

    override fun add(name: String, category: String, price: Double, stock: Int) {
        val nextId = ((products.maxOfOrNull { it.id.toIntOrNull() ?: 0 } ?: 0) + 1).toString()
        products.add(0, Product(nextId, name, category, price, stock))
    }

    override fun update(id: String, name: String, category: String, price: Double) {
        products.find { it.id == id }?.let {
            it.name = name
            it.category = category
            it.price = price
        }
    }

    override fun updateStock(id: String, newStock: Int) {
        products.find { it.id == id }?.stock = newStock
    }

    override fun delete(product: Product) {
        products.removeAll { it.id == product.id }
    }

    override fun restore(product: Product, index: Int) {
        if (products.none { it.id == product.id }) {
            val safeIndex = index.coerceIn(0, products.size)
            products.add(safeIndex, product)
        }
    }
}
