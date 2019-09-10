package com.debian17.marvelapp.app.ui.character_info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.debian17.marvelapp.app.base.observeOnUI
import com.debian17.marvelapp.app.base.subscribeOnIO
import com.debian17.marvelapp.app.base.viewmodel.BaseViewModel
import com.debian17.marvelapp.app.source.CharacterDataSource
import com.debian17.marvelapp.data.model.character.CharacterInfo


class CharacterInfoViewModel(
    private val characterId: Int,
    private val characterDataSource: CharacterDataSource
) : BaseViewModel() {

    private val characterInfo = MutableLiveData<CharacterInfo>()

    init {
        loading.value = true
        unsubscribeOnClear(
            characterDataSource.getCharacterInfo(characterId)
                .subscribeOnIO()
                .observeOnUI()
                .subscribe(this::onCharacterInfoLoaded, this::onError)
        )
    }

    private fun onCharacterInfoLoaded(characterInfo: CharacterInfo) {
        loading.value = false
        this.characterInfo.value = characterInfo
    }

    private fun onError(throwable: Throwable) {
        error.value = throwable
    }

    fun getCharacterInfo(): LiveData<CharacterInfo> {
        return characterInfo
    }

    class Factory(private val id: Int, private val characterDataSource: CharacterDataSource) :
        ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CharacterInfoViewModel(id, characterDataSource) as T
        }

    }

}
