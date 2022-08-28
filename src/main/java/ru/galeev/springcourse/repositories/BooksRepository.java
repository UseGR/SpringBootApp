package ru.galeev.springcourse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.galeev.springcourse.models.Book;
import ru.galeev.springcourse.models.Person;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByPersonId(int personId);

    @Transactional
    void deleteByPersonId(int personId);

    List<Book> findBooksByCreatedBetween(Calendar from, Calendar to);


}
