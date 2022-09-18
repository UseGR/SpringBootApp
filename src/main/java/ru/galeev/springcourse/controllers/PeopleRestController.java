package ru.galeev.springcourse.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.galeev.springcourse.services.PeopleService;


@RestController
@RequestMapping("/api/people")
@Slf4j
public class PeopleRestController {
    private final PeopleService peopleService;

    @Autowired
    public PeopleRestController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

}
