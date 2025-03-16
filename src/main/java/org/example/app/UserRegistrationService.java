package org.example.app;


import org.example.infrastructure.annotation.*;

@Component
@Log
public class UserRegistrationService {

    @Inject
    @Qualifier(UserInMemoryRepository.class)
    private UserRepository userRepository;

    @Inject
    @Qualifier(DefaultEmailSender.class)
    private EmailSender emailSender;

    @PostConstruct
    public void secondPhaseConstructor(){
        System.out.println("UserRegistrationService SecondPhaseConstructor call");
    }

    public void register(User user) {
        User existingUser = userRepository.getUser(user.getUsername());
        if (existingUser != null) {
            throw new UserAlreadyExistsException(
                    "User is already registered. Username: " + user.getUsername()
            );
        }

        userRepository.save(user);

        emailSender.send(
                user.getEmail(),
                "Account confirmation",
                "Please confirm your newly created account"
        );
    }
}
