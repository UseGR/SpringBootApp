package ru.galeev.springcourse.security.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.galeev.springcourse.models.Person;
import ru.galeev.springcourse.models.Role;
import ru.galeev.springcourse.models.Status;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(Person person) {
        return new JwtUser(
                person.getId(),
                person.getUsername(),
                person.getFirstName(),
                person.getLastName(),
                person.getAge(),
                person.getPassword(),
                person.getBooks(),
                person.getEmail(),
                person.getStatus().equals(Status.ACTIVE),
                person.getUpdated(),
                mapToGrantedAuthorities(new ArrayList<>(person.getRoles()))
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> personRoles) {
        return personRoles.stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getName())
                ).collect(Collectors.toList());
    }
}
