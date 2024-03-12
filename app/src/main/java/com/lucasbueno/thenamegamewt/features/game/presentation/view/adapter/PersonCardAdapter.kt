package com.lucasbueno.thenamegamewt.features.game.presentation.view.adapter

import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.Target
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.lucasbueno.thenamegamewt.R
import com.lucasbueno.thenamegamewt.databinding.PersonCardBinding
import com.lucasbueno.thenamegamewt.features.game.domain.model.GameDataItem


private const val TEST_GLIDE_URL =
    "https://play-lh.googleusercontent.com/NuJSG_bIoce6kvvtJnULAf34_Rsa1j-HDEt4MWTOrL_3XA51QH9qOQR5UmAYxPI96jA=w600-h300-pc0xffffff-pd"
private const val TEST_WT_URL = "https://namegame.willowtreeapps.com/images/ameir.jpeg"

class PersonCardAdapter : ListAdapter<GameDataItem, PersonCardViewHolder>(GameDataDiffCallback) {

    private var click: (GameDataItem, Int) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonCardViewHolder {
        val binding = PersonCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonCardViewHolder, position: Int) {
        val gameData = getItem(position)
        holder.bind(gameData, click)
    }

    fun itemClicked(action: (GameDataItem, Int) -> Unit) {
        click = action
    }
}

class PersonCardViewHolder(val binding: PersonCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(gameData: GameDataItem, onClick: (GameDataItem, Int) -> Unit) {
        Glide.with(binding.personImageView.context)
            .load(TEST_GLIDE_URL)
            .placeholder(R.drawable.background)
            .error(R.drawable.checkmark_error_ic)
            .into(binding.personImageView)
        binding.root.setOnClickListener { onClick.invoke(gameData, adapterPosition) }
    }
}
