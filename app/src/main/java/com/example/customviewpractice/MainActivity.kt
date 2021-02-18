package com.example.customviewpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), OnPointClickListener {
    private val events = listOf(
        Event(R.drawable.icon_univ, "대학입학", 2016, "동국대학교 컴퓨터공학과 16학번으로 입학"),
        Event(R.drawable.icon_univ, "퍼듀대학교", 2018, "퍼듀대학교 캡스톤 프로젝트에 선발되어 네트워킹 관련 프로젝트 진행"),
        Event(R.drawable.icon_univ, "라인인턴", 2019, "라인 파이낸셜플러스의 주3일 인턴십에 참여. 인니, 대만 라인뱅크의 계좌상세조회 모듈 등 구현"),
        Event(R.drawable.icon_univ, "현직장", 2020, "현직장 입사. 1년 간 당사 앱 내 웹뷰 구현"),
        Event(R.drawable.icon_univ, "이직", 2021, "(예정), 꼭!")
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        graph_view.setPointClickListener(this)
        graph_view.setData(getData())
    }

    private fun getData(): List<DataPoint<Int>>
        = (events.indices).map {
            DataPoint(it, (2022 - events[it].year) * 10)
        }

    override fun onPointClick(clickedPointIndex: Int) {
        event_view.setData(events[clickedPointIndex])
        event_view.visibility = View.VISIBLE
    }
}