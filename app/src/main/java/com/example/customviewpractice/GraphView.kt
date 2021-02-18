package com.example.customviewpractice

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class GraphView (
        context: Context,
        attributeSet: AttributeSet
) : View(context, attributeSet), View.OnClickListener { // view 클래스를 상속받음으로써 onDraw()를 재정의할 수 있음

    lateinit var onPointClickListener: OnPointClickListener
    private val dataSet = mutableListOf<DataPoint<Int>>()
    private val realDataSet = mutableListOf<DataPoint<Float>>()
    private var xMin = 0
    private var xMax = 0
    private var yMin = 0
    private var yMax = 0 // 그래프의 범위를 설정

    private val NO_MATCH_POINT = -1

    private val dataPointPaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 7f // 도형의 두께 설정
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

    fun setPointClickListener(onPointClickListener: OnPointClickListener) {
        this.onPointClickListener = onPointClickListener
    }
    fun setData(newDataSet: List<DataPoint<Int>>) {
        xMin = newDataSet.minBy { it.xVal }?.xVal ?: 0
        xMax = newDataSet.maxBy { it.xVal }?.xVal ?: 0
        yMin = newDataSet.minBy { it.yVal }?.yVal ?: 0
        yMax = newDataSet.maxBy { it.yVal }?.yVal ?: 0

        dataSet.clear()
        dataSet.addAll(newDataSet)

        invalidate() // 단순히 뷰를 다시 그릴 때 사용 ex. text, color 변경 시 -> onDraw()를 호출 함
    }

    private fun toRealDataSet(newDataSet: List<DataPoint<Int>>): List<DataPoint<Float>>
        = newDataSet.map { DataPoint(it.xVal.toRealX(), it.yVal.toRealY()) }

    private fun Int.toRealX() = toFloat() / xMax * width
    private fun Int.toRealY() = toFloat() / yMax * height

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        realDataSet.clear()
        realDataSet.addAll(toRealDataSet(dataSet))

        realDataSet.forEachIndexed { index, currentDataPoint ->
            val realX = currentDataPoint.xVal
            val realY = currentDataPoint.yVal

            if (index < (dataSet.size - 1)) {
                val nextDataPoint = realDataSet[index + 1]

                val startX = currentDataPoint.xVal
                val endX = nextDataPoint.xVal

                val startY = currentDataPoint.yVal
                val endY = nextDataPoint.yVal

                canvas?.drawLine(startX, startY, endX, endY, dataPointLinePaint)
            }

            canvas?.drawCircle(realX, realY, 7f, dataPointFillPaint)
            canvas?.drawCircle(realX, realY, 7f, dataPointPaint)
        }

        canvas?.drawLine(0f, 0f, 0f, height.toFloat(), axisLinePaint)
        canvas?.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), axisLinePaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)

        if (event?.action == MotionEvent.ACTION_DOWN) {
            val clickedPointIndex = getClickedPointIndex(event.y, event.x)
            if (clickedPointIndex != NO_MATCH_POINT) onPointClickListener.onPointClick(clickedPointIndex)
        }

        return true // 그 뒤 리스너까지 이벤트를 전달하지 않고, 터치만 하고 끝
    }

    private fun getClickedPointIndex(y: Float, x: Float): Int {
        realDataSet.forEachIndexed { index, dataPoint ->
            if ((y >= (dataPoint.yVal - 30f)) && (y <= (dataPoint.yVal + 30f)) && (x >= (dataPoint.xVal - 30f)) && (x <= (dataPoint.xVal + 30f))) return index
        }

        return NO_MATCH_POINT
    }

    override fun onClick(p0: View?) {
    }
}