package com.rocio.buencamino

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class RecursoAdapter(
    private val context: Context,
    private val listaRecursos: List<Recurso>
) : BaseAdapter() {

    override fun getCount(): Int {
        return listaRecursos.size
    }

    override fun getItem(position: Int): Any {
        return listaRecursos[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_recurso, parent, false)

        val imgRecurso = view.findViewById<ImageView>(R.id.imgRecurso)
        val txtNombre = view.findViewById<TextView>(R.id.txtNombreRecurso)
        val txtDescripcion = view.findViewById<TextView>(R.id.txtDescripcionRecurso)
        val txtAbrirEnlace = view.findViewById<TextView>(R.id.txtAbrirEnlace)

        val recurso = listaRecursos[position]

        txtNombre.text = recurso.nombre

        txtDescripcion.text = if (recurso.descripcion.isNullOrBlank()) {
            "Sin descripción disponible"
        } else {
            recurso.descripcion
        }

        txtAbrirEnlace.visibility = if (recurso.enlace.isNullOrBlank()) {
            View.GONE
        } else {
            View.VISIBLE
        }

        val imagenResId = when (recurso.nombre.lowercase()) {
            "embarazo consciente" -> R.drawable.libro_embarazo_consciente
            "lo hago como madremente puedo" -> R.drawable.libro_madremente_puedo
            "parto en casa" -> R.drawable.libro_parto_en_casa
            "nacer importa" -> R.drawable.libro_nacer_importa
            "nazareth olivera" -> R.drawable.libro_nazareth_olivera
            "álvaro bilbao" -> R.drawable.libro_alvaro_bilbao
            "alvaro bilbao" -> R.drawable.libro_alvaro_bilbao
            "carlos gonzález" -> R.drawable.libro_carlos_gonzalez
            "carlos gonzalez" -> R.drawable.libro_carlos_gonzalez
            "maría couso" -> R.drawable.libro_maria_couso
            "maria couso" -> R.drawable.libro_maria_couso
            "rosa jové" -> R.drawable.libro_rosa_jove
            "rosa jove" -> R.drawable.libro_rosa_jove
            else -> null
        }

        if (imagenResId != null) {
            imgRecurso.visibility = View.VISIBLE
            imgRecurso.setImageResource(imagenResId)
        } else {
            imgRecurso.visibility = View.GONE
        }

        return view
    }
}