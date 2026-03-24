package com.rocio.buencamino

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MapaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.selectedItemId = R.id.nav_mapa

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_mapa -> true
                R.id.nav_ayuda -> {
                    startActivity(Intent(this, AyudaActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}