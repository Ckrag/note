package com.note

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class CreateUserDto(
    val email: String,
    @JsonProperty("raw_password") val rawPassword: String,
)