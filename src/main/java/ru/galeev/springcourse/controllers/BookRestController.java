package ru.galeev.springcourse.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.galeev.springcourse.services.BooksService;
import ru.galeev.springcourse.services.PeopleService;

@RestController
@RequestMapping("/api")
@Slf4j
public class BookRestController {
    private final PeopleService peopleService;
    private final BooksService bookService;

    @Autowired
    public BookRestController(PeopleService peopleService, BooksService bookService) {
        this.peopleService = peopleService;
        this.bookService = bookService;
    }

}
