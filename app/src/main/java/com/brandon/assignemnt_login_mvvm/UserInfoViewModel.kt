package com.brandon.assignemnt_login_mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brandon.assignemnt_login_mvvm.SignUpErrorMessage.*
import com.brandon.assignemnt_login_mvvm.SignupValidationExtension.includeSpecialCharacters
import com.brandon.assignemnt_login_mvvm.SignupValidationExtension.includeUpperCase
import com.brandon.assignemnt_login_mvvm.SignupValidationExtension.isIncludeAt
import com.brandon.assignemnt_login_mvvm.SignupValidationExtension.isValidEmailProvider

class UserInfoViewModel() : ViewModel() {

    private val TAG = "ViewModel"

    // 유저 입력 정보
    val username = MutableLiveData<String>()
    val userEmail = MutableLiveData<String>()
    val userEmailProvider = MutableLiveData<String>()
    val userPassword = MutableLiveData<String>()
    val userPasswordConfirm = MutableLiveData<String>()

    // 에러 메시지 상태
    private val _usernameErrorMessage = MutableLiveData<StringValue>()
    val usernameErrorMessage: LiveData<StringValue> get() = _usernameErrorMessage
    private val _userEmailErrorMessage = MutableLiveData<StringValue>()
    val userEmailErrorMessage: LiveData<StringValue> get() = _userEmailErrorMessage
    private val _userEmailProviderErrorMessage = MutableLiveData<StringValue>()
    val userEmailProviderErrorMessage: LiveData<StringValue> get() = _userEmailProviderErrorMessage
    private val _userPasswordErrorMessage = MutableLiveData<StringValue>()
    val userPasswordErrorMessage: LiveData<StringValue> get() = _userPasswordErrorMessage
    private val _userPasswordConfirmErrorMessage = MutableLiveData<StringValue>()
    val userPasswordConfirmErrorMessage: LiveData<StringValue> get() = _userPasswordConfirmErrorMessage

    private val _spinnerData = MutableLiveData<List<Int>>()
    val spinnerData: LiveData<List<Int>> get() = _spinnerData

    val spinnerSelectedItemPosition = MutableLiveData<Int>()

    private val _confirmButtonState = MutableLiveData<Boolean>()
    val confirmButtonState: LiveData<Boolean> get() = _confirmButtonState

    val emailProvider
        get() = listOf(
            R.string.sign_up_email_provider_direct,
            R.string.sign_up_email_provider_gmail,
            R.string.sign_up_email_provider_kakao,
            R.string.sign_up_email_provider_naver
        )

    companion object {
        fun MutableLiveData<String>.updateStringData(newData: String) {
            this.value = newData
        }
    }

    init {
        loadSpinnerData()
    }

    fun getSpinnerSelectedProvider(position: Int?): StringValue {
        if (position == null) return StringValue.Empty
        return StringValue.StringResource(emailProvider[position])
    }

    fun updateSpinnerItem() {
        when (spinnerSelectedItemPosition.value) {
            0 -> StringValue.Empty
            1 -> StringValue.StringResource(R.string.sign_up_email_provider_gmail)
            2 -> StringValue.StringResource(R.string.sign_up_email_provider_kakao)
            3 -> StringValue.StringResource(R.string.sign_up_email_provider_naver)
            else -> StringValue.Empty
        }
    }

    fun getMessageValidName(input: String) {
        val errorMessage = if (input.isBlank()) {
            StringValue.StringResource(NAME.message)
        } else {
            StringValue.Empty
        }
        _usernameErrorMessage.value = errorMessage
        updateConfirmButtonState()
    }

    fun getMessageValidEmail(input: String) {
        val errorMessage = when {
            input.isBlank() -> StringValue.StringResource(EMAIL_BLANK.message)
            input.isIncludeAt() -> StringValue.StringResource(EMAIL_AT.message)
            else -> StringValue.Empty
        }
        _userEmailErrorMessage.value = errorMessage
        updateConfirmButtonState()
    }

    fun getMessageValidEmailProvider(input: String, isVisible: Boolean) {
        if (userEmail.value?.isBlank() == null) return
        val errorMessage = if (isVisible && (input.isBlank())) {
            StringValue.StringResource(EMAIL_PROVIDER_ERROR_BLANK.message)
        } else if (isVisible && (input.isValidEmailProvider().not())) {
            StringValue.StringResource(EMAIL_PROVIDER_ERROR_INVALID.message)
        } else {
            StringValue.Empty
        }
        _userEmailProviderErrorMessage.value = errorMessage
        updateConfirmButtonState()
    }

    fun getMessageValidPassword(input: String) {
        val errorMessage = when {
            input.isEmpty() -> StringValue.StringResource(PASSWORD_HINT.message)
            input.length < 5 -> StringValue.StringResource(PASSWORD_LENGTH.message)
            input.includeSpecialCharacters()
                .not() -> StringValue.StringResource(PASSWORD_SPECIAL_CHARACTERS.message)

            input.includeUpperCase()
                .not() -> StringValue.StringResource(PASSWORD_UPPER_CASE.message)

            else -> StringValue.Empty
        }
        _userPasswordErrorMessage.value = errorMessage
        updateConfirmButtonState()
    }

    fun getMessageValidPasswordConfirm(input: String) {
        val errorMessage = if (input == userPassword.value.toString()) {
            StringValue.Empty
        } else {
            StringValue.StringResource(PASSWORD_PASSWORD.message)
        }
        _userPasswordConfirmErrorMessage.value = errorMessage
        updateConfirmButtonState()
    }

    private fun updateConfirmButtonState() {
        val value = usernameErrorMessage.value?.isEmpty() == true &&
                userEmailErrorMessage.value?.isEmpty() == true &&
                userEmailProviderErrorMessage.value?.isEmpty() == true &&
                userPasswordErrorMessage.value?.isEmpty() == true &&
                userPasswordConfirmErrorMessage.value?.isEmpty() == true
        _confirmButtonState.value = value
    }

    private fun loadSpinnerData() {
        _spinnerData.value = this.emailProvider
    }
}
