package com.lucasbueno.thenamegamewt.features.game.data.model


import com.google.gson.annotations.SerializedName

data class SocialLinkResponse(
    @SerializedName("callToAction")
    val callToAction: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("url")
    val url: String?
)