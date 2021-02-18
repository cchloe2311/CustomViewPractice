package com.example.customviewpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), OnPointClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        graph_view.setPointClickListener(this)
        graph_view.setData(generateRandomDataPoints())
    }

    private fun generateRandomDataPoints(): List<DataPoint<Int>> {
        val random = Random()

        return (0..20).map {
            DataPoint(it, random.nextInt(50) + 1)
        }
    }

    override fun onPointClick(clickedPointIndex: Int) {
        Toast.makeText(this,"$clickedPointIndex clicked", Toast.LENGTH_SHORT).show()
    }
}