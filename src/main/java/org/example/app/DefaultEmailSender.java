package org.example.app;

import org.example.infrastructure.annotation.Component;
import org.example.infrastructure.annotation.Log;

@Component
public class DefaultEmailSender implements EmailSender {

    @Override
    @Log
    public void send(String to, String subject, String body) {
        System.out.printf(
                "Sending email to %s. Subject: %s. Body: %s\n",
                to,
                subject,
                body
        );
    }
}
