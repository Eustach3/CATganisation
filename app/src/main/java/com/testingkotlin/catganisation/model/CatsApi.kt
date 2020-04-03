package com.testingkotlin.catganisation.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import javax.security.auth.callback.Callback

interface CatsApi {
    @GET("/v1/breeds")
    fun getBreedList() : Single<List<Cat>>

    @GET("/images/search?breed_id={BREED_ID}")
    fun getSpecificBreedImage( @Path("breedId") breeId: String) : String

}