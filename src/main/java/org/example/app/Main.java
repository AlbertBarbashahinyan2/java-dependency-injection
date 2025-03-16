package org.example.app;

import org.example.infrastructure.Application;
import org.example.infrastructure.ApplicationContext;

public class Main {
    public static void main(String[] args) {

        ApplicationContext context = Application.run("org.example");

        UserRegistrationService registrationService = context.getObject(UserRegistrationService.class);

        registrationService.register( // the getUser used inside this one does not cache yet since it returns null
                new User(
                        "Gurgen",
                        "gurgen@inconceptlabsc.com",
                        "password123"
                )
        );
        UserRepository userRepository = context.getObject(UserInMemoryRepository.class);
        User user1 = userRepository.getUser("Gurgen"); // here Gurgen is cached
        User user2 = userRepository.getUser("Gurgen"); // here the cached User is returned
    }
}