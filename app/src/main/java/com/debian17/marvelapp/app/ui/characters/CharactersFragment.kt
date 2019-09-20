package com.debian17.marvelapp.app.ui.characters

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.debian17.marvelapp.App
import com.debian17.marvelapp.R
import com.debian17.marvelapp.app.base.error.ErrorHandler
import com.debian17.marvelapp.app.base.hide
import com.debian17.marvelapp.app.base.longSnackBar
import com.debian17.marvelapp.app.base.show
import com.debian17.marvelapp.app.base.ui.BaseFragment
import com.debian17.marvelapp.app.navigation.MainNavigatorProvider
import com.debian17.marvelapp.app.ui.character_info.CharacterInfoFragment
import com.debian17.marvelapp.app.ui.characters.adapter.CharacterDiffCallback
import com.debian17.marvelapp.app.ui.characters.adapter.CharactersAdapter
import com.debian17.marvelapp.data.model.character.Character
import kotlinx.android.synthetic.main.characters_fragment.*

class CharactersFragment : BaseFragment() {

    companion object {
        private const val DEFAULT_CHARACTER_ID = -1
        private const val CHARACTER_ADDED_ID_KEY = "characterAddedIdKey"
        const val TAG = "CharactersFragmentTag"
        fun newInstance() = CharactersFragment()
    }

    private var characterAddedId = DEFAULT_CHARACTER_ID

    private lateinit var viewModel: CharactersViewModel
    private lateinit var adapter: CharactersAdapter

    private val characterListener = object : CharactersAdapter.CharacterListener {
        override fun onCharacterClick(character: Character) {
            val orientation = resources.configuration.orientation
            val mainNavigator = (activity!! as MainNavigatorProvider).provideMainNavigator()
            val characterInfoFragment = CharacterInfoFragment.newInstance(
                character.id,
                character.name,
                character.image?.getFullPath()
            )
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                mainNavigator.addFragment(
                    characterInfoFragment,
                    R.id.flMainContainer,
                    true,
                    CharacterInfoFragment.TAG
                )
            } else {
                if (characterAddedId != character.id) {
                    characterAddedId = character.id
                    mainNavigator.replaceFragment(
                        characterInfoFragment,
                        R.id.flCharacterInfoContainer,
                        false,
                        CharacterInfoFragment.TAG
                    )
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.characters_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) {
            characterAddedId =
                savedInstanceState.getInt(CHARACTER_ADDED_ID_KEY, DEFAULT_CHARACTER_ID)
        }

        setupRecyclerView()

        val dataComponent = (activity!!.application as App).provideDataComponent()
        val characterDataSource = dataComponent.provideCharacterRepository()
        val viewModelFactory = CharactersViewModel.Factory(characterDataSource)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CharactersViewModel::class.java)

        viewModel.getPagination().observe(viewLifecycleOwner, Observer {
            handlePagination(it)
        })

        viewModel.getLoading().observe(viewLifecycleOwner, Observer {
            handleLoading(it)
        })

        viewModel.getError().observe(viewLifecycleOwner, Observer {
            handleError(it)
        })

        viewModel.characters.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

    }

    private fun setupRecyclerView() {
        val characterDiffCallback = CharacterDiffCallback()
        adapter = CharactersAdapter(context!!, characterDiffCallback, characterListener)

        rvCharacters.apply {
            this.layoutManager = LinearLayoutManager(context!!)
            this.adapter = this@CharactersFragment.adapter
        }
    }

    private fun handlePagination(isPagination: Boolean) {
        if (isPagination) {
            adapter.showPagination()
        } else {
            adapter.hidePagination()
        }
    }

    @Suppress("PLUGIN_WARNING")
    private fun handleLoading(isLoading: Boolean) {
        val orientation = resources.configuration.orientation
        if (isLoading) {
            pbLoading.show()
            rvCharacters.hide()

            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                flCharacterInfoContainer.hide()
            }

        } else {
            rvCharacters.show()
            pbLoading.hide()

            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                flCharacterInfoContainer.show()
            }
        }
    }

    private fun handleError(throwable: Throwable) {
        val errorHandler = ErrorHandler(context!!)
        view?.longSnackBar(errorHandler.getErrorMessage(throwable))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CHARACTER_ADDED_ID_KEY, characterAddedId)
    }

}
