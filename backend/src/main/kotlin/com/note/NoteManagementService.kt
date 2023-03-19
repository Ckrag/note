package com.note

import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton
import jooq.enums.OrganizationRole

@Singleton
class NoteManagementService(
    private val noteRepo: NoteRepository,
    private val userRepo: UserRepository,
    private val orgMembershipRepo: OrganizationMembershipRepository
) {

    fun createNote(note: CreateNoteDto, authentication: Authentication, orgId: Int): NoteDto {
        val user = userRepo.getUserByEmail(authentication.name)
        val org = orgMembershipRepo.getOrganization(user, orgId, OrganizationRole.USER)
        return noteRepo.createNote(note, user, org)
    }

    fun findNotesByTitle(title: String, authentication: Authentication): List<NoteDto> {
        val user = userRepo.getUserByEmail(authentication.name)
        return noteRepo.findByTitleByUser(title, user)
    }

    fun getAllNotes(authentication: Authentication): List<NoteDto> {
        val user = userRepo.getUserByEmail(authentication.name)
        return noteRepo.getAllReadableByUser(user)
    }
}