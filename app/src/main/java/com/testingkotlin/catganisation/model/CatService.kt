package com.testingkotlin.catganisation.model

import com.testingkotlin.catganisation.di.DaggerApiComponent
import io.reactivex.Single
import javax.inject.Inject

class CatService {

    @Inject
    lateinit var api: CatsApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getCatsBreedList() : Single<List<Cat>> {
        return api.getBreedList()
    }
}