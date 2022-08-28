package ru.galeev.springcourse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.galeev.springcourse.models.Person;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    @Query(value = "SELECT * from person LEFT JOIN book on Person.person_id = Book.person_id WHERE Book.book_id = ?", nativeQuery = true)
    Optional<Person> findByBookId(int id);
}
