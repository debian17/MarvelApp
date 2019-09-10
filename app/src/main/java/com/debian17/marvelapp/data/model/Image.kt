package com.debian17.marvelapp.data.model

import com.google.gson.annotations.SerializedName

class Image(
    @SerializedName("path") val path: String?,
    @SerializedName("extension") val extension: String?
) {
    companion object {
        private const val VARIANT = "portrait_incredible"
    }

    fun getFullPath(): String? {
        return if (path != null && extension != null) {
            "$path/$VARIANT.$extension"
        } else {
            null
        }
    }

}