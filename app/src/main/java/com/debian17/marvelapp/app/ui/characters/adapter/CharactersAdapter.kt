package com.debian17.marvelapp.app.ui.characters.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.debian17.marvelapp.R
import com.debian17.marvelapp.app.base.recycler.PaginationAdapter
import com.debian17.marvelapp.data.model.character.Character

class CharactersAdapter(
    context: Context,
    diffCallback: CharacterDiffCallback,
    private val characterListener: CharacterListener
) : PaginationAdapter<Character>(context, diffCallback) {

    interface CharacterListener {
        fun onCharacterClick(character: Character)
    }

    override fun getViewTypeForItem(position: Int): Int {
        return R.layout.item_character
    }

    override fun onCreateViewHolderForItem(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_character, parent, false)
        val holder = CharacterViewHolder(view)
        holder.itemView.setOnClickListener {
            val character = getItem(holder.adapterPosition)
            if (character != null) {
                characterListener.onCharacterClick(character)
            }
        }
        return holder
    }

    override fun onBindViewHolderForItem(holder: RecyclerView.ViewHolder, position: Int) {
        val character = getItem(position)
        if (character != null && holder is CharacterViewHolder) {
            holder.bind(character)
        }
    }

    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivCharacterAvatar: ImageView = itemView.findViewById(R.id.ivCharacterAvatar)
        private val tvCharacterName: TextView = itemView.findViewById(R.id.tvCharacterName)

        fun bind(character: Character) {
            Glide.with(ivCharacterAvatar)
                .load(character.image?.getFullPath())
                .into(ivCharacterAvatar)

            tvCharacterName.text = character.name
        }
    }
}