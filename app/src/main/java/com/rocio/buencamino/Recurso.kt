package com.rocio.buencamino

data class Recurso(
    val id_recurso: Int,
    val nombre: String,
    val descripcion: String,
    val enlace: String,
    val categoria_id: Int,
    val imagen: String?,
    val latitud: Double?,
    val longitud: Double?
)