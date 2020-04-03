package com.testingkotlin.catganisation.model

import com.google.gson.annotations.SerializedName

data class Cat(
    @SerializedName("id")
    val id : String, val name : String,
    @SerializedName("description")
    val description: String,
    @SerializedName("country_code")
    val countryCode: String,
    @SerializedName("temperament")
    val temperament: String,
    @SerializedName("wikipedia_url")
    val wikipediaLink: String)