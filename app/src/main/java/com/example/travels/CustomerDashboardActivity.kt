package com.example.travels

import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class CustomerDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_dashboard)
        setupSeats()
    }

    private fun setupSeats() {
        val seatCount = 14
        val seatLayout = findViewById<RelativeLayout>(R.id.seatLayout)

        for (i in 0 until seatCount) {
            val seat = ImageView(this)
            seat.layoutParams = RelativeLayout.LayoutParams(50, 50)
            seat.setImageResource(R.drawable.seat_available)
            seat.setOnClickListener {
                seat.setImageResource(R.drawable.seat_booked)
            }
            seatLayout.addView(seat)
        }
    }
}
