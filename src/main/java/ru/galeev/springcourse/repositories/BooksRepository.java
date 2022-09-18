package ru.galeev.springcourse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.galeev.springcourse.models.Book;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Long> {
    List<Book> findByPersonId(long personId);

    @Transactional
    void deleteByPersonId(long personId);

    @Query(value = "SELECT * FROM books where book_date between ? and ? and person_id = ?", nativeQuery = true)
    List<Book> findBooksByCreatedBetween(Date from, Date to, long id);

    @Query(value = "SELECT * FROM books where book_date between ? and ?", nativeQuery = true)
    List<Book> findBooksByCreatedBetween(Date from, Date to);

}
