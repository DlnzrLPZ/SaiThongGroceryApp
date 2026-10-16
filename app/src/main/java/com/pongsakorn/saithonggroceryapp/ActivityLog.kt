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

/** สี (พื้นหลัง, ไอคอน) และไอคอนของ log แต่ละประเภท ใช้ร่วมกันทั้งหน้าหลักและหน้าประวัติ */
data class LogStyle(val bgRes: Int, val tintRes: Int, val iconRes: Int)

fun LogType.style(): LogStyle = when (this) {
    LogType.ADD -> LogStyle(R.color.light_green, R.color.brand_green, android.R.drawable.ic_input_add)
    LogType.UPDATE -> LogStyle(R.color.light_blue, R.color.brand_blue, android.R.drawable.ic_menu_edit)
    LogType.DELETE -> LogStyle(R.color.light_red, R.color.brand_red, android.R.drawable.ic_menu_delete)
    LogType.ALERT -> LogStyle(R.color.light_orange, R.color.brand_orange, android.R.drawable.ic_dialog_alert)
}
