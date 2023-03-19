package com.note

import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class OrganizationService(
    private val orgMemRepo: OrganizationMembershipRepository,
    private val userRepo: UserRepository,
) {

    fun getUserOrganizations(authentication: Authentication) : UserOrganizationsResponse {
        val user = userRepo.getUserByEmail(authentication.name)
        return UserOrganizationsResponse(this.orgMemRepo.getMemberships(user))
    }

}