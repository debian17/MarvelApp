package com.debian17.marvelapp.app.ui.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.debian17.marvelapp.app.base.viewmodel.BaseViewModel
import com.debian17.marvelapp.app.source.CharacterDataSource
import com.debian17.marvelapp.app.ui.characters.source.CharactersSourceFactory
import java.util.concurrent.Executors


class CharactersViewModel(characterDataSource: CharacterDataSource) : BaseViewModel() {

    companion object {
        private const val LIMIT = 20
        private const val PREFETCH_DISTANCE = 2
    }

    private val pagination = MutableLiveData<Boolean>()

    private val charactersSourceFactory =
        CharactersSourceFactory(characterDataSource, loading, pagination, error)

    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(LIMIT)
        .setPrefetchDistance(PREFETCH_DISTANCE)
        .build()

    val characters = LivePagedListBuilder(charactersSourceFactory, config)
        .setFetchExecutor(Executors.newSingleThreadExecutor())
        .build()

    fun getPagination(): LiveData<Boolean> {
        return pagination
    }

    class Factory(private val characterDataSource: CharacterDataSource) :
        ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CharactersViewModel(characterDataSource) as T
        }

    }

}
