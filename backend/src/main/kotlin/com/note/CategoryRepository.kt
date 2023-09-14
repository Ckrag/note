package com.note

import jakarta.inject.Singleton
import jooq.Tables.*
import jooq.tables.NoteCategory
import org.jooq.DSLContext

@Singleton
class CategoryRepository(
    private val dslCtx: DSLContext
) {

    fun createCategory(category: CreateCategoryDto, user: User, org: OrganizationDto): Category {
        val newCategoryId = dslCtx.insertInto(
            NOTE_CATEGORY,
            NoteCategory.NOTE_CATEGORY.TITLE, NoteCategory.NOTE_CATEGORY.DESCRIPTION
        )
            .values(category.title, category.description)
            .returningResult(NoteCategory.NOTE_CATEGORY.ID)
            .fetchOneInto(Int::class.java) ?: throw NotFoundException("Unable to fetch newly inserted note")
        println("Created category: ${category.title}, with id $newCategoryId")
        val numOfInsertedRows = dslCtx.insertInto(
            USER_NOTECATEGORY_OWNERSHIP,
            USER_NOTECATEGORY_OWNERSHIP.USER_ID, USER_NOTECATEGORY_OWNERSHIP.ORGANIZATION_ID,
            USER_NOTECATEGORY_OWNERSHIP.CATEGORY_ID
        )
            .values(user.id, org.id, newCategoryId)
            .execute()

        if (numOfInsertedRows != 1) {
            throw DatabaseException("Failed to insert category ${category.title}, $numOfInsertedRows rows created.")
        }

        return findById(newCategoryId)
    }

    fun getAllCategories(user: User, org: OrganizationDto): List<Category> {
        return dslCtx.select(
            NOTE_CATEGORY.ID,
            NOTE_CATEGORY.TITLE,
            NOTE_CATEGORY.DESCRIPTION
        )
            .from(NOTE_CATEGORY)
            .join(USER_NOTECATEGORY_OWNERSHIP).on(
                USER_NOTECATEGORY_OWNERSHIP.CATEGORY_ID.eq(NOTE_CATEGORY.ID)
            )
            .where(
                USER_NOTECATEGORY_OWNERSHIP.ORGANIZATION_ID.eq(org.id)
                    .and(USER_NOTECATEGORY_OWNERSHIP.USER_ID.eq(user.id))
            ).fetchInto(Category::class.java)
    }

    fun getCategoryById(user: User, org: OrganizationDto, categoryId: Int): Category {
        return dslCtx.select(
            NOTE_CATEGORY.ID,
            NOTE_CATEGORY.TITLE,
            NOTE_CATEGORY.DESCRIPTION
        )
            .from(NOTE_CATEGORY)
            .join(USER_NOTECATEGORY_OWNERSHIP).on(
                USER_NOTECATEGORY_OWNERSHIP.USER_ID.eq(user.id).and(
                    USER_NOTECATEGORY_OWNERSHIP.ORGANIZATION_ID.eq(org.id)
                )
            ).where(NOTE_CATEGORY.ID.eq(categoryId))
            .groupBy(NOTE_CATEGORY.ID) // CODE SMELL :D
            .fetchOneInto(Category::class.java) ?: throw NotFoundException("No note category found for ID $categoryId")
    }

    private fun findById(id: Int): Category {
        //TODO: Limit this in scope!
        return dslCtx.select(
            NoteCategory.NOTE_CATEGORY.ID,
            NoteCategory.NOTE_CATEGORY.TITLE,
            NoteCategory.NOTE_CATEGORY.DESCRIPTION
        )
            .from(NOTE_CATEGORY)
            .where(NOTE_CATEGORY.ID.eq(id))
            .fetchOneInto(Category::class.java) ?: throw NotFoundException("No note category found for ID $id")
    }


}