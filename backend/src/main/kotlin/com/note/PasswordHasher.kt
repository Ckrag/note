package com.note

import at.favre.lib.crypto.bcrypt.BCrypt
import jakarta.inject.Singleton

@Singleton
class PasswordHasher {

    fun encode(rawPassword: String): String {
        val hash = BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray());
        return hash
    }

    fun matches(rawPassword: String, encodedPassword: String): Boolean {
        val result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword)
        return result.verified
    }

}