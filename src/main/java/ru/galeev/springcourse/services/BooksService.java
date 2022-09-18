package ru.galeev.springcourse.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.galeev.springcourse.models.Book;
import ru.galeev.springcourse.repositories.BooksRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class BooksService {
    private final BooksRepository bookRepository;

    public BooksService(BooksRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        log.info("Method findAll is returning all books");
        return bookRepository.findAll();
    }

    public List<Book> findByPersonId(long id) {
        log.info("Method findByPersonId is returning person's with id = {} list of books...", id);
        return bookRepository.findByPersonId(id);
    }

    public List<Book> findBooksByCreatedDateBetween(Date from, Date to, long id) {
        log.info("Method findBooksByCreatedDateBetween is returning books in range from = {}, to = {}, user with id = {} ...", from, to, id);
        return bookRepository.findBooksByCreatedBetween(from, to, id);
    }

    public List<Book> findBooksByCreatedDateBetween(Date from, Date to) {
        log.info("Method findBooksByCreatedDateBetween is returning books in range from = {}, to = {}...", from, to);
        return bookRepository.findBooksByCreatedBetween(from, to);
    }

    public Optional<Book> findById(long id) {
        log.info("Method findById is returning book with id = {}...", id);
        return bookRepository.findById(id);
    }

    @Transactional
    public Book save(Book book) {
        log.info("Method save is keeping book {}...", book);
        return bookRepository.save(book);
    }

    @Transactional
    public void deleteByPersonId(long personId) {
        log.info("Method deleteByPersonId is deleting person's with id = {} all books...", personId);
        bookRepository.deleteByPersonId(personId);
    }

    @Transactional
    public void deleteById(long id) {
        log.info("Method deleteById is deleting book with id = {}...", id);
        bookRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        log.info("Method deleteAll is all books");
        bookRepository.deleteAll();
    }

}
