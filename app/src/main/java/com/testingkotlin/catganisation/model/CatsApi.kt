package com.testingkotlin.catganisation.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CatsApi {
    @GET("/v1/breeds")
    fun getBreedList() : Single<List<Cat>>

    @Headers("x-api-key: 45aefa4c-2312-48e9-b41c-dd64eb0c14a8")
    @GET("/v1/images/search?breed_id=")
    fun getSpecificBreedImage( @Query("breeId") breeId: String) :Single<List<Breed>>

}