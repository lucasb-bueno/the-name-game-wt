package com.lucasbueno.thenamegamewt.features.game.data.model


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@Serializable
//data class SocialLink(
//    @SerialName("callToAction")
//    val callToAction: String,
//    @SerialName("type")
//    val type: String,
//    @SerialName("url")
//    val url: String
//)

data class SocialLink(
    @SerializedName("callToAction")
    val callToAction: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("url")
    val url: String?
)