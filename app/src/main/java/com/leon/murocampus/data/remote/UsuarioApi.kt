package com.leon.murocampus.data.remote
import com.leon.murocampus.model.Usuario
import retrofit2.http.GET
import retrofit2.http.Path
interface UsuarioApi { @GET("users/{id}") suspend fun usuario(@Path("id") id:Int):Usuario }
