package com.rocio.buencamino

data class Recurso(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val enlace: String?,
    val categoria_id: Int
)