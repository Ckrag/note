package com.note

import io.micronaut.context.annotation.Bean
import jakarta.inject.Singleton
import jooq.Tables
import jooq.tables.Organization.ORGANIZATION
import org.jooq.DSLContext

@Bean
@Singleton
class OrganizationRepository(
    private val dslCtx: DSLContext
) {

    fun findByName(name: String): List<OrganizationDto> {
        return this.dslCtx
            .select().from(Tables.ORGANIZATION)
            .where(ORGANIZATION.NAME.eq(name))
            .fetchInto(OrganizationDto::class.java)
    }

    fun findById(id: Int): OrganizationDto {
        return this.dslCtx
            .select().from(Tables.ORGANIZATION)
            .where(ORGANIZATION.ID.eq(id))
            .fetchOneInto(OrganizationDto::class.java) ?: throw NotFoundException("id $id not found")
    }

    fun create(name: String): OrganizationDto {
        val id = dslCtx.insertInto(ORGANIZATION, ORGANIZATION.NAME)
            .values(name)
            .returningResult(ORGANIZATION.ID)
            .execute()
        return findById(id)
    }

}