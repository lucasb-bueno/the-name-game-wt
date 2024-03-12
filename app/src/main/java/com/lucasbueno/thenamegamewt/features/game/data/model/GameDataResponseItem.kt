package com.lucasbueno.thenamegamewt.features.game.data.model


import com.google.gson.annotations.SerializedName
import com.lucasbueno.thenamegamewt.features.game.domain.model.GameDataItem
import com.lucasbueno.thenamegamewt.features.game.domain.model.Headshot
import com.lucasbueno.thenamegamewt.features.game.domain.model.SocialLink

data class GameDataResponseItem(
    @SerializedName("bio")
    val bio: String?,
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("headshot")
    val headshotResponse: HeadshotResponse?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("jobTitle")
    val jobTitle: String,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("slug")
    val slug: String?,
    @SerializedName("socialLinks")
    val socialLinkResponses: List<SocialLinkResponse>?,
    @SerializedName("type")
    val type: String?
)

fun GameDataResponseItem.toDomainModel(): GameDataItem {
    return GameDataItem(
        bio = this.bio,
        firstName = this.firstName,
        headshot = this.headshotResponse?.toDomainModel(),
        id = this.id,
        jobTitle = this.jobTitle,
        lastName = this.lastName,
        slug = this.slug,
        socialLinks = this.socialLinkResponses?.map { it.toDomainModel() },
        type = this.type
    )
}

fun HeadshotResponse.toDomainModel(): Headshot {
    return Headshot(
        alt = this.alt,
        height = this.height,
        id = this.id,
        mimeType = this.mimeType,
        type = this.type,
        url = this.url,
        width = this.width
    )
}

fun SocialLinkResponse.toDomainModel(): SocialLink {
    return SocialLink(
        callToAction = this.callToAction,
        type = this.type,
        url = this.url
    )
}