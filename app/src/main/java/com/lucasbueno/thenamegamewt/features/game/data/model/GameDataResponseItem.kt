package com.lucasbueno.thenamegamewt.features.game.data.model


import com.google.gson.annotations.SerializedName
import com.lucasbueno.thenamegamewt.features.game.domain.model.GameDataItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@Serializable
//data class GameDataResponseItem(
//    @SerialName("bio")
//    val bio: String,
//    @SerialName("firstName")
//    val firstName: String,
//    @SerialName("headshot")
//    val headshot: Headshot,
//    @SerialName("id")
//    val id: String,
//    @SerialName("jobTitle")
//    val jobTitle: String,
//    @SerialName("lastName")
//    val lastName: String,
//    @SerialName("slug")
//    val slug: String,
//    @SerialName("socialLinks")
//    val socialLinks: List<SocialLink>,
//    @SerialName("type")
//    val type: String
//)

data class GameDataResponseItem(
    @SerializedName("bio")
    val bio: String?,
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("headshot")
    val headshot: Headshot?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("jobTitle")
    val jobTitle: String,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("slug")
    val slug: String?,
    @SerializedName("socialLinks")
    val socialLinks: List<SocialLink>?,
    @SerializedName("type")
    val type: String?
)

fun GameDataResponseItem.toGameDataItem(): GameDataItem {
    return GameDataItem(
        bio = this.bio,
        firstName = this.firstName,
        headshot = this.headshot?.toDomainModel(),
        id = this.id,
        jobTitle = this.jobTitle,
        lastName = this.lastName,
        slug = this.slug,
        socialLinks = this.socialLinks?.map { it.toDomainModel() },
        type = this.type
    )
}

fun Headshot.toDomainModel(): com.lucasbueno.thenamegamewt.features.game.domain.model.Headshot {
    return com.lucasbueno.thenamegamewt.features.game.domain.model.Headshot(
        alt = this.alt,
        height = this.height,
        id = this.id,
        mimeType = this.mimeType,
        type = this.type,
        url = this.url,
        width = this.width
    )
}

fun SocialLink.toDomainModel(): com.lucasbueno.thenamegamewt.features.game.domain.model.SocialLink {
    return com.lucasbueno.thenamegamewt.features.game.domain.model.SocialLink(
        callToAction = this.callToAction,
        type = this.type,
        url = this.url
    )
}