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

private const val HANDLER_DELAY = 1000L

class PersonCardAdapter(
    private val listener: OnItemClickListener,
) : ListAdapter<GameDataItem, PersonCardAdapter.PersonCardViewHolder>(GameDataDiffCallback) {

    private var correctAnswerId: String = ""
    private var isCorrectAnswer = false

    interface OnItemClickListener {
        fun onItemClick(position: Int, gameData: GameDataItem)
    }

    fun setCorrectAnswerId(name: String) {
        correctAnswerId = name
    }

    fun isCorrectAnswer(isCorrect: Boolean) {
        isCorrectAnswer = isCorrect
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonCardViewHolder {
        val binding = PersonCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonCardViewHolder, position: Int) {
        val gameData = getItem(position)
        holder.bind(gameData)
    }

    inner class PersonCardViewHolder(val binding: PersonCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    listener.onItemClick(position, item)
                }
            }
        }

        fun bind(gameData: GameDataItem) {
            Glide.with(binding.personImageView.context)
                .load(gameData.headshot?.url)
                .placeholder(R.drawable.background)
                .error(R.drawable.checkmark_error_ic)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.e("GlideError", "Load failed for " + model + " with error: ", e)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .dontAnimate().into(binding.personImageView)
//                .into(binding.personImageView)


            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition, gameData)

                binding.overlayFrame.visibility = View.VISIBLE

                binding.overlayFrame.apply {
                    visibility = View.VISIBLE
                    setBackgroundColor(
                        if (isCorrectAnswer) ContextCompat.getColor(context, R.color.checkmark_green)
                        else ContextCompat.getColor(context, R.color.checkmark_red)
                    )
                }

                binding.checkmarkImageView.setImageResource(
                    if (isCorrectAnswer) R.drawable.checkmark_ic
                    else R.drawable.checkmark_error_ic
                )

                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    binding.overlayFrame.visibility = View.GONE
                }, HANDLER_DELAY)
            }
        }
    }
}
