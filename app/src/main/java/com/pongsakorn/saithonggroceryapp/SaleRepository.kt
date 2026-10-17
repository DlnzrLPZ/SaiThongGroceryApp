package com.pongsakorn.saithonggroceryapp

/**
 * SaleRepository: จุดเดียวที่หน้าจอใช้บันทึกและอ่านยอดขาย
 * ตอนนี้ใช้ SaleData (เก็บในหน่วยความจำ) เมื่อทำ backend เสร็จ ให้สร้างคลาสใหม่ที่ implement interface นี้
 */
interface SaleRepository {

    fun add(productId: String, productName: String, quantity: Int, unitPrice: Double)

    /** ยอดขายทั้งหมดที่ยังไม่หมดอายุ (เก็บย้อนหลัง 3 เดือน) */
    fun getAll(): List<Sale>
}
