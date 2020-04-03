package com.testingkotlin.catganisation.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.testingkotlin.catganisation.R
import com.testingkotlin.catganisation.viewModel.LoginViewModel
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : AppCompatActivity() {
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        observeViewModel()
        login_button.setOnClickListener{ loginViewModel.validate(user_name.text.toString(), user_password.text.toString()) }

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
}