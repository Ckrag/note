package com.note

import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import java.security.Principal
import java.util.*
import javax.validation.Valid

@Validated
@Singleton
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/org/{orgId}/note/")
class NoteController(
    private val noteManagement: NoteManagementService
) {

    @Produces(MediaType.APPLICATION_JSON)
    @Get(value = "/all")
    fun getNotes(authentication: Authentication): HttpResponse<List<NoteDto>> {
        return HttpResponse.ok(this.noteManagement.getAllNotes(authentication))
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Post(value = "/find")
    fun getNoteByTitle(@QueryValue title: String, authentication: Authentication): HttpResponse<List<NoteDto>> {
        return HttpResponse.ok(this.noteManagement.findNotesByTitle(title, authentication).toList())
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Post(value = "/create")
    fun createNote(@Body @Valid note: CreateNoteDto, authentication: Authentication, orgId: Int): HttpResponse<NoteDto> {
        val saved = this.noteManagement.createNote(note, authentication, orgId)
        return HttpResponse.created(saved)
    }
}