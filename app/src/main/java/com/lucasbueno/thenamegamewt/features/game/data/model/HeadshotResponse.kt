package com.lucasbueno.thenamegamewt.features.game.data.model


import com.google.gson.annotations.SerializedName

data class HeadshotResponse(
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