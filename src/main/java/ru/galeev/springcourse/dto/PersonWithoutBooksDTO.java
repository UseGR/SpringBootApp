package ru.galeev.springcourse.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.galeev.springcourse.models.Person;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class PersonWithoutBooksDTO {
    private long id;

    @NotEmpty(message = "Username should not be empty")
    private String username;

    @NotEmpty(message = "Firstname should not be empty")
    private String firstName;

    @NotEmpty(message = "Lastname should not be empty")
    private String lastName;

    @Min(value = 0, message = "Age should be greater than 0")
    private int age;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;


    public PersonWithoutBooksDTO(long id, String username, String firstName, String lastName, int age, String email) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
    }

    public static PersonWithoutBooksDTO toPersonWithoutBooksDTO(Person person) {
        PersonWithoutBooksDTO personDTO = new PersonWithoutBooksDTO();
        personDTO.setId(person.getId());
        personDTO.setUsername(person.getUsername());
        personDTO.setFirstName(person.getFirstName());
        personDTO.setLastName(person.getLastName());
        personDTO.setEmail(person.getEmail());
        personDTO.setAge(person.getAge());

        return personDTO;

    }

}
