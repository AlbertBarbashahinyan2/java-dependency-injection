package org.example.app;

import org.example.infrastructure.annotation.*;
import org.example.infrastructure.enums.ScopeType;

import java.util.Collections;
import java.util.List;

@Component
@Scope(ScopeType.SINGLETON)
public class UserDatabaseRepository implements UserRepository {
    @Env("database_url")
    private String databaseUrl;

    @Env
    private String databaseUsername;

    @Property("datasource.username")
    private String username;

    @Property
    private String datasourcePassword;

    public UserDatabaseRepository() {
        System.out.println("UserDatabaseRepository constructor call");
    }

    @PostConstruct
    public void SecondPhaseConstructor() {
        System.out.println("UserDatabaseRepository SecondPhaseConstructor call");
    }

    @Override
    @Log
    public void save(User user) {

    }

    @Override
    public User getUser(String username) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return Collections.emptyList();
    }
}
