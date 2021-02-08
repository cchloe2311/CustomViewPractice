package com.example.customviewpractice

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class GraphView (
    context: Context,
    attributeSet: AttributeSet
) : View(context, attributeSet) { // view 클래스를 상속받음으로써 onDraw()를 재정의할 수 있음

    private val dataSet = mutableListOf<DataPoint>()
    private var xMin = 0
    private var xMax = 0
    private var yMin = 0
    private var yMax = 0 // 그래프의 범위를 설정

    private val dataPointPaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 7f
        style = Paint.Style.STROKE
    } // 각각의 dataPoint를 그릴 객체

    private val dataPointFillPaint = Paint().apply {
        color = Color.WHITE
    } // 각 점에 그려질 원형 포인트

    private val dataPointLinePaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 7f
        isAntiAlias = true
    } // dataPoint의 사이를 그릴 객체

    private val axisLinePaint = Paint().apply {
        color = Color.RED
        strokeWidth = 10f
    } // y, x 좌표를 그릴 객체

    fun setData(newDataSet: List<DataPoint>) {
        xMin = newDataSet.minBy { it.xVal }?.xVal ?: 0
        xMax = newDataSet.maxBy { it.xVal }?.xVal ?: 0
        yMin = newDataSet.minBy { it.yVal }?.yVal ?: 0
        yMax = newDataSet.maxBy { it.yVal }?.yVal ?: 0

        dataSet.clear()
        dataSet.addAll(newDataSet)

        invalidate() // 단순히 뷰를 다시 그릴 때 사용 ex. text, color 변경 시 -> onDraw()를 호출 함
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        dataSet.forEachIndexed { index, currentDataPoint ->
            val realX = currentDataPoint.xVal.toRealX()
            val realY = currentDataPoint.yVal.toRealY()

            if (index < (dataSet.size - 1)) {
                val nextDataPoint = dataSet[index + 1]

                val startX = currentDataPoint.xVal.toRealX()
                val endX = nextDataPoint.xVal.toRealX()

                val startY = currentDataPoint.yVal.toRealY()
                val endY = nextDataPoint.yVal.toRealY()

                canvas?.drawLine(startX, startY, endX, endY, dataPointLinePaint)
            }

            canvas?.drawCircle(realX, realY, 7f, dataPointFillPaint)
            canvas?.drawCircle(realX, realY, 7f, dataPointPaint)
        }

        canvas?.drawLine(0f, 0f, 0f, height.toFloat(), axisLinePaint)
        canvas?.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), axisLinePaint)
    }

    private fun Int.toRealX() = toFloat() / xMax * width
    private fun Int.toRealY() = toFloat() / yMax * height
}