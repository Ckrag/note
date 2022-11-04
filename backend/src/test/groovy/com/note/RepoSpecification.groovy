package com.note

import io.micronaut.context.ApplicationContext
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.flywaydb.core.Flyway
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Table
import org.jooq.TableField
import org.testcontainers.containers.JdbcDatabaseContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

abstract class RepoSpecification extends Specification {

    @Shared
    JdbcDatabaseContainer postgresContainer

    @AutoCleanup
    @Shared
    ApplicationContext applicationContext = ApplicationContext.run(getDbConfig())

    @Subject
    @Shared
    DSLContext dslCtx = applicationContext.getBean(DSLContext)

    Map<String, String> getDbConfig() {
        if (postgresContainer == null) {
            postgresContainer = new PostgreSQLContainer("postgres:bullseye")
                    .withDatabaseName("lol")
                    .withUsername("lol2")
                    .withPassword("lol3")
            postgresContainer.start()

            Flyway flyway = Flyway.configure()
                    .dataSource(
                            postgresContainer.getJdbcUrl(),
                            postgresContainer.getUsername(),
                            postgresContainer.getPassword()
                    )
                    .locations("filesystem:./flyway/sql")
                    .load()
            flyway.migrate()
        }

        return [
                'datasources.default.url': postgresContainer.getJdbcUrl(),
                'datasources.default.password': postgresContainer.getPassword(),
                'datasources.default.username': postgresContainer.getUsername(),
        ]
    }

    abstract Map<Table<Record>, List<Map<TableField, String>>> getData()

    def setupSpec() {
        print("INNER CALLED")
        getData().each {table, inserts ->
            inserts.each { insert ->
                dslCtx.insertInto(table)
                        .columns(insert.keySet())
                        .values(insert.values()).execute()
            }
        }
    }

}
