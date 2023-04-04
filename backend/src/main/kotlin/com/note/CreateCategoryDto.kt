package com.note

import io.micronaut.serde.annotation.Serdeable

@Serdeable
class CreateCategoryDto(
    var title: String,
    var description: String?
)