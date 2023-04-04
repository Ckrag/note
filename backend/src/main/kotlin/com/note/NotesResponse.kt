package com.note

import com.fasterxml.jackson.annotation.JsonInclude
import io.micronaut.serde.annotation.Serdeable

@Serdeable
class NotesResponse(
    @JsonInclude(JsonInclude.Include.ALWAYS)
    var notes: List<NoteDto>,
)
