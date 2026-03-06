package com.rocio.buencamino

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val lista = findViewById<ListView>(R.id.listCategorias)

        RetrofitClient.api.getCategorias().enqueue(object : Callback<List<Categoria>> {

            override fun onResponse(call: Call<List<Categoria>>, response: Response<List<Categoria>>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "OK: " + (response.body()?.size ?: 0) + " categorias",
                        Toast.LENGTH_LONG
                    ).show()
                    val categoriasApi = response.body() ?: emptyList()
                    val nombres = categoriasApi.map { it.nombre }

                    val adaptador = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_list_item_1,
                        nombres
                    )
                    lista.adapter = adaptador

                    lista.setOnItemClickListener { _, _, position, _ ->
                        val categoria = categoriasApi[position]

                        val intent = android.content.Intent(this@MainActivity, RecursosActivity::class.java)
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
