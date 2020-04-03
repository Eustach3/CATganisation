package com.testingkotlin.catganisation.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cat(
    @SerializedName("id")
    val id : String,
    @SerializedName("name")
    val name : String,
    @SerializedName("description")
    val description: String,
    @SerializedName("country_code")
    val countryCode: String,
    @SerializedName("temperament")
    val temperament: String,
    @SerializedName("wikipedia_url")
    val wikipediaLink: String) : Parcelable