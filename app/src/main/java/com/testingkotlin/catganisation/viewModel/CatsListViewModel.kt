package com.testingkotlin.catganisation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.testingkotlin.catganisation.di.DaggerApiComponent
import com.testingkotlin.catganisation.model.Cat
import com.testingkotlin.catganisation.model.CatService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CatsListViewModel : ViewModel() {

    @Inject
    lateinit var catService: CatService
    val compDisposable = CompositeDisposable()
    val cats= MutableLiveData<List<Cat>>()
    val catsLoading = MutableLiveData<Boolean>()
    val catsLoadingError = MutableLiveData<Boolean>()

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun refreshList() {
        fetchCatList()
    }

    private fun fetchCatList() {
        catsLoading.value = true
        compDisposable.add(
            catService.getCatsBreedList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Cat>>() {
                    override fun onSuccess(list: List<Cat>) {
                        cats.value = list
                        catsLoading.value = false
                        catsLoadingError.value = false
                    }

                    override fun onError(e: Throwable) {
                        catsLoadingError.value = true
                        catsLoading.value = false
                    }

                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compDisposable.clear()
    }
}