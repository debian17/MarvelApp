package com.debian17.marvelapp.app.ui.main

import android.os.Bundle
import com.debian17.marvelapp.R
import com.debian17.marvelapp.app.base.ui.BaseActivity
import com.debian17.marvelapp.app.navigation.MainNavigator
import com.debian17.marvelapp.app.navigation.MainNavigatorProvider
import com.debian17.marvelapp.app.ui.characters.CharactersFragment

class MainActivity : BaseActivity(), MainNavigatorProvider {

    private lateinit var mainNavigator: MainNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainNavigator = MainNavigator(supportFragmentManager, R.id.flMainContainer)

        if (savedInstanceState == null) {
            val charactersFragment = CharactersFragment.newInstance()
            mainNavigator.addFragment(charactersFragment, false, CharactersFragment.TAG)
        }

    }

    override fun provideMainNavigator(): MainNavigator {
        return mainNavigator
    }

}
