package com.note

import jakarta.inject.Singleton
import jooq.Tables
import jooq.enums.OrganizationRole
import jooq.tables.Organization.ORGANIZATION
import jooq.tables.UserOrganizationMembership.USER_ORGANIZATION_MEMBERSHIP
import org.jooq.DSLContext

@Singleton
class OrganizationMembershipRepository(
    private val dslCtx: DSLContext
) {
    fun grantAccess(user: User, role: OrganizationRole, org: OrganizationDto) {
        dslCtx.insertInto(
            Tables.USER_ORGANIZATION_MEMBERSHIP,
            USER_ORGANIZATION_MEMBERSHIP.USER_ID,
            USER_ORGANIZATION_MEMBERSHIP.ORGANIZATION_ID,
            USER_ORGANIZATION_MEMBERSHIP.ROLE
        )
            .values(user.id, org.id, role)
            .onDuplicateKeyIgnore()
            .execute()
    }

    fun getMemberships(user: User): List<OrganizationMembershipReducedDto> {
        val memberships = this.dslCtx
            .select(ORGANIZATION.ID.`as`("org_id"), ORGANIZATION.NAME.`as`("org_name"), USER_ORGANIZATION_MEMBERSHIP.ROLE).from(ORGANIZATION)
            .join(USER_ORGANIZATION_MEMBERSHIP)
            .on(USER_ORGANIZATION_MEMBERSHIP.ORGANIZATION_ID.eq(ORGANIZATION.ID))
            .where(USER_ORGANIZATION_MEMBERSHIP.USER_ID.eq(user.id))
            .fetchInto(OrganizationMembershipDto::class.java)

        val reducedMemberships = mutableMapOf<Int, MutableList<OrganizationRole>>()
        val uniqueOrgList = mutableListOf<OrganizationMembershipDto>()

        memberships.forEach {
            if (reducedMemberships.contains(it.orgId)) {
                reducedMemberships[it.orgId]!!.add(it.role)
            } else {
                uniqueOrgList.add(it)
                reducedMemberships[it.orgId] = mutableListOf(it.role)
            }
        }

        return uniqueOrgList.map {
            OrganizationMembershipReducedDto(it.orgId, it.orgName, reducedMemberships[it.orgId]!!.toList())
        }

    }

    fun getOrganization(user: User, orgId: Int, role: OrganizationRole): OrganizationDto {
        return this.dslCtx
            .select().from(ORGANIZATION)
            .join(USER_ORGANIZATION_MEMBERSHIP)
            .on(USER_ORGANIZATION_MEMBERSHIP.ORGANIZATION_ID.eq(ORGANIZATION.ID))
            .where(USER_ORGANIZATION_MEMBERSHIP.USER_ID.eq(user.id)).and(USER_ORGANIZATION_MEMBERSHIP.ROLE.eq(role))
            .and(ORGANIZATION.ID.eq(orgId))
            .fetchOneInto(OrganizationDto::class.java) ?:
            throw NotFoundException("No organization with ID $orgId found for user ID ${user.id}")
    }
}