package com.note

import jakarta.inject.Singleton
import jooq.Tables
import jooq.Tables.USER_NOTE_OWNERSHIP
import jooq.enums.NoteAccess
import jooq.tables.Note.*
import jooq.tables.Organization.*
import jooq.tables.Users
import org.jooq.DSLContext
import org.jooq.Record3
import org.jooq.SelectJoinStep
import org.jooq.SelectWhereStep

@Singleton
class NoteRepository (
    private val dslCtx: DSLContext
) {

    private fun withNoteSelect():
            SelectJoinStep<Record3<Int, String, String>> {
        return dslCtx.select(NOTE.ID, NOTE.TITLE, NOTE.CONTENT).from(Tables.NOTE)
    }

    private fun forOwnership(step: SelectJoinStep<Record3<Int, String, String>>):
            SelectWhereStep<Record3<Int, String, String>> {
        return step.join(USER_NOTE_OWNERSHIP)
                .on(USER_NOTE_OWNERSHIP.NOTE_ID.eq(NOTE.ID))
            .join(Users.USERS).on(Users.USERS.ID.eq(USER_NOTE_OWNERSHIP.USER_ID))
            .join(ORGANIZATION).on(ORGANIZATION.ID.eq(USER_NOTE_OWNERSHIP.ORGANIZATION_ID))
    }

    fun findByTitleByUser(title: String, user: User): List<NoteDto> {
        println("Looking for notes with title '$title'")
        val noteSelect = withNoteSelect()
        val noteSelectForUser = forOwnership(noteSelect)
        return noteSelectForUser
            .where(Users.USERS.ID.eq(user.id)).and(NOTE.TITLE.eq(title))
            .fetchInto(NoteDto::class.java)
    }

    fun findById(id: Int): NoteDto {
        return withNoteSelect()
            .where(NOTE.ID.eq(id))
            .fetchOneInto(NoteDto::class.java) ?: throw NotFoundException("No note found for ID $id")
    }

    fun getAllWriteableByUser(user: User): List<NoteDto> {
        val noteSelect = withNoteSelect()
        val noteSelectForUser = forOwnership(noteSelect)
        return noteSelectForUser
            .where(Users.USERS.ID.eq(user.id))
            .fetchInto(NoteDto::class.java)
    }

    fun getAllReadableByUser(user: User): List<NoteDto> {
        val noteSelect = withNoteSelect()
        val noteSelectForUser = forOwnership(noteSelect)
        return noteSelectForUser
            .where(Users.USERS.ID.eq(user.id))
            .fetchInto(NoteDto::class.java)
    }

    fun createNote(createNoteDto: CreateNoteDto, user: User, org: OrganizationDto): NoteDto{
        val newNoteId = dslCtx.insertInto(Tables.NOTE, NOTE.TITLE, NOTE.CONTENT)
            .values(createNoteDto.title, createNoteDto.content)
            .returningResult(NOTE.ID)
            .fetchOneInto(Int::class.java) ?: throw NotFoundException("Unable to fetch newly inserted note")

        val numOfInsertedRows = dslCtx.insertInto(
            USER_NOTE_OWNERSHIP,
            USER_NOTE_OWNERSHIP.USER_ID, USER_NOTE_OWNERSHIP.ORGANIZATION_ID, USER_NOTE_OWNERSHIP.NOTE_ID, USER_NOTE_OWNERSHIP.NOTE_ACCESS)
            .values(user.id, org.id, newNoteId, NoteAccess.WRITE)
            .execute()

        if (numOfInsertedRows != 1) {
            throw DatabaseException("Failed to insert ownership of note $newNoteId for user ${user.id} and org ${org.id}")
        }

        return findById(newNoteId)
    }

}