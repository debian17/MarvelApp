package com.debian17.marvelapp.app.base.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    private val subscriptions = CompositeDisposable()

    protected val loading = MutableLiveData<Boolean>()
    protected val error = MutableLiveData<Throwable>()

    protected fun unsubscribeOnClear(disposable: Disposable) {
        subscriptions.add(disposable)
    }

    @CallSuper
    override fun onCleared() {
        subscriptions.clear()
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    fun getError(): LiveData<Throwable> {
        return error
    }

}