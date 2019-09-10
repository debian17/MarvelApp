package com.debian17.marvelapp.app.di.component

import com.debian17.marvelapp.app.di.module.RepositoryModule
import com.debian17.marvelapp.app.source.CharacterDataSource
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class])
interface DataComponent {

    fun provideCharacterRepository(): CharacterDataSource

}