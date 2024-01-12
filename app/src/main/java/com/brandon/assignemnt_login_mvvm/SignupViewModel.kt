package com.brandon.assignemnt_login_mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brandon.assignemnt_login_mvvm.SignupValidationExtension.includeAt

class SignupViewModel() : ViewModel() {

    private val TAG = "ViewModel"

    val username = MutableLiveData<String>()
    val userEmail = MutableLiveData<String>()
    val userPassword = MutableLiveData<String>()
    val userPasswordConfirm = MutableLiveData<String>()

    private val _usernameErrorMessage = MutableLiveData<StringValue>()
    val usernameErrorMessage: LiveData<StringValue> get() = _usernameErrorMessage
    private val _userEmailErrorMessage = MutableLiveData<StringValue>()
    val userEmailErrorMessage: LiveData<StringValue> get() = _userEmailErrorMessage

    private val _userPasswordErrorMessage = MutableLiveData<String>()
    val userPasswordErrorMessage: LiveData<String> get() = _userPasswordErrorMessage
    private val _userPasswordConfirmErrorMessage = MutableLiveData<String>()
    val userPasswordConfirmErrorMessage: LiveData<String> get() = _userPasswordConfirmErrorMessage

    fun getMessageValidName(input: String) {
        val errorMessage = if (input.isBlank()) {
            StringValue.StringResource(R.string.sign_up_name_error)
        } else {
            StringValue.StringResource(R.string.sign_up_pass)
        }
        _usernameErrorMessage.postValue(errorMessage)
    }

    fun getMessageValidEmail(input: String) {
        val errorMessage = when {
            input.isBlank() -> StringValue.StringResource(R.string.sign_up_email_error_blank)
            input.includeAt() -> StringValue.StringResource(R.string.sign_up_email_error_at)
            else -> StringValue.StringResource(R.string.sign_up_pass)
        }
        _userEmailErrorMessage.postValue(errorMessage)
    }

}
