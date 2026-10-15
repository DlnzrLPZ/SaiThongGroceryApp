package com.pongsakorn.saithonggroceryapp

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ActivityData: จัดเก็บและเพิ่มบันทึกกิจกรรมล่าสุดในร้าน
 */
object ActivityData {

    val logs = mutableListOf(
        ActivityLog(
            id = "1",
            title = "เพิ่มสินค้าใหม่",
            detail = "มาม่าจัมโบ้ (120 ชิ้น)",
            timestamp = "เมื่อครู่นี้",
            type = LogType.ADD,
        ),
        ActivityLog(
            id = "2",
            title = "สินค้าใกล้หมด",
            detail = "เลย์สแน็ค คงเหลือ 8 ชิ้น",
            timestamp = "15 นาทีที่แล้ว",
            type = LogType.ALERT,
        ),
        ActivityLog(
            id = "3",
            title = "สินค้าหมดสต็อก",
            detail = "น้ำดื่มสิงห์ 600ml คงเหลือ 0 ชิ้น",
            timestamp = "1 ชั่วโมงที่แล้ว",
            type = LogType.ALERT,
        ),
    )

    fun logAction(title: String, detail: String, type: LogType) {
        val timeStr = SimpleDateFormat("HH:mm น.", Locale.getDefault()).format(Date())
        logs.add(
            0,
            ActivityLog(
                id = System.currentTimeMillis().toString(),
                title = title,
                detail = detail,
                timestamp = timeStr,
                type = type,
            ),
        )
    }
}
