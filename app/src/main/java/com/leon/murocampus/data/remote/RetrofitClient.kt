package com.leon.murocampus.data.remote
import com.leon.murocampus.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
object RetrofitClient {
    private val json=Json { ignoreUnknownKeys=true }
    private val cliente=OkHttpClient.Builder()
        .addInterceptor(AcceptJsonInterceptor())
        .apply { if(BuildConfig.DEBUG)addInterceptor(HttpLoggingInterceptor().apply { level=HttpLoggingInterceptor.Level.BODY }) }
        .connectTimeout(15,TimeUnit.SECONDS).readTimeout(20,TimeUnit.SECONDS).callTimeout(30,TimeUnit.SECONDS)
        .build()
    private val retrofit by lazy {
        Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com/")
            .client(cliente).addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
    val api:PostApi by lazy { retrofit.create(PostApi::class.java) }
    val usuarioApi:UsuarioApi by lazy { retrofit.create(UsuarioApi::class.java) }
}
