package com.pongsakorn.saithonggroceryapp.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.pongsakorn.saithonggroceryapp.R
import kotlin.math.max
import kotlin.math.min

/**
 * BarChartView: กราฟแท่งอย่างง่าย (ไม่ต้องพึ่งไลบรารีภายนอก) ใช้ทั้งหน้ารายงานและการ์ดยอดขายหน้าหลัก
 * แท่งที่เน้น (highlightIndex) ใช้สี highlightColor ส่วนที่เหลือใช้ barColor
 */
class BarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    var barColor: Int = ContextCompat.getColor(context, R.color.chart_bar)
        set(value) {
            field = value
            invalidate()
        }
    var highlightColor: Int = ContextCompat.getColor(context, R.color.brand_orange)
        set(value) {
            field = value
            invalidate()
        }
    var labelColor: Int = ContextCompat.getColor(context, R.color.text_secondary)
        set(value) {
            field = value
            invalidate()
        }
    var showLabels: Boolean = true
        set(value) {
            field = value
            invalidate()
        }

    private var values: List<Float> = emptyList()
    private var labels: List<String> = emptyList()
    private var highlightIndex = -1

    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 11f, resources.displayMetrics)
    }
    private val barRect = RectF()
    private val minBarHeight = dp(2f)
    private val maxBarWidth = dp(28f)
    private val maxRadius = dp(6f)

    fun setData(values: List<Float>, labels: List<String>, highlightIndex: Int) {
        this.values = values
        this.labels = labels
        this.highlightIndex = highlightIndex
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (values.isEmpty()) return

        val left = paddingLeft.toFloat()
        val contentWidth = (width - paddingLeft - paddingRight).toFloat()
        val labelBand = if (showLabels) labelPaint.textSize * 1.8f else 0f
        val baseline = height - paddingBottom - labelBand
        val chartHeight = baseline - paddingTop
        if (contentWidth <= 0f || chartHeight <= 0f) return

        val maxValue = max(values.maxOrNull() ?: 0f, 1f)
        val slot = contentWidth / values.size
        val barWidth = min(slot * 0.62f, maxBarWidth)
        val radius = min(barWidth / 2f, maxRadius)

        labelPaint.color = labelColor
        values.forEachIndexed { index, value ->
            val barHeight = max(value / maxValue * chartHeight, minBarHeight)
            val centerX = left + slot * index + slot / 2f
            barRect.set(centerX - barWidth / 2f, baseline - barHeight, centerX + barWidth / 2f, baseline)
            barPaint.color = if (index == highlightIndex) highlightColor else barColor
            canvas.drawRoundRect(barRect, radius, radius, barPaint)

            val label = labels.getOrNull(index).orEmpty()
            if (showLabels && label.isNotEmpty()) {
                val half = labelPaint.measureText(label) / 2f
                val textX = centerX.coerceIn(left + half, left + contentWidth - half)
                canvas.drawText(label, textX, height - paddingBottom - labelPaint.descent(), labelPaint)
            }
        }
    }

    private fun dp(value: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)
}
