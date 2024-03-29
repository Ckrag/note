package com.note

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class UserDto(
    val id: Int,
    val email: String,
)