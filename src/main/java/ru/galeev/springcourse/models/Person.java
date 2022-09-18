package ru.galeev.springcourse.models;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "people")
@Data
public class Person extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long id;

    @NotEmpty(message = "Username should not be empty")
    @Column(name = "username")
    private String username;

    @NotEmpty(message = "Firstname should not be empty")
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty(message = "Lastname should not be empty")
    @Column(name = "last_name")
    private String lastName;

    @Min(value = 0, message = "Age should be greater than 0")
    @Column(name = "age")
    private int age;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "Password should not be empty")
    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "person_roles",
            joinColumns = {@JoinColumn(name = "person_id", referencedColumnName = "person_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "role_id")})
    private List<Role> roles;

    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    //@LazyCollection(LazyCollectionOption.FALSE)
    private List<Book> books;

}
