package com.note

import io.micronaut.context.annotation.Bean
import jakarta.inject.Singleton
import jooq.Tables
import org.jooq.DSLContext

@Bean
@Singleton
class NoteRepository (
    private val dslCtx: DSLContext
) {

    fun findByTitle(title: String): List<NoteDto> {
        return this.dslCtx
            .select().from(Tables.NOTE)
            .where(jooq.tables.Note.NOTE.TITLE.eq(title))
            .fetchInto(NoteDto::class.java)
    }

    fun getAll(): List<NoteDto> {
        return this.dslCtx
            .select().from(Tables.NOTE)
            .fetchInto(NoteDto::class.java)
    }

}