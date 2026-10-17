package com.pongsakorn.saithonggroceryapp.data

import com.pongsakorn.saithonggroceryapp.model.Product

/**
 * ProductRepository: จุดเดียวที่หน้าจอทุกหน้าใช้เข้าถึงข้อมูลสินค้า
 *
 * ตอนนี้ใช้ ProductData (เก็บในหน่วยความจำ) เป็นตัวทำงานจริง
 * เมื่อทำ backend (Ktor) เสร็จ ให้สร้างคลาสใหม่ที่ implement interface นี้
 * (เช่น RemoteProductRepository ที่เรียก Retrofit) แล้วเปลี่ยนที่ MainNavigation ที่เดียว
 * หน้าจออื่น ๆ ไม่ต้องแก้
 */
interface ProductRepository {

    /** สินค้าทั้งหมด (คืนเป็น List ที่แก้ไขโดยตรงไม่ได้) */
    fun getAll(): List<Product>

    fun add(name: String, category: String, price: Double, stock: Int)

    fun update(id: String, name: String, category: String, price: Double)

    fun updateStock(id: String, newStock: Int)

    fun delete(product: Product)

    /** คืนสินค้าที่เพิ่งลบ (ใช้กับปุ่ม Undo) */
    fun restore(product: Product, index: Int = 0)

    /** ตำแหน่งของสินค้าในรายการ ใช้จำตำแหน่งก่อนลบ (-1 ถ้าไม่พบ) */
    fun indexOf(id: String): Int = getAll().indexOfFirst { it.id == id }

    /** มีสินค้าชื่อนี้อยู่แล้วหรือไม่ (ไม่สนตัวพิมพ์เล็ก/ใหญ่) */
    fun existsByName(name: String): Boolean =
        getAll().any { it.name.equals(name.trim(), ignoreCase = true) }
}
