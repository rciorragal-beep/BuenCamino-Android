package com.rocio.buencamino
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter

class CategoriaAdapter(
    private val context: Context,
    private val listaCategorias: List<CategoriaVisual>
) : BaseAdapter() {

    private val categoriasEnConstruccion = setOf(
        "Lactancia",
        "Calzado respetuoso",
        "Fisioterapia",
        "Podcasts",
        "Porteo",
        "Pediatras"
    )

    override fun getCount(): Int {
        return listaCategorias.size
    }

    override fun getItem(position: Int): Any {
        return listaCategorias[position]
    }

    override fun getItemId(position: Int): Long {
        return listaCategorias[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val vista = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_categoria, parent, false)

        val imgCategoria = vista.findViewById<ImageView>(R.id.imgCategoria)
        val txtCategoria = vista.findViewById<TextView>(R.id.txtCategoria)

        val categoria = listaCategorias[position]

        imgCategoria.setImageResource(categoria.imagen)
        txtCategoria.text = categoria.nombre

        if (categoria.nombre in categoriasEnConstruccion) {
            val matrix = ColorMatrix()
            matrix.setSaturation(0f)
            imgCategoria.colorFilter = ColorMatrixColorFilter(matrix)
        } else {
            imgCategoria.clearColorFilter()
        }

        return vista
    }
}
