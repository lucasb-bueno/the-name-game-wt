package com.lucasbueno.thenamegamewt.utils

import androidx.recyclerview.widget.DiffUtil
import com.lucasbueno.thenamegamewt.features.game.domain.model.GameDataItem

object DiffUtilCallback : DiffUtil.ItemCallback<GameDataItem>() {
    override fun areItemsTheSame(oldItem: GameDataItem, newItem: GameDataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GameDataItem, newItem: GameDataItem): Boolean {
        return oldItem == newItem
    }
}
