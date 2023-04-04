package com.note

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import java.lang.Exception
import java.util.*
import javax.validation.Valid

@Validated
@Singleton
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/org/{orgId}/note/")
class NoteController(
    private val noteService: NoteService
) {

    @Produces(MediaType.APPLICATION_JSON)
    @Get(value = "/all")
    fun getNotes(authentication: Authentication, orgId: Int): HttpResponse<List<NoteDto>> {
        throw Exception("NOT SAFE, MISSING ORG")
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Post(value = "/find")
    fun getNoteByTitle(@QueryValue title: String, authentication: Authentication, orgId: Int): HttpResponse<List<NoteDto>> {
        throw Exception("NOT SAFE, MISSING ORG")
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Get(value = "/category/{categoryId}")
    fun getNotesByCategory(@QueryValue categoryId: Int, authentication: Authentication, orgId: Int): HttpResponse<NotesResponse> {
        return HttpResponse.ok(NotesResponse(this.noteService.findNotesByCategory(authentication, orgId, categoryId)))
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Post(value = "/create")
    fun createNote(@Body @Valid note: CreateNoteDto, authentication: Authentication, orgId: Int): HttpResponse<NoteDto> {
        val saved = this.noteService.createNote(note, authentication, orgId)
        return HttpResponse.created(saved)
    }
}