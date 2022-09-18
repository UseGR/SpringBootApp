package ru.galeev.springcourse.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import ru.galeev.springcourse.models.Book;
import ru.galeev.springcourse.models.Person;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class PersonWithBooksDTO {
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

    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Book> books;

    public PersonWithBooksDTO(long id, String username, String firstName, String lastName, int age, String email, List<Book> books) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.books = books;
    }

    public static PersonWithBooksDTO toPersonDTOWithBooks(Person person) {
        PersonWithBooksDTO personDTO = new PersonWithBooksDTO();
        personDTO.setId(person.getId());
        personDTO.setUsername(person.getUsername());
        personDTO.setFirstName(person.getFirstName());
        personDTO.setLastName(person.getLastName());
        personDTO.setEmail(person.getEmail());
        personDTO.setAge(person.getAge());
        personDTO.setBooks(person.getBooks());

        return personDTO;
    }

}
