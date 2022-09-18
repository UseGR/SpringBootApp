package ru.galeev.springcourse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.galeev.springcourse.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Long> {
    @Query(value = "SELECT * FROM people LEFT JOIN books on People.person_id = Books.person_id WHERE Books.book_id = ?", nativeQuery = true)
    Optional<Person> findByBookId(long id);

    Optional<Person> findByUsername(String username);

    @Query(value = "SELECT person FROM Person person JOIN FETCH person.books books")
    List<Person> findAll();

    @Query(value = "SELECT person FROM Person person JOIN FETCH person.books books where person.id = :id")
    Optional<Person> findById(long id);
}
