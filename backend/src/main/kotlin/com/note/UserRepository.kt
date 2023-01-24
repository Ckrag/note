package com.note

import io.micronaut.context.annotation.Bean
import jakarta.inject.Singleton
import jooq.Tables
import jooq.tables.Users
import org.jooq.DSLContext

@Bean
@Singleton
class UserRepository (
    private val dslCtx: DSLContext
) {
    fun createUser(createdUser: CreateUserDto): User {
        val numOfInsertedRows = dslCtx.insertInto(Tables.USERS, Users.USERS.USERNAME, Users.USERS.EMAIL)
            .values(createdUser.username, createdUser.email)
            .execute()
        if (numOfInsertedRows != 1) {
            throw DatabaseException("Failed to insert user ${createdUser.username}, ${createdUser.email}. $numOfInsertedRows rows created.")
        }
        return getUserByUsername(createdUser.username)
    }

    fun getUserByUsername(username: String): User {
        return this.dslCtx
            .select(Users.USERS.ID, Users.USERS.USERNAME, Users.USERS.EMAIL).from(Users.USERS)
            .where(Users.USERS.USERNAME.eq(username))
            .fetchOneInto(User::class.java) ?: throw NotFoundException("Username $username not found")
    }
}