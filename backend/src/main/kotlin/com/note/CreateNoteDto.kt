package com.note

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class CreateNoteDto(
    val title: String,
    val content: String,
    @JsonProperty("category_id") val categoryId: Int?,
                //var tags: List<Int?>
)

