package com.note

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces
import io.micronaut.http.annotation.QueryValue
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import java.util.Optional
import javax.validation.Valid

@Validated
@Controller("/note")
class NoteController {

    @Inject
    lateinit var noteRepo: NoteRepository

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