package ru.galeev.springcourse.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.galeev.springcourse.models.Person;
import ru.galeev.springcourse.repositories.PeopleRepository;
import ru.galeev.springcourse.security.jwt.JwtAuthenticationException;
import ru.galeev.springcourse.security.jwt.JwtUser;
import ru.galeev.springcourse.security.jwt.JwtUserFactory;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final PeopleRepository peopleRepository;


    @Autowired
    public JwtUserDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = peopleRepository.findByUsername(username).orElseThrow(() -> new JwtAuthenticationException("User with username: " + username + " not found"));

        JwtUser jwtUser = JwtUserFactory.create(person);
        log.info("IN loadUserByUsername - user with username: {} successfully loaded", username);

        return jwtUser;
    }
}
