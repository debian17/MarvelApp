package com.debian17.marvelapp.app.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class MainNavigator(private val fragmentManager: FragmentManager) {

    fun addFragment(
        fragment: Fragment, @IdRes containerId: Int,
        addToBackStack: Boolean,
        tag: String?
    ) {
        val transaction = fragmentManager.beginTransaction()
            .add(containerId, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.commit()
    }

    fun replaceFragment(
        fragment: Fragment,
        @IdRes containerId: Int,
        addToBackStack: Boolean,
        tag: String?
    ) {
        val transaction = fragmentManager.beginTransaction()
            .replace(containerId, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.commit()
    }

}