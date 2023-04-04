package com.note

import jakarta.inject.Singleton
import jooq.Tables
import jooq.tables.Note
import jooq.tables.NoteCategory
import org.jooq.DSLContext

@Singleton
class CategoryRepository(
    private val dslCtx: DSLContext
) {

    fun createCategory(category: CreateCategoryDto, user: User, org: OrganizationDto): Category {
        val id = dslCtx.insertInto(
            Tables.NOTE_CATEGORY,
            NoteCategory.NOTE_CATEGORY.TITLE, NoteCategory.NOTE_CATEGORY.DESCRIPTION
        )
            .values(category.title, category.description)
            .returningResult(NoteCategory.NOTE_CATEGORY.ID)
            .fetchOneInto(Int::class.java) ?: throw NotFoundException("Unable to fetch newly inserted note")

        val numOfInsertedRows = dslCtx.insertInto(
            Tables.USER_NOTECATEGORY_OWNERSHIP,
            Tables.USER_NOTECATEGORY_OWNERSHIP.USER_ID, Tables.USER_NOTECATEGORY_OWNERSHIP.ORGANIZATION_ID
        )
            .values(user.id, org.id)
            .execute()

        if (numOfInsertedRows != 1) {
            throw DatabaseException("Failed to insert category ${category.title}, $numOfInsertedRows rows created.")
        }

        return findById(id)
    }

    fun getAllCategories(user: User, org: OrganizationDto): List<Category> {
        return dslCtx.select(
            Tables.NOTE_CATEGORY.ID,
            Tables.NOTE_CATEGORY.TITLE,
            Tables.NOTE_CATEGORY.DESCRIPTION
        )
            .from(Tables.NOTE_CATEGORY)
            .leftJoin(Tables.USER_NOTECATEGORY_OWNERSHIP).on(
                Tables.USER_NOTECATEGORY_OWNERSHIP.USER_ID.eq(user.id).and(
                    Tables.USER_NOTECATEGORY_OWNERSHIP.ORGANIZATION_ID.eq(org.id)
                )
            )
            .groupBy(Tables.NOTE_CATEGORY.ID) // CODE SMELL :D
            .fetchInto(Category::class.java)
    }

    fun getCategoryById(user: User, org: OrganizationDto, categoryId: Int): Category {
        return dslCtx.select(
            Tables.NOTE_CATEGORY.ID,
            Tables.NOTE_CATEGORY.TITLE,
            Tables.NOTE_CATEGORY.DESCRIPTION
        )
            .from(Tables.NOTE_CATEGORY)
            .join(Tables.USER_NOTECATEGORY_OWNERSHIP).on(
                Tables.USER_NOTECATEGORY_OWNERSHIP.USER_ID.eq(user.id).and(
                    Tables.USER_NOTECATEGORY_OWNERSHIP.ORGANIZATION_ID.eq(org.id)
                )
            ).where(Tables.NOTE_CATEGORY.ID.eq(categoryId))
            .groupBy(Tables.NOTE_CATEGORY.ID) // CODE SMELL :D
            .fetchOneInto(Category::class.java) ?: throw NotFoundException("No note category found for ID $categoryId")
    }

    private fun findById(id: Int): Category {
        //TODO: Limit this in scope!
        return dslCtx.select(
            NoteCategory.NOTE_CATEGORY.ID,
            NoteCategory.NOTE_CATEGORY.TITLE,
            NoteCategory.NOTE_CATEGORY.DESCRIPTION
        )
            .from(Tables.NOTE_CATEGORY)
            .where(Tables.NOTE_CATEGORY.ID.eq(id))
            .fetchOneInto(Category::class.java) ?: throw NotFoundException("No note category found for ID $id")
    }


}