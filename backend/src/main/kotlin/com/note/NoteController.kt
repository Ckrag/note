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
class NoteController {

    @Inject
    lateinit var noteRepo: NoteRepository

    @Produces(TEXT_PLAIN)
    @Get
    fun index(principal: Principal): String = principal.name

    @Get(value = "/all")
    fun getNotes(): HttpResponse<List<Note>> {
        return HttpResponse.ok(this.noteRepo.findAll().toList())
    }

    @Get
    fun getNoteByTitle(@QueryValue title: String): HttpResponse<List<Note>> {
        return HttpResponse.ok(this.noteRepo.findByTitle(title).toList())
    }

    @Get(value = "/{id}")
    fun getNoteByTitle(@PathVariable id: Long): Optional<Note> {
        return this.noteRepo.findById(id) ?: Optional.empty<Note>()
    }

    @Post
    fun createNote(@Body @Valid note: Note): HttpResponse<Note> {
        val saved = this.noteRepo.save(note)
        return HttpResponse.created(saved)
    }
}