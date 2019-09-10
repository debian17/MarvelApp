package com.debian17.marvelapp.app.di.module

import android.content.Context
import android.net.ConnectivityManager
import com.debian17.marvelapp.data.network.ApiKeyInterceptor
import com.debian17.marvelapp.data.network.CharacterService
import com.debian17.marvelapp.data.network.ApiHelper
import com.debian17.marvelapp.data.network.RequestUtils
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class NetworkModule {

    companion object {
        private const val MARVEL_API_URL = "https://gateway.marvel.com/"
        private const val TIME_OUT = 30L
    }

    @Provides
    fun provideConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Singleton
    @Provides
    fun provideRequestUtils(connectivityManager: ConnectivityManager): RequestUtils {
        return RequestUtils(connectivityManager)
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {

        val apiHelper = ApiHelper()

        val apiKeyInterceptor = ApiKeyInterceptor(apiHelper)

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .addNetworkInterceptor(apiKeyInterceptor)
            .build()

        val gsonConverterFactory = GsonConverterFactory.create()
        val rxJavaCallAdapter = RxJava2CallAdapterFactory.create()

        return Retrofit.Builder()
            .baseUrl(MARVEL_API_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJavaCallAdapter)
            .build()
    }

    @Provides
    fun provideCharacterService(retrofit: Retrofit): CharacterService {
        return retrofit.create(CharacterService::class.java)
    }

}