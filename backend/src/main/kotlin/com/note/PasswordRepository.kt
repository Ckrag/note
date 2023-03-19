package com.note

import jakarta.inject.Singleton
import jooq.Tables
import jooq.tables.Auth
import org.jooq.DSLContext


@Singleton
class PasswordRepository(
    private val dslCtx: DSLContext,
) {
    fun saveUserPassword(user: User, password: String) {
        val numOfInsertedRows = dslCtx.insertInto(Tables.AUTH, Auth.AUTH.ID, Auth.AUTH.ENCRYPTED_PASSWORD)
            .values(user.id, password)
            .execute()
        if (numOfInsertedRows != 1) {
            throw DatabaseException("Failed to insert user ${user.email}. $numOfInsertedRows rows created.")
        }
    }

    fun getUserPassword(user: User): String {
        return this.dslCtx
            .select(Auth.AUTH.ENCRYPTED_PASSWORD)
            .from(Auth.AUTH)
            .where(Auth.AUTH.ID.eq(user.id))
            .fetchOneInto(String::class.java) ?: throw NotFoundException("No password found for user ${user.id}")
    }
}