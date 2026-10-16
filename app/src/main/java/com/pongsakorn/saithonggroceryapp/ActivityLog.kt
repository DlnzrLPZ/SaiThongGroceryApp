package com.pongsakorn.saithonggroceryapp

/**
 * ActivityLog: ข้อมูลกิจกรรมในร้าน
 * - timeMillis: เวลาที่เกิดเหตุการณ์ (ใช้คำนวณอายุของ log สำหรับลบอัตโนมัติหลัง 3 เดือน)
 * - remark: สาเหตุ / หมายเหตุ ที่ผู้ใช้กรอก (เช่น ตอนอัปเดตสต็อก)
 */
data class ActivityLog(
    val id: String,
    val title: String,
    val detail: String,
    val timeMillis: Long,
    val type: LogType,
    val remark: String = "",
)

enum class LogType {
    ADD,
    UPDATE,
    DELETE,
    ALERT
}
