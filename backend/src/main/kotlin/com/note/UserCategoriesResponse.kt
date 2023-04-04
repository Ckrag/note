package com.note

import com.fasterxml.jackson.annotation.JsonInclude
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class UserCategoriesResponse(
    @JsonInclude(JsonInclude.Include.ALWAYS)
    var categories: List<Category>
)
