package com.debian17.marvelapp.app.navigation

import androidx.fragment.app.FragmentManager
import com.debian17.marvelapp.app.base.ui.BaseFragment

class MainNavigator(
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) {

    fun addFragment(fragment: BaseFragment, addToBackStack: Boolean, tag: String?) {
        val transaction = fragmentManager.beginTransaction()
            .add(containerId, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.commit()
    }

}