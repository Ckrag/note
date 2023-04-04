package com.note

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import java.util.*
import javax.validation.Valid

@Validated
@Singleton
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/org/{orgId}/category/")
class CategoryController(
    private val categoryService: CategoryService
) {

    @Produces(MediaType.APPLICATION_JSON)
    @Get(value = "/all")
    fun getCategories(authentication: Authentication, orgId: Int): HttpResponse<UserCategoriesResponse> {
        return HttpResponse.ok(UserCategoriesResponse(this.categoryService.getUserCategories(authentication, orgId)))
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Post(value = "/create")
    fun createCategory(@Body @Valid createCategoryDto: CreateCategoryDto, authentication: Authentication, orgId: Int): HttpResponse<Category> {
        val saved = this.categoryService.createCategory(authentication, orgId, createCategoryDto)
        return HttpResponse.created(saved)
    }

}