package com.testingkotlin.catganisation.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.testingkotlin.catganisation.R
import com.testingkotlin.catganisation.di.DaggerApiComponent
import com.testingkotlin.catganisation.model.Breed
import com.testingkotlin.catganisation.model.CatsApi
import com.testingkotlin.catganisation.util.getProgressDrawable
import com.testingkotlin.catganisation.util.loadImage
import com.testingkotlin.catganisation.viewModel.LoginViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.login_activity.*
import java.util.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
    lateinit var loginViewModel: LoginViewModel
    lateinit var imageDisposable : DisposableSingleObserver<List<Breed>>
    @Inject
    lateinit var api: CatsApi
    init {
        DaggerApiComponent.create().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        observeViewModel()
        login_button.setOnClickListener{ loginViewModel.validate(user_name.text.toString(), user_password.text.toString()) }
        setupImage()
    }

    private fun setupImage() {
        // Favorite cats
        val randArray = arrayListOf("abys", "bsho", "chee", "esho", "pixi", "rblu", "sfol")
        val rndNum = Random().nextInt(randArray.size-1)
        val rndCatId = randArray[rndNum]
        imageDisposable = api.getSpecificBreedImage(rndCatId).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<Breed>>() {
                override fun onSuccess(list: List<Breed>) {
                    login_image.loadImage(list[0].url, getProgressDrawable(this@LoginActivity))
                }

                override fun onError(e: Throwable) {

                }
            })

    }

    private fun observeViewModel() {
        loginViewModel.usernameError.observe(this, Observer { error ->
            error?.let {
                if (it) {
                    Toast.makeText(this,"Invalid user name!", Toast.LENGTH_LONG).show()
                }
            }
        })
        loginViewModel.passwordError.observe(this, Observer { error ->
            error?.let {
                if (it) {
                    Toast.makeText(this,"Invalid password!", Toast.LENGTH_LONG).show()
                }
            }
        })
        loginViewModel.loadingError.observe(this, Observer { error ->
            error?.let {
                if (it) {
                    Toast.makeText(this,"Login error!", Toast.LENGTH_LONG).show()
                }
            }
        })
        loginViewModel.loading.observe(this, Observer { loading ->
            loading?.let {
                if (it) {
                    login_button.visibility = View.GONE
                    login_loading.visibility = View.VISIBLE
                }
                else {
                    login_button.visibility = View.VISIBLE
                    login_loading.visibility = View.GONE
                }
            }
        })

        loginViewModel.loginSucceeded.observe(this, Observer { succeeded ->
            succeeded?.let { startActivity(Intent(this, MainActivity::class.java)) }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        imageDisposable.dispose()
    }
}