package com.note

import jakarta.inject.Singleton
import jooq.Tables
import jooq.Tables.NOTE_CATEGORY
import jooq.Tables.USER_NOTE_OWNERSHIP
import jooq.enums.NoteAccess
import jooq.tables.Note.*
import jooq.tables.Organization.*
import jooq.tables.Users
import org.jooq.*

@Singleton
class NoteRepository(
    private val dslCtx: DSLContext
) {

    private fun withNoteSelect():
            SelectJoinStep<Record4<Int, String, String, Int>> {
        return dslCtx.select(NOTE.ID, NOTE.TITLE, NOTE.CONTENT, NOTE.CATEGORY_ID).from(Tables.NOTE)
    }

    private fun forOwnership(step: SelectJoinStep<Record4<Int, String, String, Int>>):
            SelectJoinStep<Record4<Int, String, String, Int>> {
        return step.join(USER_NOTE_OWNERSHIP)
            .on(USER_NOTE_OWNERSHIP.NOTE_ID.eq(NOTE.ID))
            .join(Users.USERS).on(Users.USERS.ID.eq(USER_NOTE_OWNERSHIP.USER_ID))
            .join(ORGANIZATION).on(ORGANIZATION.ID.eq(USER_NOTE_OWNERSHIP.ORGANIZATION_ID))
    }

    private fun forCategory(step: SelectJoinStep<Record4<Int, String, String, Int>>):
            SelectJoinStep<Record4<Int, String, String, Int>> {
        return step.join(NOTE_CATEGORY).on(NOTE.CATEGORY_ID.eq(NOTE_CATEGORY.ID))
    }

    private fun whereMatching(
        query: SelectJoinStep<Record4<Int, String, String, Int>>,
        user: User, org: OrganizationDto, category: Category?
    ): SelectConditionStep<Record4<Int, String, String, Int>> {
        val alteredQuery = query.where(Users.USERS.ID.eq(user.id)).and(ORGANIZATION.ID.eq(org.id))
        if (category != null) {
            alteredQuery.and(NOTE_CATEGORY.ID.eq(category.id))
        }
        return alteredQuery
    }

    private fun findById(id: Int): NoteDto {
        return withNoteSelect()
            .where(NOTE.ID.eq(id))
            .fetchOneInto(NoteDto::class.java) ?: throw NotFoundException("No note found for ID $id")
    }

    fun getAllNotesForCategory(user: User, org: OrganizationDto, category: Category): List<NoteDto> {
        val noteSelect = withNoteSelect()
        val noteSelectForCat = forCategory(noteSelect)
        val noteSelectForUser = forOwnership(noteSelectForCat)
        return whereMatching(noteSelectForUser, user, org, category)
            .fetchInto(NoteDto::class.java)
    }

    fun createNote(createNoteDto: CreateNoteDto, user: User, org: OrganizationDto): NoteDto {
        val newNoteId = dslCtx.insertInto(Tables.NOTE, NOTE.TITLE, NOTE.CONTENT, NOTE.CATEGORY_ID, NOTE.AUTHOR_ID)
            .values(createNoteDto.title, createNoteDto.content, createNoteDto.categoryId, user.id)
            .returningResult(NOTE.ID)
            .fetchOneInto(Int::class.java) ?: throw NotFoundException("Unable to fetch newly inserted note")

        val numOfInsertedRows = dslCtx.insertInto(
            USER_NOTE_OWNERSHIP,
            USER_NOTE_OWNERSHIP.USER_ID,
            USER_NOTE_OWNERSHIP.ORGANIZATION_ID,
            USER_NOTE_OWNERSHIP.NOTE_ID,
            USER_NOTE_OWNERSHIP.NOTE_ACCESS
        )
            .values(user.id, org.id, newNoteId, NoteAccess.WRITE)
            .execute()

        if (numOfInsertedRows != 1) {
            throw DatabaseException("Failed to insert ownership of note $newNoteId for user ${user.id} and org ${org.id}")
        }

        return findById(newNoteId)
    }

}