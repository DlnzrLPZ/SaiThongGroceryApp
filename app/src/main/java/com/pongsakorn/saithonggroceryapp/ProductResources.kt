package com.pongsakorn.saithonggroceryapp

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

/**
 * ProductResources: ศูนย์รวมการจัดการไอคอนและสีตามหมวดหมู่สินค้า
 * ช่วยให้เรียกใช้งานไอคอนได้ง่ายและเป็นระเบียบทั้งแอป
 */
object ProductResources {

    /**
     * ดึง Resource ID ของไอคอนตามชื่อหมวดหมู่
     */
    @DrawableRes
    fun getIconByCategory(category: String): Int {
        return when (category) {
            "มาม่า" -> R.drawable.noodle
            "เครื่องดื่ม" -> R.drawable.water
            "ขนม" -> R.drawable.chips
            "นม" -> R.drawable.milk
            "แช่แข็ง" -> R.drawable.frozenfood
            else -> R.drawable.ic_launcher_foreground // ไอคอนเริ่มต้นถ้าไม่พบ
        }
    }

    /**
     * ดึง Resource ID ของสีพื้นหลังไอคอนตามชื่อหมวดหมู่
     */
    @ColorRes
    fun getBackgroundColorByCategory(category: String): Int {
        return when (category) {
            "มาม่า" -> R.color.light_red
            "เครื่องดื่ม" -> R.color.light_blue
            "ขนม" -> R.color.light_orange
            "นม" -> R.color.light_purple
            "แช่แข็ง" -> R.color.light_blue // หรือสีที่ชอบ
            else -> R.color.background_gray
        }
    }

    /**
     * ดึง Resource ID ของสี Tint ไอคอนตามชื่อหมวดหมู่
     */
    @ColorRes
    fun getTintByCategory(category: String): Int {
        return when (category) {
            "มาม่า" -> R.color.brand_red
            "เครื่องดื่ม" -> R.color.brand_blue
            "ขนม" -> R.color.brand_orange
            "นม" -> R.color.brand_purple
            "แช่แข็ง" -> R.color.brand_blue
            else -> R.color.text_secondary
        }
    }
}
