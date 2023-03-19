package com.note


import jooq.Tables
import org.jooq.Record
import org.jooq.Table
import org.jooq.TableField
import spock.lang.Shared
import spock.lang.Subject

class NoteRepositorySpec extends RepoSpecification {

    @Subject
    @Shared
    NoteRepository noteRepository = applicationContext.getBean(NoteRepository)

    void "testFindByTitle"() {
        when:
        def matching = noteRepository.findByTitleByUser("my-note")
        then:
        matching.size() == 1

        when:
        def notMatching = noteRepository.findByTitleByUser("fubar")
        then:
        notMatching.empty
    }

    @Override
    Map<Table<Record>, List<Map<TableField, String>>> getData() {
        [
                (Tables.USERS): [
                        [(Tables.USERS.ID): 1, (Tables.USERS.USERNAME): 'bob', (Tables.USERS.EMAIL): 'kek@m.com'],
                ],
                (Tables.NOTE) : [
                        [(Tables.NOTE.TITLE): 'my-note', (Tables.NOTE.CONTENT): 'my content for my note', (Tables.NOTE.AUTHOR_ID): 1]
                ]
        ] as Map<Table<Record>, List<Map<TableField, String>>>
    }

}