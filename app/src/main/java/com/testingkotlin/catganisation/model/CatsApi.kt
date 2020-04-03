package com.testingkotlin.catganisation.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import javax.security.auth.callback.Callback

interface CatsApi {
    @GET("/v1/breeds")
    fun getBreedList() : Single<List<Cat>>

    @Headers("x-api-key: 45aefa4c-2312-48e9-b41c-dd64eb0c14a8")
    @GET("/v1/images/search?breed_id=beng")
    fun getSpecificBreedImage() : Single<List<Breed>>
//    @Headers("x-api-key: 45aefa4c-2312-48e9-b41c-dd64eb0c14a8")
//    @GET("/v1/images/search?breed_id={breeId}")
//    fun getSpecificBreedImage( @Path("breeId") breeId: String) : List<Breed>

}