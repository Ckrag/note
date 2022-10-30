package com.note

import jakarta.inject.Inject
import jooq.Tables
import org.jooq.DSLContext


class NoteRepository {

    @Inject
    lateinit var dslCtx: DSLContext

    fun findByTitle(title: String): List<Note> {
        return this.dslCtx
            .select().from(Tables.NOTE)
            .where(jooq.tables.Note.NOTE.TITLE.eq(title))
            .fetchInto(Note::class.java)
    }

}