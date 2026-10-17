package com.pongsakorn.saithonggroceryapp.data

import com.pongsakorn.saithonggroceryapp.model.Sale
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class SalesPeriod { TODAY, WEEK, MONTH, THREE_MONTHS }

data class TopSeller(val rank: Int, val name: String, val quantity: Int, val revenue: Double)

/**
 * SalesSummary: ผลสรุปยอดขายของช่วงเวลาหนึ่ง
 * - barValues / barLabels: ข้อมูลกราฟแท่ง (วันนี้ = รายชั่วโมง, สัปดาห์/เดือน = รายวัน, 3 เดือน = รายสัปดาห์)
 * - highlightIndex: แท่งที่เน้นสี (ชั่วโมงปัจจุบัน หรือแท่งล่าสุด)
 */
data class SalesSummary(
    val revenue: Double,
    val pieces: Int,
    val barValues: List<Float>,
    val barLabels: List<String>,
    val highlightIndex: Int,
    val topSellers: List<TopSeller>,
)

/** คำนวณสรุปยอดขายจากรายการขาย (ไม่ยุ่งกับหน้าจอ) */
object SalesStats {

    private const val HOUR_MILLIS = 3_600_000L
    private const val WEEK_DAYS = 7
    private const val MONTH_DAYS = 30
    private const val THREE_MONTH_WEEKS = 13
    private const val TOP_LIMIT = 3

    private val localeTH = Locale.forLanguageTag("th-TH")

    private class Bucket(val start: Long, val end: Long, val label: String)

    fun summarize(
        sales: List<Sale>,
        period: SalesPeriod,
        nowMillis: Long = System.currentTimeMillis(),
    ): SalesSummary {
        val buckets = bucketsFor(period, nowMillis)
        val rangeStart = buckets.first().start
        val rangeEnd = buckets.last().end
        val inRange = sales.filter { it.timeMillis in rangeStart until rangeEnd }

        val values = buckets.map { bucket ->
            inRange.filter { it.timeMillis in bucket.start until bucket.end }.sumOf { it.total }.toFloat()
        }

        val topSellers = inRange
            .groupBy { it.productId }
            .map { (_, list) -> Triple(list.last().productName, list.sumOf { it.quantity }, list.sumOf { it.total }) }
            .sortedByDescending { it.second }
            .take(TOP_LIMIT)
            .mapIndexed { index, (name, quantity, revenue) -> TopSeller(index + 1, name, quantity, revenue) }

        val highlight = if (period == SalesPeriod.TODAY) {
            Calendar.getInstance().apply { timeInMillis = nowMillis }.get(Calendar.HOUR_OF_DAY)
        } else {
            buckets.lastIndex
        }

        return SalesSummary(
            revenue = inRange.sumOf { it.total },
            pieces = inRange.sumOf { it.quantity },
            barValues = values,
            barLabels = buckets.map { it.label },
            highlightIndex = highlight,
            topSellers = topSellers,
        )
    }

    /** ยอดขายรวมของวันที่ย้อนหลัง daysAgo วัน (0 = วันนี้, 1 = เมื่อวาน) */
    fun revenueOnDay(
        sales: List<Sale>,
        daysAgo: Int,
        nowMillis: Long = System.currentTimeMillis(),
    ): Double {
        val start = startOfDay(nowMillis, daysAgo)
        val end = startOfDay(nowMillis, daysAgo - 1)
        return sales.filter { it.timeMillis in start until end }.sumOf { it.total }
    }

    private fun startOfDay(nowMillis: Long, daysAgo: Int): Long =
        Calendar.getInstance().apply {
            timeInMillis = nowMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_YEAR, -daysAgo)
        }.timeInMillis

    private fun bucketsFor(period: SalesPeriod, nowMillis: Long): List<Bucket> = when (period) {
        SalesPeriod.TODAY -> hourBuckets(nowMillis)
        SalesPeriod.WEEK -> dayBuckets(nowMillis, WEEK_DAYS) { _, start ->
            SimpleDateFormat("EEE", localeTH).format(Date(start))
        }
        SalesPeriod.MONTH -> dayBuckets(nowMillis, MONTH_DAYS) { index, start ->
            if (index % 7 == 0) SimpleDateFormat("d", localeTH).format(Date(start)) else ""
        }
        SalesPeriod.THREE_MONTHS -> weekBuckets(nowMillis)
    }

    private fun hourBuckets(nowMillis: Long): List<Bucket> {
        val dayStart = startOfDay(nowMillis, 0)
        return (0 until 24).map { hour ->
            val start = dayStart + hour * HOUR_MILLIS
            Bucket(start, start + HOUR_MILLIS, if (hour % 6 == 0) hour.toString() else "")
        }
    }

    private fun dayBuckets(
        nowMillis: Long,
        days: Int,
        labelOf: (index: Int, start: Long) -> String,
    ): List<Bucket> =
        (days - 1 downTo 0).mapIndexed { index, daysAgo ->
            val start = startOfDay(nowMillis, daysAgo)
            Bucket(start, startOfDay(nowMillis, daysAgo - 1), labelOf(index, start))
        }

    /** 13 สัปดาห์ (91 วัน) นับถึงวันนี้ แท่งสุดท้ายคือ 7 วันล่าสุด */
    private fun weekBuckets(nowMillis: Long): List<Bucket> =
        (0 until THREE_MONTH_WEEKS).map { week ->
            val firstDaysAgo = (THREE_MONTH_WEEKS - 1 - week) * 7 + 6
            val start = startOfDay(nowMillis, firstDaysAgo)
            val label = if (week % 4 == 0) SimpleDateFormat("d MMM", localeTH).format(Date(start)) else ""
            Bucket(start, startOfDay(nowMillis, firstDaysAgo - 7), label)
        }
}
