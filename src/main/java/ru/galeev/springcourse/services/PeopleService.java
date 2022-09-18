package ru.galeev.springcourse.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.galeev.springcourse.models.Person;
import ru.galeev.springcourse.models.Role;
import ru.galeev.springcourse.models.Status;
import ru.galeev.springcourse.repositories.PeopleRepository;
import ru.galeev.springcourse.repositories.RoleRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Person> findAll() {
        log.info("Method findAll is returning all people...");
        return peopleRepository.findAll();
    }

    public Optional<Person> findOne(long id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        log.info("Method findOne is returning person with id = {}...", id);
        return foundPerson;
    }

    public Optional<Person> findPersonByBookId(long id) {
        Optional<Person> foundPerson = peopleRepository.findByBookId(id);
        log.info("Method findPersonByBookId is returning person with book id = {}", id);
        return foundPerson;
    }


    @Transactional
    public void delete(long id) {
        peopleRepository.deleteById(id);
        log.info("Method delete is removing person with id = {}...", id);
    }
    @Transactional
    public void deleteAll() {
        peopleRepository.deleteAll();
        log.info("Method deleteAll is removing people...");
    }

    public boolean exists(long id) {
        log.info("Method exists is checking person with id = {}...", id);
        return peopleRepository.existsById(id);
    }

    @Transactional
    public Person register(Person user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);
        user.setCreated(new Date());
        user.setUpdated(new Date());

        Person registeredUser = peopleRepository.save(user);

        log.info("IN register - user: {} successfully registered", registeredUser);

        return registeredUser;
    }

    public Optional<Person> findByUsername(String username) {
        Optional<Person> result = peopleRepository.findByUsername(username);
        log.info("IN findByUsername - user: {} found by username: {}", result, username);
        return result;
    }


}
