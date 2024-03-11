package com.lucasbueno.thenamegamewt.features.game.data.model


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@Serializable
//data class Headshot(
//    @SerialName("alt")
//    val alt: String,
//    @SerialName("height")
//    val height: Int,
//    @SerialName("id")
//    val id: String,
//    @SerialName("mimeType")
//    val mimeType: String,
//    @SerialName("type")
//    val type: String,
//    @SerialName("url")
//    val url: String,
//    @SerialName("width")
//    val width: Int
//)

data class Headshot(
    @SerializedName("alt")
    val alt: String?,
    @SerializedName("height")
    val height: Int?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("mimeType")
    val mimeType: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("width")
    val width: Int?
)