package com.rocio.buencamino
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class CategoriaAdapter(
    private val context: Context,
    private val listaCategorias: List<CategoriaVisual>
) : BaseAdapter() {

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

        return vista
    }
}
