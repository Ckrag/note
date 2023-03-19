package com.note

import io.micronaut.serde.annotation.Serdeable
import jooq.enums.OrganizationRole

@Serdeable
data class OrganizationMembershipDto(
    val orgId: Int,
    val orgName: String,
    val role: OrganizationRole,
)