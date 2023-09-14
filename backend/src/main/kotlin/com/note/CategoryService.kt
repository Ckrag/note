package com.note

import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton
import jooq.enums.OrganizationRole

@Singleton
class CategoryService(
    private val orgMemRepo: OrganizationMembershipRepository,
    private val userRepo: UserRepository,
    private val categoryRepo: CategoryRepository
) {

    fun getUserCategories(authentication: Authentication, orgId: Int) : List<Category> {
        println("getting categories for org $orgId")
        val user = userRepo.getUserByEmail(authentication.name)
        val org = orgMemRepo.getOrganization(user, orgId, OrganizationRole.USER)
        return categoryRepo.getAllCategories(user, org)
    }

    fun createCategory(authentication: Authentication, orgId: Int, createCategory: CreateCategoryDto): Category {
        val user = userRepo.getUserByEmail(authentication.name)
        val org = orgMemRepo.getOrganization(user, orgId, OrganizationRole.USER)
        return categoryRepo.createCategory(createCategory, user, org)
    }

}