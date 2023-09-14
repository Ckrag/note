package com.note

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jooq.enums.OrganizationRole
import org.jooq.Record
import org.jooq.Table
import org.jooq.TableField
import spock.lang.Shared
import spock.lang.Subject

@MicronautTest
class UserCaseRepositorySpec extends RepoSpecification {

    @Subject
    @Shared
    NoteRepository noteRepo = applicationContext.getBean(NoteRepository)

    @Subject
    @Shared
    OrganizationMembershipRepository orgMembershipRepo = applicationContext.getBean(OrganizationMembershipRepository)

    @Subject
    @Shared
    UserRepository userRepo = applicationContext.getBean(UserRepository)

    @Subject
    @Shared
    CategoryRepository catRepo = applicationContext.getBean(CategoryRepository)

    @Subject
    @Shared
    OrganizationRepository orgRepo = applicationContext.getBean(OrganizationRepository)


    void "onlygettingownnotes"() {
        when:
        userRepo.createUser(new CreateUserDto("kek1@m.com", "1234"))
        def user1 = userRepo.getUserByEmail("kek1@m.com")
        userRepo.createUser(new CreateUserDto("kek2@m.com", "1234"))
        def user2 = userRepo.getUserByEmail("kek2@m.com")
        userRepo.createUser(new CreateUserDto("kek3@m.com", "1234"))
        def user3 = userRepo.getUserByEmail("kek3@m.com")

        orgRepo.create("org 1 and 2")
        def org1 = orgRepo.findByName("org 1 and 2")[0]
        orgRepo.create("org 3")
        def org3 = orgRepo.findByName("org 3")[0]

        orgMembershipRepo.grantAccess(user1, OrganizationRole.ADMIN, org1)
        orgMembershipRepo.grantAccess(user2, OrganizationRole.ADMIN, org1)
        orgMembershipRepo.grantAccess(user2, OrganizationRole.ADMIN, org3)

        // Gets category if user has access
        def cat1Id = catRepo.createCategory(new CreateCategoryDto("my category 1", ""), user1, org1)
        def cat1 = catRepo.getCategoryById(user1, org1, cat1Id.id)
        def cat2Id = catRepo.createCategory(new CreateCategoryDto("my category 2", ""), user2, org1)
        def cat2 = catRepo.getCategoryById(user2, org1, cat2Id.id)
        def cat3Id = catRepo.createCategory(new CreateCategoryDto("my category 3", ""), user3, org3)
        def cat3 = catRepo.getCategoryById(user3, org3, cat3Id.id)
        catRepo.createCategory(new CreateCategoryDto("my extra category 1", ""), user1, org1)

        // Gets all categories a user has access to within an organization
        def user1Cats = catRepo.getAllCategories(user1, org1)
        def user3Cats = catRepo.getAllCategories(user3, org3)

        noteRepo.createNote(new CreateNoteDto("note1", "content", cat1.id), user1, org1)
        noteRepo.createNote(new CreateNoteDto("note21", "content", cat2.id), user2, org1)
        noteRepo.createNote(new CreateNoteDto("note22", "content", cat2.id), user2, org1)
        noteRepo.createNote(new CreateNoteDto("note3", "content", cat3.id), user3, org3)

        def org1NotesUser1 = noteRepo.getAllNotesForCategory(user1, org1, cat1)
        def org3NotesUser1 = noteRepo.getAllNotesForCategory(user1, org3, cat1)
        def org1NotesUser2 = noteRepo.getAllNotesForCategory(user2, org1, cat2)
        def org3NotesUser3 = noteRepo.getAllNotesForCategory(user3, org3, cat3)
        def org1NotesUser3 = noteRepo.getAllNotesForCategory(user1, org3, cat3)

        then:
        org1NotesUser1.size() == 1
        org1NotesUser1[0].title == "note1"

        org1NotesUser2.size() == 2
        org1NotesUser2[0].title == "note21"
        org1NotesUser2[1].title == "note22"

        org3NotesUser3.size() == 1
        org3NotesUser3[0].title == "note3"

        org3NotesUser1.size() == 0
        org1NotesUser3.size() == 0

        user1Cats.size() == 2
        user3Cats.size() == 1
    }


    @Override
    Map<Table<Record>, List<Map<TableField, String>>> getData() {
        Map.of() as Map<Table<Record>, List<Map<TableField, String>>>
    }

}