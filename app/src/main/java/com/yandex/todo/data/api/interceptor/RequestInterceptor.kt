package com.yandex.todo.data.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .header("Authorization", "Bearer e6232113c1064f50a7e73221524a357d")
            .build()

        return chain.proceed(request)
    }
}