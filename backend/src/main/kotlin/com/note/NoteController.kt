package com.note

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import java.security.Principal
import java.util.*
import javax.validation.Valid

@Validated
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/note")
class NoteController(
    private val noteRepo: NoteRepository
) {

    @Produces(TEXT_PLAIN)
    @Get
    fun index(principal: Principal): String = principal.name

    @Get(value = "/all")
    fun getNotes(): HttpResponse<List<NoteDto>> {
        return HttpResponse.ok(this.noteRepo.getAll())
    }

    @Get
    fun getNoteByTitle(@QueryValue title: String): HttpResponse<List<NoteDto>> {
        return HttpResponse.ok(this.noteRepo.findByTitle(title).toList())
    }

    @Get(value = "/{id}")
    fun getNoteByTitle(@PathVariable id: Long): Optional<NoteDto> {
        //return this.noteRepo.findById(id) ?: Optional.empty<Note>()
        return Optional.empty()
    }

    @Post
    fun createNote(@Body @Valid note: NoteDto): HttpResponse<NoteDto> {
        //val saved = this.noteRepo.save(note)
        //return HttpResponse.created(saved)
        return HttpResponse.created(note)
    }
}