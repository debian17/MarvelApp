package com.debian17.marvelapp.data.network

import android.net.ConnectivityManager
import com.debian17.marvelapp.data.exception.NoNetworkException
import io.reactivex.Completable
import io.reactivex.Single

class RequestUtils(private val connectivityManager: ConnectivityManager) {

    fun <T> prepareRequest(request: Single<T>): Single<T> {
        return Completable.fromCallable {
            if (!isNetworkConnected()) {
                throw NoNetworkException()
            }
        }.andThen(request)
    }

    private fun isNetworkConnected(): Boolean {
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }

}