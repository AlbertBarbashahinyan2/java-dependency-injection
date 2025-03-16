package org.example.app;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.infrastructure.annotation.Scope;
import org.example.infrastructure.enums.ScopeType;

@Setter
@Getter
@AllArgsConstructor
@ToString
@Scope(ScopeType.PROTOTYPE)
public class User {

    private String username;
    private String email;
    private String password;
}
