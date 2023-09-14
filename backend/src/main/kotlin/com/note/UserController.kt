package com.note

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import jooq.enums.OrganizationRole
import java.util.*
import javax.validation.Valid

@Validated
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/user")
class UserController(
    private val userService: UserService,
    private val organizationMembershipRepository: OrganizationMembershipRepository,
    private val organizationRepository: OrganizationRepository,
    private val organizationService: OrganizationService
) {

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Post(value = "/create")
    fun createUserWithOrg(@Body @Valid userDto: CreateUserDto): HttpResponse<UserDto> {
        return try {
            println("CALLING ORG REPO CREATE")
            val org = organizationRepository.create(userDto.email)
            val user = userService.createUser(userDto)
            organizationMembershipRepository.grantAccess(user, OrganizationRole.USER, org)
            organizationMembershipRepository.grantAccess(user, OrganizationRole.ADMIN, org)
            println("created user ${user.email} under org ${org.id}")
            HttpResponse.created(UserDto(user.id, user.email))
        } catch (e: AlreadyExistsException) {
            HttpResponse.status(HttpStatus.CONFLICT)
        }
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Get(value = "/organizations")
    fun getUserOrganizations(authentication: Authentication): HttpResponse<UserOrganizationsResponse> {
        val orgs = organizationService.getUserOrganizations(authentication)
        return HttpResponse.ok(orgs)
    }
}