package org.example.app;


import org.example.infrastructure.annotation.*;
import org.example.infrastructure.enums.ScopeType;

@Component
@Scope(ScopeType.SINGLETON)
public class UserService {

    @Inject
    @Qualifier(UserInMemoryRepository.class)
    private UserRepository userRepository;

    @PostConstruct
    public void secondPhaseConstructor(){ // this won't execute since UserService is not used at all
        System.out.println("UserService SecondPhaseConstructor call");
    }
}
