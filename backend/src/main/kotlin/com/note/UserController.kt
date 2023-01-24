package com.note

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import io.netty.handler.codec.http.HttpResponseStatus
import java.util.*
import javax.validation.Valid

@Validated
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/user")
class UserController(
    private val userRepository: UserRepository,
    private val passWordRepository: PasswordRepository,
    private val passwordHasher: PasswordHasher
) {

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Post(value = "/create")
    fun createUser(@Body @Valid userDto: CreateUserDto): HttpResponse<UserDto> {
        try {
            userRepository.getUserByUsername(userDto.username)
        } catch (e: NotFoundException) {
            val user = userRepository.createUser(userDto)
            passWordRepository.saveUserPassword(user, passwordHasher.encode(userDto.rawPassword))
            return HttpResponse.created(UserDto(user.id, user.username, user.email))
        }
        return HttpResponse.status(HttpStatus.CONFLICT)
    }
}