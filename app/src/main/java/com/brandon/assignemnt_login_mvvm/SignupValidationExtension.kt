package com.brandon.assignemnt_login_mvvm

object SignupValidationExtension {

    /**
     * @return true if "@" is present, false otherwise.
     */
    fun String.isIncludeAt() = this.contains("@")

    fun String.isValidEmailProvider() = Regex("[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}").matches(this)

    fun String.includeSpecialCharacters() = Regex("[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+").containsMatchIn(this)

    fun String.includeUpperCase() = Regex("[A-Z]").containsMatchIn(this)

}