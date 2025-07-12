package com.example.donutapp.ui.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.donutapp.R
import com.example.donutapp.adapter.TransactionAdapter
import com.example.donutapp.ui.home.MainActivity
import java.text.NumberFormat
import java.util.Locale

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val totalView = findViewById<TextView>(R.id.tv_total)
        val changeView = findViewById<TextView>(R.id.tv_change)
        val cashInput = findViewById<EditText>(R.id.et_cash)
        val finishBtn = findViewById<Button>(R.id.btn_finish)

        // Format currency
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        format.maximumFractionDigits = 0

        // Calculate total with 10% tax
        val total = TransactionAdapter.getTotalWithTax()
        totalView.text = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(total)

        // When payment done
        TransactionAdapter.clearAll()

        finishBtn.setOnClickListener {
            val cash = cashInput.text.toString().toDoubleOrNull() ?: 0.0
            val change = cash - total

            changeView.text = format.format(change)

            // Clear transaction data using the companion object method
            TransactionAdapter.clearAll()

            // Return to main activity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
