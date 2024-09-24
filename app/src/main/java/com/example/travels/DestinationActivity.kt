package com.example.travels

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity

class DestinationActivity : AppCompatActivity() {

    private lateinit var nairobiEldoret: RadioButton
    private lateinit var nairobiKisumu: RadioButton
    private lateinit var eldoretNakuru: RadioButton
    private lateinit var kisumuKisii: RadioButton
    private lateinit var proceedButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination)

        nairobiEldoret = findViewById(R.id.nairobiEldoret)
        nairobiKisumu = findViewById(R.id.nairobiKisumu)
        eldoretNakuru = findViewById(R.id.eldoretNakuru)
        kisumuKisii = findViewById(R.id.kisumuKisii)
        proceedButton = findViewById(R.id.proceedButton)

        proceedButton.setOnClickListener {
            val destination: String = when {
                nairobiEldoret.isChecked -> "Nairobi - Eldoret"
                nairobiKisumu.isChecked -> "Nairobi - Kisumu"
                eldoretNakuru.isChecked -> "Eldoret - Nakuru"
                kisumuKisii.isChecked -> "Kisumu - Kisii"
                else -> ""
            }
            val price: Int = when (destination) {
                "Nairobi - Eldoret" -> 1100
                "Nairobi - Kisumu" -> 1400
                "Eldoret - Nakuru" -> 500
                "Kisumu - Kisii" -> 1000
                else -> 0
            }
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("destination", destination)
            intent.putExtra("price", price)
            startActivity(intent)
            finish()
        }
    }
}
