package com.note

import io.micronaut.serde.annotation.Serdeable
@Serdeable
class Category(
    var id: Int,
    var title: String,
    var description: String?
)