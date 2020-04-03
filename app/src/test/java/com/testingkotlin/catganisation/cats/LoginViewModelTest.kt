package com.testingkotlin.catganisation.cats

import android.content.Context
import android.widget.EditText
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.testingkotlin.catganisation.model.Cat
import com.testingkotlin.catganisation.model.CatService
import com.testingkotlin.catganisation.viewModel.CatsListViewModel
import com.testingkotlin.catganisation.viewModel.LoginViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import junit.framework.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class LoginViewModelTest {
    @get: Rule
    var rule = InstantTaskExecutorRule()



    private var testSingle: Single<List<Boolean>>? = null

    @InjectMocks
    var loginViewModel = LoginViewModel()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getUserNameError() {
        val userName = ""
        val password = "abc"
        loginViewModel.validate(userName, password)
        Assert.assertEquals(true, loginViewModel.usernameError.value)
    }

    @Test
    fun getPasswordError() {
        val userName = "abc"
        val password = ""
        loginViewModel.validate(userName, password)
        Assert.assertEquals(true, loginViewModel.passwordError.value)
    }

    @Test
    fun getLoadingError() {
        testSingle = Single.error(Throwable())
        //TODO after login implementation we should get loginViewModel.loadingError true on this test
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