package com.debian17.marvelapp.data.network

import com.debian17.marvelapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val apiHelper: ApiHelper) : Interceptor {

    companion object {
        private const val TIME_STAMP = "ts"
        private const val API_KEY = "apikey"
        private const val HASH = "hash"
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        val url = request.url()

        val timeStamp = apiHelper.getTimeStamp()
        val hash = apiHelper.getMD5(timeStamp)

        val newUrl = url.newBuilder()
            .addQueryParameter(TIME_STAMP, timeStamp)
            .addQueryParameter(API_KEY, BuildConfig.MARVEL_API_KEY)
            .addQueryParameter(HASH, hash)
            .build()

        val newRequest = request.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)

    }

}