package com.testingkotlin.catganisation.di

import com.testingkotlin.catganisation.model.CatService
import com.testingkotlin.catganisation.model.CatsApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {
    private val BASE_URL : String = "https://api.thecatapi.com"

    @Provides
    fun catsApi() : CatsApi {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build().create(CatsApi::class.java)
    }

    @Provides
    fun getCatsService() : CatService {
        return CatService()
    }
}