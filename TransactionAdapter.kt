package com.example.donutapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.donutapp.R
import java.text.NumberFormat
import java.util.Locale

class TransactionAdapter(
    private val onTotalChanged: (Int) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    companion object {
        private val listId = mutableListOf<Int>()
        private val listName = mutableListOf<String>()
        private val listHarga = mutableListOf<Int>()
        private val listJumlah = mutableListOf<Int>()
        private var harga: Int = 0

        fun clearAll() {
            listId.clear()
            listName.clear()
            listHarga.clear()
            listJumlah.clear()
            harga = 0
        }

        fun addItem(donutId: Int, donutName: String, donutPrice: Int) {
            val existingIndex = listId.indexOf(donutId)
            if (existingIndex != -1) {
                listJumlah[existingIndex] += 1
                harga += donutPrice
            } else {
                listId.add(donutId)
                listName.add(donutName)
                listHarga.add(donutPrice)
                listJumlah.add(1)
                harga += donutPrice
            }
        }

        fun getTotalWithTax(): Double = harga * 1.1
    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.findViewById(R.id.textNameTransaksi)
        val priceView: TextView = itemView.findViewById(R.id.textHargaTransaksi)
        val quantityView: TextView = itemView.findViewById(R.id.textQtyTransaksi)
        val plusButton: Button = itemView.findViewById(R.id.buttonPlus)
        val minusButton: Button = itemView.findViewById(R.id.buttonMinus)
        val deleteButton: Button = itemView.findViewById(R.id.buttonHapusTransaksi)

        private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
            maximumFractionDigits = 0
        }

        fun formatPrice(price: Int): String = currencyFormat.format(price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.nameView.text = listName[position]
        holder.priceView.text = holder.formatPrice(listHarga[position])
        holder.quantityView.text = listJumlah[position].toString()

        holder.plusButton.setOnClickListener {
            listJumlah[position]++
            harga += listHarga[position]
            updateItem(position, holder)
        }

        holder.minusButton.setOnClickListener {
            if (listJumlah[position] > 1) {
                listJumlah[position]--
                harga -= listHarga[position]
                updateItem(position, holder)
            } else {
                Toast.makeText(holder.itemView.context,
                    "Minimum quantity is 1",
                    Toast.LENGTH_SHORT).show()
            }
        }

        holder.deleteButton.setOnClickListener {
            harga -= listHarga[position] * listJumlah[position]
            removeItem(position)
            Toast.makeText(holder.itemView.context,
                "${listName[position]} removed",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateItem(position: Int, holder: TransactionViewHolder) {
        holder.quantityView.text = listJumlah[position].toString()
        notifyItemChanged(position)
        onTotalChanged(harga)
    }

    private fun removeItem(position: Int) {
        listId.removeAt(position)
        listName.removeAt(position)
        listHarga.removeAt(position)
        listJumlah.removeAt(position)
        notifyDataSetChanged()
        onTotalChanged(harga)
    }

    override fun getItemCount(): Int = listId.size
}
