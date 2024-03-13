package com.lucasbueno.thenamegamewt

import com.lucasbueno.thenamegamewt.features.game.domain.model.GameDataItem
import com.lucasbueno.thenamegamewt.features.game.domain.model.Headshot

object GameDataStub {

    val gameDataListStub = listOf<GameDataItem>(
        GameDataItem(
            bio = "Bio 1",
            id = "3WCYqVR963Q4hB7pH9YVxe",
            firstName = "Ameir",
            lastName = "Al-Zoubi",
            jobTitle = "Engenheiro de Software Pessoal",
            slug = "al-zoubi",
            headshot = Headshot(
                id = "79PkYrx56H2EhTJDmIxBTk",
                alt = "ameir al-zoubi",
                mimeType = "imagem/jpeg",
                type = "imagem",
                url = "https://namegame.willowtreeapps.com/images/ameir.jpeg",
                height = 500,
                width = 500
            ),
            socialLinks = emptyList(),
            type = "people"
        ),
        GameDataItem(
            bio = "Bio 2",
            id = "pBcwRVa0782lEA34jjQKn",
            firstName = "Josh",
            lastName = "Amer",
            jobTitle = "Estrategista de produto principal",
            slug = "amer",
            headshot = Headshot(
                id = "6PqIUTySRU5mn39PuPFc1r",
                alt = "Josh Amer",
                mimeType = "imagem/png",
                type = "imagem",
                url = "https://namegame.willowtreeapps.com/images/amer_josh.png",
                height = 1000,
                width = 1000
            ),
            socialLinks = emptyList(),
            type = "people"
        )
    )
}