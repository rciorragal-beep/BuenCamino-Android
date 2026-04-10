package com.rocio.buencamino

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.material.bottomnavigation.BottomNavigationView

class RecursosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recursos)

        val titulo = findViewById<TextView>(R.id.tvTitulo)
        val listView = findViewById<ListView>(R.id.listViewRecursos)

        // Recibimos datos de la pantalla anterior
        val nombreCategoria = intent.getStringExtra("categoria_nombre")
        val categoriaId = intent.getIntExtra("categoria_id", -1)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        bottomNavigation.selectedItemId = R.id.nav_home

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_mapa -> {
                    startActivity(Intent(this, MapaActivity::class.java))
                    true
                }
                R.id.nav_ayuda -> {
                    startActivity(Intent(this, AyudaActivity::class.java))
                    true
                }
                else -> false
            }
        }

        titulo.text = nombreCategoria ?: "Recursos"

        // Si el id no es válido, no hacemos nada
        if (categoriaId == -1) {
            Toast.makeText(this, "Error al cargar recursos", Toast.LENGTH_SHORT).show()
            return
        }

        // Llamada al backend para obtener recursos reales
        RetrofitClient.api.getRecursosDeCategoria(categoriaId)
            .enqueue(object : Callback<List<Recurso>> {

                override fun onResponse(
                    call: Call<List<Recurso>>,
                    response: Response<List<Recurso>>
                ) {
                    if (response.isSuccessful) {

                        val recursos = response.body() ?: emptyList()

                        val adapter = RecursoAdapter(this@RecursosActivity, recursos)
                        listView.adapter = adapter

                        // ✅ Al pulsar un recurso, abrir el enlace en el navegador
                        listView.setOnItemClickListener { _, _, position, _ ->
                            val recursoSeleccionado = recursos[position]

                            val enlace = recursoSeleccionado.enlace?.trim()

                            // Validación básica
                            if (enlace.isNullOrBlank()) {
                                Toast.makeText(
                                    this@RecursosActivity,
                                    "Este recurso no tiene enlace",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@setOnItemClickListener
                            }

                            // Si no trae http/https, le añadimos https por defecto
                            val enlaceFinal = if (
                                enlace.startsWith("http://") || enlace.startsWith("https://")
                            ) {
                                enlace
                            } else {
                                "https://$enlace"
                            }

                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(enlaceFinal))
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@RecursosActivity,
                                    "No se pudo abrir el enlace",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    } else {
                        Toast.makeText(
                            this@RecursosActivity,
                            "Error al cargar recursos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Recurso>>, t: Throwable) {
                    Toast.makeText(
                        this@RecursosActivity,
                        "No conecta con el servidor",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}