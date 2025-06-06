package com.gustavo.mobiauto_backend.model.user;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class User {
    private @Id int id;
    private UserName name;
    private UserEmail email;
    private UserPassword password;
}
