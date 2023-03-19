package com.note

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class NoteDto(
    var id: Int,
    var title: String,
    var content: String,
                //var category: Category?,
                //var tags: List<Tag>
)

