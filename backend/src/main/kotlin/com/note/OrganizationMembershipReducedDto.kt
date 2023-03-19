package com.note

import io.micronaut.serde.annotation.Serdeable
import jooq.enums.OrganizationRole

@Serdeable
data class OrganizationMembershipReducedDto(
    val orgId: Int,
    val orgName: String,
    val roles: List<OrganizationRole>
)