package com.rocio.buencamino

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("api/categorias")
    fun getCategorias(): Call<List<Categoria>>

    @GET("api/categorias/{id}/recursos")
    fun getRecursosDeCategoria(@Path("id") id: Int): Call<List<Recurso>>
}