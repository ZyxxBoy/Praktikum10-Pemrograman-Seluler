package com.example.donutapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.donutapp.R
import com.example.donutapp.data.DatabaseHelper

class ProfileFragment : Fragment() {

    private lateinit var dbHelper: DatabaseHelper
    private var email: String = ""

    companion object {
        fun newInstance(email: String): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString("email", email)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout langsung tanpa binding
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = arguments?.getString("email") ?: ""
        dbHelper = DatabaseHelper(requireContext())

        // Inisialisasi view
        // val textEmail = view.findViewById<TextView>(R.id.text_email)
        // textEmail.text = email

        // Load data pengguna dari database
        // Anda bisa implementasikan ini sesuai fungsi DatabaseHelper
    }
}
