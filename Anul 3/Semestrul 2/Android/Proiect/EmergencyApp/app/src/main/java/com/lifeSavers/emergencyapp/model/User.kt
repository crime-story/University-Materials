package com.lifeSavers.emergencyapp.model

data class User(
    var uid: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val birthDate: String? = null,
    val userType: Long? = null,
    val profileImage: String? = null,
    val deviceToken: String? = null,
)