package com.rocio.buencamino

import android.content.Intent
import android.os.Bundle
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val lista = findViewById<GridView>(R.id.gridViewCategorias)

        // Bottom navigation
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        bottomNavigation.selectedItemId = R.id.nav_home

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
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

        RetrofitClient.api.getCategorias().enqueue(object : Callback<List<Categoria>> {

            override fun onResponse(call: Call<List<Categoria>>, response: Response<List<Categoria>>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "OK: " + (response.body()?.size ?: 0) + " categorias",
                        Toast.LENGTH_LONG
                    ).show()

                    val categoriasApi = response.body() ?: emptyList()

                    val categoriasVisuales = categoriasApi.map { categoria ->
                        CategoriaVisual(
                            nombre = categoria.nombre,
                            imagen = when (categoria.nombre.lowercase()) {
                                "lactancia" -> R.drawable.categoria_lactancia
                                "matronas" -> R.drawable.categoria_matronas
                                "libros" -> R.drawable.categoria_libros
                                "pediatras" -> R.drawable.categoria_pediatras
                                "fisioterapia" -> R.drawable.categoria_fisioterapia
                                "calzado respetuoso" -> R.drawable.categoria_calzado
                                "porteo" -> R.drawable.categoria_porteo
                                "podcasts" -> R.drawable.categoria_podcasts
                                else -> R.drawable.categoria_lactancia
                            },
                            id = categoria.id
                        )
                    }

                    val adaptador = CategoriaAdapter(this@MainActivity, categoriasVisuales)
                    lista.adapter = adaptador

                    lista.setOnItemClickListener { _, _, position, _ ->
                        val categoria = categoriasVisuales[position]

                        val intent = Intent(this@MainActivity, RecursosActivity::class.java)
                        intent.putExtra("categoria_id", categoria.id)
                        intent.putExtra("categoria_nombre", categoria.nombre)
                        startActivity(intent)
                    }

                } else {
                    Toast.makeText(this@MainActivity, "Error al cargar categorías", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Categoria>>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "FALLO: " + (t.message ?: "sin mensaje"),
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}