package com.note

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class CreateNoteDto(
                var title: String,
                var content: String,
                //var category: Category?,
                //var tags: List<Tag>
)

