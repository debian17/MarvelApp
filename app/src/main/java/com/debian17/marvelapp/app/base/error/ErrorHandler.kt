package com.debian17.marvelapp.app.base.error

import android.content.Context
import com.debian17.marvelapp.R
import com.debian17.marvelapp.data.exception.NoNetworkException

class ErrorHandler(private val context: Context) {

    fun getErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is NoNetworkException -> context.getString(R.string.no_network_connection)
            else -> throwable.message ?: context.getString(R.string.unknown_error)
        }
    }

}