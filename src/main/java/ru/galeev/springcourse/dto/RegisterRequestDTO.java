package ru.galeev.springcourse.dto;

import lombok.Data;
import ru.galeev.springcourse.models.Person;

@Data
public class RegisterRequestDTO {
    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String password;

    public Person toPerson() {
        Person person = new Person();
        person.setId(id);
        person.setUsername(username);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setAge(age);
        person.setEmail(email);
        person.setPassword(password);

        return person;
    }
}
