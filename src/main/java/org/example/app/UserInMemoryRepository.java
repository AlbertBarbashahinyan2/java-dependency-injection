package org.example.app;

import org.example.infrastructure.annotation.*;
import org.example.infrastructure.enums.ScopeType;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(ScopeType.SINGLETON)
@Log
public class UserInMemoryRepository implements UserRepository {

    private List<User> users = new ArrayList<>();

    public UserInMemoryRepository() {
        System.out.println("UserInMemoryRepository constructor call");
    }

    @PostConstruct
    public void secondPhaseConstructor(){

        System.out.println("UserInMemoryRepository SecondPhaseConstructor call");
    }

    @Override
    public void save(User user) {
        users.add(user);
    }

    @Override
    @Cacheable
    public User getUser(@CacheKey String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users);
    }
}
