package com.gustavo.mobiauto_backend.model.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
public class User implements UserDetails {
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

    @Column(name = "active")
    private boolean active;

    public User(UserName name, UserEmail email, UserPassword password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.active = true;
    }

    public User(UserName name, UserEmail email, UserPassword password, boolean active) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.active = active;
    }

    public User(String firstName, String lastName, String email, String password) {
        this.name = new UserName(firstName, lastName);
        this.email = new UserEmail(email);
        this.password = new UserPassword(password);
        this.active = true;
    }

    public String getFullName() {
        return name.getFirstName() + " " + name.getLastName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password != null ? this.password.getValue() : null;
    }

    @Override
    public String getUsername() {
        return this.email.getValue();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }
}
