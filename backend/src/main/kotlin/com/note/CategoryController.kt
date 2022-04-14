package com.note

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import javax.validation.Valid

/*
@Validated
@Controller("/category")
class CategoryController {

    @Inject
    lateinit var categoryRepo: CategoryRepository

    @Get(value = "/all")
    fun getCategories(): HttpResponse<List<Category>> {
        return HttpResponse.ok(this.categoryRepo.findAll().toList())
    }
    @Post
    fun createCategory(@Body @Valid category: Category): HttpResponse<Category> {
        val saved = this.categoryRepo.save(category)
        return HttpResponse.created(saved)
    }
}
 */