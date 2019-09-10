package com.debian17.marvelapp.app.base

import android.view.View
import android.view.animation.AlphaAnimation
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

const val ANIMATION_DURATION = 350L

//View
fun View.show() {
    if (visibility != View.VISIBLE) {
        val alphaAnimation = AlphaAnimation(0f, 1f).apply {
            duration = ANIMATION_DURATION
        }
        startAnimation(alphaAnimation)
        visibility = View.VISIBLE
    }
}

fun View.hide() {
    if (visibility != View.GONE) {
        val alphaAnimation = AlphaAnimation(1f, 0f).apply {
            duration = ANIMATION_DURATION
        }
        startAnimation(alphaAnimation)
        visibility = View.GONE
    }
}

fun View.longSnackBar(text: String?) {
    if (text != null && text.isNotEmpty()) {
        Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()
    }
}

//Rx
fun <T> Single<T>.subscribeOnIO() = subscribeOn(Schedulers.io())

fun <T> Single<T>.observeOnUI() = observeOn(AndroidSchedulers.mainThread())