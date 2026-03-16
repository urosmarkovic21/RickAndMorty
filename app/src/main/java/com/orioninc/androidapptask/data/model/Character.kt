package com.orioninc.androidapptask.data.model

import android.media.Image

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String
)