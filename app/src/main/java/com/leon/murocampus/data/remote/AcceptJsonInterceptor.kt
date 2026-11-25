package com.leon.murocampus.data.remote

import okhttp3.Interceptor
import okhttp3.Response

/** Contrato compartido por GET, POST, PATCH y DELETE. No se registran credenciales. */
class AcceptJsonInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(
        chain.request().newBuilder().header("Accept", "application/json").build()
    )
}
