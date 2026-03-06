package com.rocio.buencamino

interface ApiService {

    @retrofit2.http.GET("api/categorias")
    fun getCategorias(): retrofit2.Call<List<Categoria>>

    @retrofit2.http.GET("api/categorias/{id}/recursos")
    fun getRecursosDeCategoria(@retrofit2.http.Path("id") id: Int): retrofit2.Call<List<Recurso>>
}