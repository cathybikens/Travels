package com.example.travels

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val destination = intent.getStringExtra("destination")
        val price = intent.getIntExtra("price", 0)

        // Initiate M-Pesa STK push (this part requires Safaricom API integration)

        // Assuming payment is successful, navigate to SuccessActivity
        val intent = Intent(this, SuccessActivity::class.java)
        intent.putExtra("ticketNumber", generateTicketNumber())
        startActivity(intent)
        finish()
    }

    private fun generateTicketNumber(): String {
        return "TICKET-" + System.currentTimeMillis()
    }
}
