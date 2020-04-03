package com.testingkotlin.catganisation.model

import com.google.gson.annotations.SerializedName

data class Breed(
    @SerializedName("id")
    val id : String,
    @SerializedName("url")
    val url : String
)