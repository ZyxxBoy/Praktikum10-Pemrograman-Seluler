
package com.example.donutapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.donutapp.R
import com.example.donutapp.adapter.DonutAdapter
import com.example.donutapp.data.DatabaseHelper

class MenuFragment : Fragment() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var donutAdapter: DonutAdapter
    private lateinit var rvDonuts: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvDonuts = view.findViewById(R.id.rvDonuts)
        dbHelper = DatabaseHelper(requireContext())
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val donuts = dbHelper.getAllDonuts()

        donutAdapter = DonutAdapter(
            donuts = donuts,
            onItemClick = { donut ->
                // Handle donut item click
                // Example: show donut details
            },
            onCartUpdated = {
                // Update cart total if needed
                // This will be called when items are added to cart
            }
        )

        rvDonuts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = donutAdapter
        }
    }
}
