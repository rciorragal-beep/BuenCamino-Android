package com.rocio.buencamino

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecursosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recursos)

        val titulo = findViewById<TextView>(R.id.tvTitulo)
        val listView = findViewById<ListView>(R.id.listViewRecursos)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        val categoriaId = intent.getIntExtra("categoria_id", -1)
        val categoriaNombre = intent.getStringExtra("categoria_nombre") ?: ""

        titulo.text = categoriaNombre

        if (categoriaId == -1) {
            Toast.makeText(this, "Error al cargar recursos", Toast.LENGTH_SHORT).show()
            return
        }

        bottomNavigation.selectedItemId = R.id.nav_home

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }

                R.id.nav_mapa -> {
                    val mapaIntent = Intent(this, MapaActivity::class.java)
                    mapaIntent.putExtra("categoria_id", categoriaId)
                    mapaIntent.putExtra("categoria_nombre", categoriaNombre)
                    startActivity(mapaIntent)
                    true
                }

                R.id.nav_ayuda -> {
                    startActivity(Intent(this, AyudaActivity::class.java))
                    true
                }

                else -> false
            }
        }

        RetrofitClient.api.getRecursosDeCategoria(categoriaId)
            .enqueue(object : Callback<List<Recurso>> {

                override fun onResponse(
                    call: Call<List<Recurso>>,
                    response: Response<List<Recurso>>
                ) {
                    if (response.isSuccessful) {
                        val recursos = response.body() ?: emptyList()

                        if (recursos.isEmpty()) {
                            Toast.makeText(
                                this@RecursosActivity,
                                "No hay recursos en esta categoría",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }

                        val adapter = RecursoAdapter(this@RecursosActivity, recursos)
                        listView.adapter = adapter

                        listView.setOnItemClickListener { _, _, position, _ ->
                            val recursoSeleccionado = recursos[position]
                            var enlace = recursoSeleccionado.enlace?.trim()

                            Toast.makeText(
                                this@RecursosActivity,
                                "Has pulsado: ${recursoSeleccionado.nombre}",
                                Toast.LENGTH_SHORT
                            ).show()

                            if (enlace.isNullOrBlank()) {
                                Toast.makeText(
                                    this@RecursosActivity,
                                    "Este recurso no tiene enlace disponible",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                if (!enlace.startsWith("http://") && !enlace.startsWith("https://")) {
                                    enlace = "https://$enlace"
                                }

                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(enlace))
                                startActivity(intent)
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@RecursosActivity,
                            "Error al obtener recursos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Recurso>>, t: Throwable) {
                    Toast.makeText(
                        this@RecursosActivity,
                        "Error de conexión",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}