package com.note

import io.micronaut.context.annotation.Bean
import jakarta.inject.Singleton
import jooq.Tables
import jooq.tables.Users
import org.jooq.DSLContext

@Singleton
class UserRepository (
    private val dslCtx: DSLContext
) {
    fun createUser(createdUser: CreateUserDto): User {
        val numOfInsertedRows = dslCtx.insertInto(Tables.USERS, Users.USERS.EMAIL)
            .values(createdUser.email)
            .execute()
        if (numOfInsertedRows != 1) {
            throw DatabaseException("Failed to insert user ${createdUser.email}, ${createdUser.email}. $numOfInsertedRows rows created.")
        }
        return getUserByEmail(createdUser.email)
    }

    fun getUserByEmail(email: String): User {
        return this.dslCtx
            .select(Users.USERS.ID, Users.USERS.EMAIL).from(Users.USERS)
            .where(Users.USERS.EMAIL.eq(email))
            .fetchOneInto(User::class.java) ?: throw NotFoundException("Username $email not found")
    }
}