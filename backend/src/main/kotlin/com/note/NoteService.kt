package com.note

import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton
import jooq.enums.OrganizationRole

@Singleton
class NoteService(
    private val noteRepo: NoteRepository,
    private val userRepo: UserRepository,
    private val orgMembershipRepo: OrganizationMembershipRepository,
    private val categoryRepository: CategoryRepository
) {

    fun createNote(note: CreateNoteDto, authentication: Authentication, orgId: Int): NoteDto {
        val user = userRepo.getUserByEmail(authentication.name)
        val org = orgMembershipRepo.getOrganization(user, orgId, OrganizationRole.USER)
        return noteRepo.createNote(note, user, org)
    }

    fun findNotesByCategory(authentication: Authentication, orgId: Int, categoryId: Int): List<NoteDto> {
        println("finding notes by category, org: $orgId, category id: $categoryId")
        val user = userRepo.getUserByEmail(authentication.name)
        val org = orgMembershipRepo.getOrganization(user, orgId, OrganizationRole.USER)
        val category = categoryRepository.getCategoryById(user, org, categoryId)
        return noteRepo.getAllNotesForCategory(user, org, category)
    }
}