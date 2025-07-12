package com.example.donutapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.donutapp.R
import com.example.donutapp.data.Donut
import java.text.NumberFormat
import java.util.Locale

class DonutAdapter(
    private val donuts: List<Donut>,
    private val onItemClick: (Donut) -> Unit,
    private val onCartUpdated: () -> Unit
) : RecyclerView.Adapter<DonutAdapter.DonutViewHolder>() {

    class DonutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageDonut)
        val nameView: TextView = itemView.findViewById(R.id.textNameDonut)
        val priceView: TextView = itemView.findViewById(R.id.textPriceDonut)
        val addButton: Button = itemView.findViewById(R.id.buttonAdd)

        private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
            maximumFractionDigits = 0
        }

        fun formatPrice(price: Int): String = currencyFormat.format(price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_donut, parent, false)
        return DonutViewHolder(view)
    }

    override fun onBindViewHolder(holder: DonutViewHolder, position: Int) {
        val donut = donuts[position]

        // Set image (replace with your actual image loading)
        holder.imageView.setImageResource(R.drawable.ic_logo)
        holder.nameView.text = donut.name
        holder.priceView.text = holder.formatPrice(donut.price)

        holder.addButton.setOnClickListener {
            // Use the TransactionAdapter's addItem method
            TransactionAdapter.addItem(
                donutId = donut.id,
                donutName = donut.name,
                donutPrice = donut.price
            )

            // Notify about cart update
            onCartUpdated()

            Toast.makeText(
                holder.itemView.context,
                "${donut.name} added to cart",
                Toast.LENGTH_SHORT
            ).show()
        }

        holder.itemView.setOnClickListener {
            onItemClick(donut)
        }
    }

    override fun getItemCount(): Int = donuts.size
}
