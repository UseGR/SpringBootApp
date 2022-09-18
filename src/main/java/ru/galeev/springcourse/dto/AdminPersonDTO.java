package ru.galeev.springcourse.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.galeev.springcourse.models.Book;
import ru.galeev.springcourse.models.Person;
import ru.galeev.springcourse.models.Status;

import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminPersonDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String status;
    private List<Book> books;


    public static AdminPersonDTO fromPerson(Person person) {
        AdminPersonDTO adminPersonDTO = new AdminPersonDTO();
        adminPersonDTO.setId(person.getId());
        adminPersonDTO.setUsername(person.getUsername());
        adminPersonDTO.setFirstName(person.getFirstName());
        adminPersonDTO.setLastName(person.getLastName());
        adminPersonDTO.setAge(person.getAge());
        adminPersonDTO.setEmail(person.getEmail());
        adminPersonDTO.setBooks(person.getBooks());
        adminPersonDTO.setStatus(person.getStatus().name());
        return adminPersonDTO;
    }
}
