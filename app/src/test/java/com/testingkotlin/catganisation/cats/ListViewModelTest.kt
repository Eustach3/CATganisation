package com.testingkotlin.catganisation.cats

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.testingkotlin.catganisation.model.Cat
import com.testingkotlin.catganisation.model.CatService
import com.testingkotlin.catganisation.viewModel.CatsListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class ListViewModelTest {
    @get: Rule
    var rule = InstantTaskExecutorRule()


    @Mock
    lateinit var catService: CatService

    @InjectMocks
    var listViewModel = CatsListViewModel()

    private var testSingle: Single<List<Cat>>? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getCatsSuccess() {
        val cat = Cat("nice", "cat", "beautiful", "US", "calm", "www.wiki.com")
        val catList = arrayListOf(cat)
        testSingle = Single.just(catList)
        Mockito.`when`(catService.getCatsBreedList()).thenReturn(testSingle)
        listViewModel.refreshList()

        assertEquals(1, listViewModel.cats.value?.size)
        assertEquals(false, listViewModel.catsLoadingError.value)
        assertEquals(false, listViewModel.catsLoading.value)
    }

    @Test
    fun getCatsError() {
//        val cat = Cat("nice", "cat", "beautiful", "US", "calm", "www.wiki.com")
        testSingle = Single.error(Throwable())
        Mockito.`when`(catService.getCatsBreedList()).thenReturn(testSingle)
        listViewModel.refreshList()

//        assertEquals(1, listViewModel.countries.value?.size)
        assertEquals(true, listViewModel.catsLoadingError.value)
        assertEquals(false, listViewModel.catsLoading.value)
    }


    @Before
    fun setUpRxSchedulers() {
        val immediate = object : Scheduler() {
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() },false)
            }

            override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }

        }
        RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler->immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler-> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler{ scheduler ->immediate}
    }
}