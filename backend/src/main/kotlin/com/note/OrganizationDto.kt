package com.note

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class OrganizationDto(
    val id: Int,
    val name: String,
)