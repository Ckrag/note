package com.note

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.jooq.Record
import org.jooq.Table
import org.jooq.TableField
import spock.lang.Shared
import spock.lang.Subject

import static jooq.Tables.*

@MicronautTest
class NoteRepositorySpec extends RepoSpecification {

    @Subject
    @Shared
    NoteRepository noteRepository = applicationContext.getBean(NoteRepository)

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
        def user1 = userRepo.getUserByEmail("kek1@m.com")
        def user2 = userRepo.getUserByEmail("kek2@m.com")
        def user3 = userRepo.getUserByEmail("kek3@m.com")

        def org1 = orgRepo.findByName("org 1 and 2")[0]
        def org3 = orgRepo.findByName("org 3")[0]

        // Gets category if user has access
        def cat1 = catRepo.getCategoryById(user1, org1, 1)
        def cat2 = catRepo.getCategoryById(user2, org1, 2)
        def cat3 = catRepo.getCategoryById(user3, org3, 3)

        // Gets all categories a user has access to within an organization
        def user1Cats = catRepo.getAllCategories(user1, org1)
        def user3Cats = catRepo.getAllCategories(user3, org3)

        noteRepository.createNote(new CreateNoteDto("note1", "content", cat1.id), user1, org1)
        noteRepository.createNote(new CreateNoteDto("note21", "content", cat2.id), user2, org1)
        noteRepository.createNote(new CreateNoteDto("note22", "content", cat2.id), user2, org1)
        noteRepository.createNote(new CreateNoteDto("note3", "content", cat3.id), user3, org3)

        def org1NotesUser1 = noteRepository.getAllNotesForCategory(user1, org1, cat1)
        def org3NotesUser1 = noteRepository.getAllNotesForCategory(user1, org3, cat1)
        def org1NotesUser2 = noteRepository.getAllNotesForCategory(user2, org1, cat2)
        def org3NotesUser3 = noteRepository.getAllNotesForCategory(user3, org3, cat3)
        def org1NotesUser3 = noteRepository.getAllNotesForCategory(user1, org3, cat3)

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
        [
                (USERS)                      : [
                        [(USERS.ID): 1, (USERS.EMAIL): 'kek1@m.com'],
                        [(USERS.ID): 2, (USERS.EMAIL): 'kek2@m.com'],
                        [(USERS.ID): 3, (USERS.EMAIL): 'kek3@m.com'],
                ],
                (ORGANIZATION)               : [
                        [(ORGANIZATION.ID): 1, (ORGANIZATION.NAME): "org 1 and 2"],
                        [(ORGANIZATION.ID): 2, (ORGANIZATION.NAME): "org 3"],
                ],
                (NOTE_CATEGORY)              : [
                        [(NOTE_CATEGORY.ID): 1, (NOTE_CATEGORY.TITLE): "my category 1"],
                        [(NOTE_CATEGORY.ID): 2, (NOTE_CATEGORY.TITLE): "my category 2"],
                        [(NOTE_CATEGORY.ID): 3, (NOTE_CATEGORY.TITLE): "my category 3"],
                        [(NOTE_CATEGORY.ID): 4, (NOTE_CATEGORY.TITLE): "my extra category 1"],
                ],
                (USER_NOTECATEGORY_OWNERSHIP): [
                        [
                                (USER_NOTECATEGORY_OWNERSHIP.ID)             : 1,
                                (USER_NOTECATEGORY_OWNERSHIP.CATEGORY_ID)    : 1,
                                (USER_NOTECATEGORY_OWNERSHIP.USER_ID)        : 1,
                                (USER_NOTECATEGORY_OWNERSHIP.ORGANIZATION_ID): 1,
                        ],
                        [
                                (USER_NOTECATEGORY_OWNERSHIP.ID)             : 4,
                                (USER_NOTECATEGORY_OWNERSHIP.CATEGORY_ID)    : 4,
                                (USER_NOTECATEGORY_OWNERSHIP.USER_ID)        : 1,
                                (USER_NOTECATEGORY_OWNERSHIP.ORGANIZATION_ID): 1,
                        ],
                        [
                                (USER_NOTECATEGORY_OWNERSHIP.ID)             : 2,
                                (USER_NOTECATEGORY_OWNERSHIP.CATEGORY_ID)    : 2,
                                (USER_NOTECATEGORY_OWNERSHIP.USER_ID)        : 2,
                                (USER_NOTECATEGORY_OWNERSHIP.ORGANIZATION_ID): 1,
                        ],
                        [
                                (USER_NOTECATEGORY_OWNERSHIP.ID)             : 3,
                                (USER_NOTECATEGORY_OWNERSHIP.CATEGORY_ID)    : 3,
                                (USER_NOTECATEGORY_OWNERSHIP.USER_ID)        : 3,
                                (USER_NOTECATEGORY_OWNERSHIP.ORGANIZATION_ID): 2,
                        ],
                ],
        ] as Map<Table<Record>, List<Map<TableField, String>>>
    }

}