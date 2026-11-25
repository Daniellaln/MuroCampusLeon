package com.leon.murocampus.data.remote
import com.leon.murocampus.model.*
import retrofit2.http.*
interface PostApi {
    @GET("posts") suspend fun pagina(@Query("_start") inicio:Int,@Query("_limit") limite:Int,@Query("userId") usuario:Int?=null):List<Post>
    @GET("posts") suspend fun listar():List<Post>
    @GET("posts/{id}") suspend fun detalle(@Path("id") id:Int):Post
    @GET("posts/{id}/comments") suspend fun comentarios(@Path("id") id:Int):List<Comentario>
    @GET("posts") suspend fun porUsuario(@Query("userId") userId:Int):List<Post>
    @DELETE("posts/{id}") suspend fun eliminar(@Path("id") id:Int):retrofit2.Response<Unit>
    @PATCH("posts/{id}") suspend fun editar(@Path("id") id:Int,@Body cambios:EditarPost):Post
    @POST("posts") suspend fun crear(@Body nuevo:NuevoPost):Post
}
