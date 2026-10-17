package com.pongsakorn.saithonggroceryapp.data

import com.pongsakorn.saithonggroceryapp.model.ActivityLog
import com.pongsakorn.saithonggroceryapp.model.LogType
import com.pongsakorn.saithonggroceryapp.model.Product
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * ActivityData: จัดเก็บบันทึกกิจกรรมในร้าน
 * log ที่เก่ากว่า 3 เดือนจะถูกลบทิ้งอัตโนมัติ (ดู purgeExpired)
 */
object ActivityData {

    private const val RETENTION_MONTHS = 3
    private const val MINUTE = 60_000L

    private var nextId = 0L
    private val localeTH = Locale.forLanguageTag("th-TH")

    val logs = mutableListOf<ActivityLog>().apply {
        val now = System.currentTimeMillis()
        add(ActivityLog("seed-3", "สินค้าหมดสต็อก", "น้ำดื่มสิงห์ 600ml คงเหลือ 0 ชิ้น", now - 60 * MINUTE, LogType.ALERT))
        add(ActivityLog("seed-2", "สินค้าใกล้หมด", "เลย์สแน็ค คงเหลือ 8 ชิ้น", now - 15 * MINUTE, LogType.ALERT))
        add(ActivityLog("seed-1", "เพิ่มสินค้าใหม่", "มาม่าจัมโบ้ (120 ชิ้น)", now - 1 * MINUTE, LogType.ADD))
        sortByDescending { it.timeMillis }
    }

    /** บันทึกกิจกรรม แล้วคืน id ของ log (ใช้ลบทีหลังได้ เช่นตอน Undo) */
    fun logAction(title: String, detail: String, type: LogType, remark: String = ""): String {
        val id = "${System.currentTimeMillis()}-${nextId++}"
        logs.add(
            0,
            ActivityLog(
                id = id,
                title = title,
                detail = detail,
                timeMillis = System.currentTimeMillis(),
                type = type,
                remark = remark.trim(),
            ),
        )
        purgeExpired()
        return id
    }

    fun removeLog(id: String) {
        logs.removeAll { it.id == id }
    }

    /** แจ้งเตือนเฉพาะตอนสต็อกเพิ่งลดลงมาถึงเกณฑ์ใกล้หมด / หมด (ไม่แจ้งซ้ำถ้าอยู่ในเกณฑ์อยู่แล้ว) */
    fun logStockAlert(name: String, oldStock: Int, newStock: Int) {
        when {
            newStock == 0 && oldStock > 0 ->
                logAction("สินค้าหมดสต็อก", "$name คงเหลือ 0 ชิ้น", LogType.ALERT)
            newStock in 1..Product.LOW_STOCK_THRESHOLD && oldStock > Product.LOW_STOCK_THRESHOLD ->
                logAction("สินค้าใกล้หมด", "$name คงเหลือ $newStock ชิ้น", LogType.ALERT)
        }
    }

    /** ลบ log ที่เก่ากว่า 3 เดือนออกจากรายการ */
    fun purgeExpired() {
        val cutoff = Calendar.getInstance().apply { add(Calendar.MONTH, -RETENTION_MONTHS) }.timeInMillis
        logs.removeAll { it.timeMillis < cutoff }
    }

    /** log ทั้งหมดที่ยังไม่หมดอายุ (ใหม่สุดก่อน) */
    fun allLogs(): List<ActivityLog> {
        purgeExpired()
        return logs.toList()
    }

    /** log ล่าสุดสำหรับหน้าหลัก */
    fun recentLogs(limit: Int = 5): List<ActivityLog> = allLogs().take(limit)

    /** เวลาในวันนั้น เช่น 14:05 น. (ใช้ในหน้าประวัติที่จัดกลุ่มตามวันอยู่แล้ว) */
    fun formatClock(timeMillis: Long): String =
        SimpleDateFormat("HH:mm น.", localeTH).format(Date(timeMillis))

    /** หัวข้อกลุ่มวัน: วันนี้ / เมื่อวาน / วันที่เต็ม */
    fun dayKey(timeMillis: Long): String =
        SimpleDateFormat("yyyyMMdd", Locale.US).format(Date(timeMillis))

    fun dayLabel(timeMillis: Long, todayLabel: String, yesterdayLabel: String): String {
        val now = System.currentTimeMillis()
        val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.timeInMillis
        return when (dayKey(timeMillis)) {
            dayKey(now) -> todayLabel
            dayKey(yesterday) -> yesterdayLabel
            else -> SimpleDateFormat("d MMM yyyy", localeTH).format(Date(timeMillis))
        }
    }

    /** วันนี้แสดงเฉพาะเวลา, วันอื่นแสดงวันที่ + เวลา */
    fun formatTime(timeMillis: Long): String {
        val date = Date(timeMillis)
        val today = Calendar.getInstance()
        val then = Calendar.getInstance().apply { time = date }
        val sameDay = today.get(Calendar.YEAR) == then.get(Calendar.YEAR) &&
            today.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR)
        val pattern = if (sameDay) "HH:mm น." else "d MMM yyyy HH:mm น."
        return SimpleDateFormat(pattern, localeTH).format(date)
    }
}
