package com.lucasbueno.thenamegamewt.features.game.domain.model

data class GameDataItem(
    val bio: String?,
    val firstName: String?,
    val headshot: Headshot?,
    val id: String?,
    val jobTitle: String?,
    val lastName: String?,
    val slug: String?,
    val socialLinks: List<SocialLink>?,
    val type: String?
)