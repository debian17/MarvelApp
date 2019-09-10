package com.debian17.marvelapp.data.model.base

import com.google.gson.annotations.SerializedName

class DataContainer<T>(
    @SerializedName("results")
    val results: List<T>
)