package com.brandon.assignemnt_login_mvvm

enum class UserInfoEntryType {
    CREATE,
    UPDATE
    ;

    companion object {
        fun getEntryType(ordinal: Int?): UserInfoEntryType {
            return UserInfoEntryType.values().firstOrNull {
                it.ordinal == ordinal
            } ?: CREATE
        }
    }
}
