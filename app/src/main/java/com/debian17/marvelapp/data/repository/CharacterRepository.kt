package com.debian17.marvelapp.data.repository

import com.debian17.marvelapp.app.source.CharacterDataSource
import com.debian17.marvelapp.data.model.character.Character
import com.debian17.marvelapp.data.model.character.CharacterInfo
import com.debian17.marvelapp.data.network.CharacterService
import com.debian17.marvelapp.data.network.RequestUtils
import io.reactivex.Single

class CharacterRepository(
    private val characterService: CharacterService,
    private val requestUtils: RequestUtils
) : CharacterDataSource {

    override fun getCharacters(limit: Int, offset: Int): Single<List<Character>> {
        return requestUtils.prepareRequest(characterService.getCharacters(limit, offset)
            .map { it.data.results })
    }

    override fun getCharacterInfo(id: Int): Single<CharacterInfo> {
        return requestUtils.prepareRequest(characterService.getCharacterInfo(id)
            .map { it.data.results.first() })
    }

}