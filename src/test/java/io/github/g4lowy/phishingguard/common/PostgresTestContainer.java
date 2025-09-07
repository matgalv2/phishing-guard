package io.github.g4lowy.phishingguard.common;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

    private static final String IMAGE = "postgres:16-alpine";
    private static PostgresTestContainer container;

    private PostgresTestContainer() {
        super(IMAGE);
        withDatabaseName("testdb");
        withUsername("test");
        withPassword("test");
    }

    public static synchronized PostgresTestContainer getInstance() {
        if (container == null) {
            container = new PostgresTestContainer();
        }
        return container;
    }

    @Override
    public void stop() {

    }
}
