package com.lucasbueno.thenamegamewt.features.game.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lucasbueno.thenamegamewt.R
import com.lucasbueno.thenamegamewt.databinding.PersonCardBinding
import com.lucasbueno.thenamegamewt.features.game.domain.model.GameDataItem
import com.lucasbueno.thenamegamewt.utils.DiffUtilCallback

class GameScreenAdapter : ListAdapter<GameDataItem, GameScreenViewHolder>(DiffUtilCallback) {

    private var click: (GameDataItem, Int) -> Unit = {_, _ ->}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameScreenViewHolder {
        val binding = PersonCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameScreenViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameScreenViewHolder, position: Int) {
        val gameData = getItem(position)
        holder.bind(gameData, click)
    }

    fun itemClicked(action: (GameDataItem, Int) -> Unit) {
        click = action
    }
}

class GameScreenViewHolder(val binding: PersonCardBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(gameDataItem: GameDataItem, onClick: (GameDataItem, Int) -> Unit) {
        Glide.with(binding.personImageView.context)
            .load(gameDataItem.headshot?.url)
            .error(R.drawable.checkmark_error_ic)
            .into(binding.personImageView)
        binding.root.setOnClickListener {
            onClick.invoke(gameDataItem, adapterPosition)
        }
    }
}