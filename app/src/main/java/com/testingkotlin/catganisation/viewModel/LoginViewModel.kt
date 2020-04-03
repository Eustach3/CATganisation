package com.testingkotlin.catganisation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    val loadingError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    val usernameError = MutableLiveData<Boolean>()
    val passwordError = MutableLiveData<Boolean>()
    val loginSucceeded = MutableLiveData<Boolean>()


    fun validate(username: String, password: String) {
        checkCredentials(username, password)
    }

    private fun checkCredentials(username: String, password: String) {
        if (username.isBlank()) {
            usernameError.value = true
            return
        }
        usernameError.value = false
        if (password.isBlank()) {
            passwordError.value = true
            return
        }
        passwordError.value = false
        loading.value = true

        //TODO implement api/database check for user access data

        android.os.Handler().postDelayed(
            {
                loading.value = false
                loadingError.value = false
                loginSucceeded.value = true
            }, 2000
        )
    }

}