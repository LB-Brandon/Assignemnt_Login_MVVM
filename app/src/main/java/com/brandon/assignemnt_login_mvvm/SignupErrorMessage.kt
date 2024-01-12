package com.brandon.assignemnt_login_mvvm

import androidx.annotation.StringRes

enum class SignUpErrorMessage(
    @StringRes val message: Int
) {
    NAME(R.string.sign_up_name_error),

    EMAIL_BLANK(R.string.sign_up_email_error_blank),
    EMAIL_AT(R.string.sign_up_email_error_at),
    EMAIL_SERVICE_PROVIDER(R.string.sign_up_email_error_provider),
    EMAIL_SERVICE_PROVIDER_ERROR(R.string.sign_up_email_provider_error),

    PASSWORD_HINT(R.string.sign_up_password_hint),
    PASSWORD_LENGTH(R.string.sign_up_password_error_length),
    PASSWORD_SPECIAL_CHARACTERS(R.string.sign_up_password_error_special),
    PASSWORD_UPPER_CASE(R.string.sign_up_password_error_upper),
    PASSWORD_PASSWORD(R.string.sign_up_confirm_error),

    PASS(R.string.sign_up_pass)
}