package com.debian17.marvelapp.app.base.recycler

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import java.lang.RuntimeException

abstract class PaginationDataSource<T>(
    private val initialLoading: MutableLiveData<Boolean>,
    private val pagination: MutableLiveData<Boolean>,
    private val error: MutableLiveData<Throwable>
) : PositionalDataSource<T>() {

    protected abstract fun loadInitialData(
        params: LoadInitialParams,
        callback: LoadInitialCallback<T>
    )

    protected abstract fun loadRangeData(
        params: LoadRangeParams,
        callback: LoadRangeCallback<T>
    )

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<T>) {
        initialLoading.postValue(true)
        try {
            loadInitialData(params, callback)
        } catch (e: RuntimeException) {
            error.postValue(e)
            return
        }
        initialLoading.postValue(false)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<T>) {
        pagination.postValue(true)
        try {
            loadRangeData(params, callback)
        } catch (e: RuntimeException) {
            error.postValue(e)
            return
        }
        pagination.postValue(false)
    }

}