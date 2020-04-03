package com.testingkotlin.catganisation.di

import androidx.lifecycle.ViewModel
import com.testingkotlin.catganisation.model.CatService
import com.testingkotlin.catganisation.view.CatListAdapter
import com.testingkotlin.catganisation.viewModel.CatsListViewModel
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {
    fun inject(service: CatService)
    fun inject(viewModel: CatsListViewModel)
    fun inject(adapter : CatListAdapter)

}