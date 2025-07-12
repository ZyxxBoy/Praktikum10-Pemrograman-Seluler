package com.example.donutapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.donutapp.R
import com.example.donutapp.adapter.TransactionAdapter

class TransactionFragment : Fragment() {

    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var rvTransaksi: RecyclerView
    private lateinit var txtOrder: TextView
    private lateinit var txtTax: TextView
    private lateinit var txtTotal: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout directly
        val view = inflater.inflate(R.layout.fragment_transaction, container, false)

        // Initialize views
        rvTransaksi = view.findViewById(R.id.recycler_transaksi)
        txtOrder = view.findViewById(R.id.text_total_order)
        txtTax = view.findViewById(R.id.text_tax)
        txtTotal = view.findViewById(R.id.text_total_price)
        val buttonPay: Button = view.findViewById(R.id.button_pay_now)

        // Initialize adapter with callback
        transactionAdapter = TransactionAdapter { totalOrder ->
            updateTotals(totalOrder)
        }

        // Setup RecyclerView
        rvTransaksi.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = transactionAdapter
        }

        buttonPay.setOnClickListener {
            (activity as? MainActivity)?.navigateToPayment()
        }

        return view
    }

    private fun updateTotals(totalOrder: Int) {
        val tax = totalOrder * 0.10
        val totalPrice = totalOrder + tax

        txtOrder.text = totalOrder.toString()
        txtTax.text = tax.toString()
        txtTotal.text = totalPrice.toString()
    }

    companion object {
        fun newInstance() = TransactionFragment()
    }
}
