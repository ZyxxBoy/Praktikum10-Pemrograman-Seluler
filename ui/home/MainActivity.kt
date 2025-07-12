package com.example.donutapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.donutapp.R
import com.example.donutapp.ui.auth.LoginActivity
import com.example.donutapp.ui.payment.PaymentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private var currentEmail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentEmail = intent.getStringExtra("email") ?: ""

        // Initialize bottom navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set initial fragment
        replaceFragment(HomeFragment())

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_menu -> replaceFragment(MenuFragment())
                R.id.nav_transaction -> replaceFragment(TransactionFragment())
                R.id.nav_profile -> replaceFragment(ProfileFragment.newInstance(currentEmail))
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    fun navigateToPayment() {
        startActivity(Intent(this, PaymentActivity::class.java))
    }
}
