package com.lifeSavers.emergencyappsignup.model

data class User(
    val uid: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val birthDate: String? = null,
    val userType: Long? = null,
    val profileImage: String? = null
)