package com.gustavo.mobiauto_backend.model.user;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Embedded
    private UserName name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", unique = true))
    private UserEmail email;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "password"))
    private UserPassword password;

    @Column(name = "enabled")
    private boolean enabled;

    public User(UserName name, UserEmail email, UserPassword password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.enabled = true;
    }

    public User(UserName name, UserEmail email, UserPassword password, boolean enabled) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }

    public User(String firstName, String lastName, String email, String password) {
        this.name = new UserName(firstName, lastName);
        this.email = new UserEmail(email);
        this.password = new UserPassword(password);
        this.enabled = true;
    }

    public String getFullName() {
        return name.getFirstName() + " " + name.getLastName();
    }
}
