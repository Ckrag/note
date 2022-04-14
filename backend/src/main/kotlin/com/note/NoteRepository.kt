package com.note

import io.micronaut.context.annotation.Executable
import io.micronaut.data.annotation.*
import io.micronaut.data.repository.CrudRepository
import java.util.*

@Repository
interface NoteRepository : CrudRepository<Note, Long>{

    // TODO: https://micronaut-projects.github.io/micronaut-data/latest/guide/#joinQueries

    @Executable
    fun findByTitle(title: String): List<Note>

}