package com.debian17.marvelapp.app.ui.character_info

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.debian17.marvelapp.App

import com.debian17.marvelapp.R
import com.debian17.marvelapp.app.base.error.ErrorHandler
import com.debian17.marvelapp.app.base.hide
import com.debian17.marvelapp.app.base.longSnackBar
import com.debian17.marvelapp.app.base.show
import com.debian17.marvelapp.app.base.ui.BaseFragment
import com.debian17.marvelapp.app.ui.characters.CharactersFragment
import kotlinx.android.synthetic.main.character_info_fragment.*

class CharacterInfoFragment : BaseFragment() {

    companion object {
        private const val CHARACTER_ID_KEY = "characterIdKey"
        private const val CHARACTER_NAME_KEY = "characterNameKey"
        private const val IMAGE_URL_KEY = "imageUrlKey"
        const val TAG = "CharacterInfoFragmentTag"
        fun newInstance(
            characterId: Int,
            characterName: String?,
            imageUrl: String?
        ): CharacterInfoFragment {
            return CharacterInfoFragment().apply {
                arguments = Bundle().apply {
                    putInt(CHARACTER_ID_KEY, characterId)
                    putString(CHARACTER_NAME_KEY, characterName)
                    putString(IMAGE_URL_KEY, imageUrl)
                }
            }
        }
    }

    private lateinit var viewModel: CharacterInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.character_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val characterId = arguments!!.getInt(CHARACTER_ID_KEY)
        val characterName = arguments!!.getString(CHARACTER_NAME_KEY)
        val imageUrl = arguments!!.getString(IMAGE_URL_KEY)

        val dataComponent = (activity!!.application as App).provideDataComponent()
        val characterDataSource = dataComponent.provideCharacterRepository()
        val viewModelFactory = CharacterInfoViewModel.Factory(characterId, characterDataSource)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CharacterInfoViewModel::class.java)

        toolbar.title = characterName ?: getString(R.string.description_not_found)
        loadCharacterAvatar(imageUrl)

        viewModel.getLoading().observe(viewLifecycleOwner, Observer {
            if (it) {
                pbLoading.show()
                tvCharacterDescription.hide()
            } else {
                tvCharacterDescription.show()
                pbLoading.hide()
            }
        })

        viewModel.getCharacterInfo().observe(viewLifecycleOwner, Observer {
            val description = it.description
            if (description != null && description.isNotEmpty()) {
                tvCharacterDescription.text = description
            } else {
                tvCharacterDescription.text = getString(R.string.description_not_found)
            }
        })

        viewModel.getError().observe(viewLifecycleOwner, Observer {
            val errorHandler = ErrorHandler(context!!)
            view?.longSnackBar(errorHandler.getErrorMessage(it))
        })

    }

    private fun loadCharacterAvatar(imageUrl: String?) {
        Glide.with(ivCharacterAvatar)
            .load(imageUrl)
            .into(ivCharacterAvatar)
    }

}
