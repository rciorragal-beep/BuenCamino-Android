package com.rocio.buencamino

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var categoriaId: Int = -1
    private var categoriaNombre: String = "Mapa"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        categoriaId = intent.getIntExtra("categoria_id", -1)
        categoriaNombre = intent.getStringExtra("categoria_nombre") ?: "Mapa"

        title = "Mapa - $categoriaNombre"

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val madrid = LatLng(40.4168, -3.7038)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(madrid, 12f))

        if (categoriaId == -1) {
            Toast.makeText(
                this,
                "Primero tienes que entrar en una categoría",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        cargarRecursosDeCategoria(categoriaId)
    }

    private fun cargarRecursosDeCategoria(categoriaId: Int) {
        RetrofitClient.api.getRecursosDeCategoria(categoriaId)
            .enqueue(object : Callback<List<Recurso>> {

                override fun onResponse(
                    call: Call<List<Recurso>>,
                    response: Response<List<Recurso>>
                ) {
                    if (response.isSuccessful) {
                        val recursos = response.body()

                        if (recursos.isNullOrEmpty()) {
                            Toast.makeText(
                                this@MapaActivity,
                                "No hay recursos en esta categoría",
                                Toast.LENGTH_LONG
                            ).show()
                            return
                        }

                        var contadorMarcadores = 0
                        var primeraPosicion: LatLng? = null

                        for (recurso in recursos) {
                            if (recurso.latitud != null && recurso.longitud != null) {
                                val posicion = LatLng(recurso.latitud, recurso.longitud)

                                if (primeraPosicion == null) {
                                    primeraPosicion = posicion
                                }

                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(posicion)
                                        .title(recurso.nombre)
                                        .snippet(recurso.descripcion)
                                )

                                contadorMarcadores++
                            }
                        }

                        if (contadorMarcadores > 0 && primeraPosicion != null) {
                            mMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(primeraPosicion, 13f)
                            )
                        } else {
                            Toast.makeText(
                                this@MapaActivity,
                                "Esta categoría no tiene ubicaciones todavía",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@MapaActivity,
                            "Error al cargar recursos",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Recurso>>, t: Throwable) {
                    Toast.makeText(
                        this@MapaActivity,
                        "Error de conexión: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}