package ru.galeev.springcourse.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "books")
@Data
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    @JsonIgnore
    private Person person;

    @Column(name = "name")
    private String name;

    @Column(name = "book_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bookDate;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", person=" + person.getFirstName() +
                ", name='" + name + '\'' +
                ", created=" + (bookDate.getYear() + 1900) +
                '}';
    }
}
