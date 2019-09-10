package com.debian17.marvelapp.app.di.module

import com.debian17.marvelapp.app.source.CharacterDataSource
import com.debian17.marvelapp.data.network.CharacterService
import com.debian17.marvelapp.data.network.RequestUtils
import com.debian17.marvelapp.data.repository.CharacterRepository
import dagger.Module
import dagger.Provides

@Module(includes = [NetworkModule::class])
class RepositoryModule {

    @Provides
    fun provideCharacterRepository(
        characterService: CharacterService,
        requestUtils: RequestUtils
    ): CharacterDataSource {
        return CharacterRepository(characterService, requestUtils)
    }

}