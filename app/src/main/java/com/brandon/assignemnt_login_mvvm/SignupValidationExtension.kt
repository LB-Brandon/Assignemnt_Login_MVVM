package com.brandon.assignemnt_login_mvvm

object SignupValidationExtension {

    /**
     * @return true if "@" is present, false otherwise.
     */
    fun String.includeAt() = this.contains("@")



}