package com.note

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class UserOrganizationsResponse(
    var organizations: List<OrganizationMembershipReducedDto>
)
