package com.debian17.marvelapp.app.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
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
        private const val SPAN_COUNT = 2
        const val TAG = "CharactersFragmentTag"
        fun newInstance() = CharactersFragment()
    }

    private lateinit var viewModel: CharactersViewModel
    private lateinit var adapter: CharactersAdapter

    private val spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val viewType = adapter.getItemViewType(position)
            if (viewType == R.layout.item_character) {
                return 1
            } else {
                return 2
            }
        }
    }

    private val characterListener = object : CharactersAdapter.CharacterListener {
        override fun onCharacterClick(character: Character) {

            val characterInfoFragment = CharacterInfoFragment.newInstance(
                character.id,
                character.name,
                character.image?.getFullPath()
            )

            val mainNavigator = (activity!! as MainNavigatorProvider).provideMainNavigator()
            mainNavigator.addFragment(characterInfoFragment, true, CharacterInfoFragment.TAG)
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

        setupRecyclerView()

        val dataComponent = (activity!!.applicationContext as App).provideDataComponent()
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

        val layoutManager = GridLayoutManager(context, SPAN_COUNT).apply {
            spanSizeLookup = this@CharactersFragment.spanSizeLookup
        }
        rvCharacters.apply {
            this.layoutManager = layoutManager
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

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            pbLoading.show()
            rvCharacters.hide()
        } else {
            rvCharacters.show()
            pbLoading.hide()
        }
    }

    private fun handleError(throwable: Throwable) {
        val errorHandler = ErrorHandler(context!!)
        view?.longSnackBar(errorHandler.getErrorMessage(throwable))
    }

}
