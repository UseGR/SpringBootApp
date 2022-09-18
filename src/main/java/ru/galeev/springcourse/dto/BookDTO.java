package ru.galeev.springcourse.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import ru.galeev.springcourse.models.Person;
import ru.galeev.springcourse.models.Status;

import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDTO {
    private long id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Person person;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bookDate;

    private Date created;
    private Date updated;
    private Status status;


    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", person='" + person.getFirstName() +
                "', name='" + name + '\'' +
                ", created=" + (bookDate.getYear() + 1900) +
                '}';
    }
}
