package com.note

import jakarta.inject.Singleton

@Singleton
class UserService(
    private val userRepo: UserRepository,
    private val passwordRepo: PasswordRepository,
    private val passwordHasher: PasswordHasher,
) {

    fun createUser(userDto: CreateUserDto): User {
        try {
            userRepo.getUserByEmail(userDto.email)
        } catch (e: NotFoundException) {
            val user = userRepo.createUser(userDto)
            passwordRepo.saveUserPassword(user, passwordHasher.encode(userDto.rawPassword))
            return user
        }
        throw AlreadyExistsException("${userDto.email} is already taken")
    }

}