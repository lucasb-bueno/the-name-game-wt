package com.lucasbueno.thenamegamewt.features.game.presentation.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.lucasbueno.thenamegamewt.features.game.domain.model.GameDataItem

object GameDataDiffCallback : DiffUtil.ItemCallback<GameDataItem>() {
    override fun areItemsTheSame(oldItem: GameDataItem, newItem: GameDataItem): Boolean {
        // You need to provide a unique identifier for each item and use it here.
        // For example, if each GameDataItem has a unique id.
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GameDataItem, newItem: GameDataItem): Boolean {
        // Here you can compare the full contents of the items to decide whether or not they are equal.
        // This is usually a field-by-field comparison.
        return oldItem == newItem
    }
}
