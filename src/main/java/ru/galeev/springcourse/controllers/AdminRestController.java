package ru.galeev.springcourse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.galeev.springcourse.dto.*;
import ru.galeev.springcourse.models.Person;
import ru.galeev.springcourse.models.Status;
import ru.galeev.springcourse.security.jwt.JwtTokenProvider;
import ru.galeev.springcourse.services.BooksService;
import ru.galeev.springcourse.services.PeopleService;
import ru.galeev.springcourse.util.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/admin")
public class AdminRestController {
    private final PeopleService peopleService;
    private final JwtTokenProvider jwtTokenProvider;
    private final BooksService booksService;

    @Autowired
    public AdminRestController(PeopleService peopleService, JwtTokenProvider jwtTokenProvider, BooksService booksService) {
        this.peopleService = peopleService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.booksService = booksService;
    }

    @PostMapping(value = "/users/reg")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        Person person = registerRequestDTO.toPerson();
        peopleService.register(person);

        String token = jwtTokenProvider.createToken(person.getUsername(), person.getRoles());
        RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO();
        registerResponseDTO.setUsername(person.getUsername());
        registerResponseDTO.setToken(token);

        return new ResponseEntity<>(registerResponseDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<AdminPersonDTO> getUserById(@PathVariable(name = "id") Long id) {
        Person person = peopleService.findOne(id).orElseThrow(() -> new PersonNotFoundException("Person with id: " + id + " wasn't found"));

        if (person == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);


        AdminPersonDTO result = AdminPersonDTO.fromPerson(person);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<AdminPersonDTO>> getAllPeople() {
        List<AdminPersonDTO> result = peopleService.findAll().stream().map(AdminPersonDTO::fromPerson).collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/users/getPersonByBookId/{id}")
    public ResponseEntity getPersonByBookId(@PathVariable("id") long id, @RequestParam(required = false) boolean verbose) {
        if (verbose) {
            PersonWithBooksDTO result = PersonWithBooksDTO.toPersonDTOWithBooks(peopleService.findPersonByBookId(id)
                    .orElseThrow(() -> new BookNotFoundException("Book with id = " + id + " wasn't found!")));

            return new ResponseEntity(result, HttpStatus.OK);
        } else {
            PersonWithoutBooksDTO result = PersonWithoutBooksDTO.toPersonWithoutBooksDTO(peopleService.findPersonByBookId(id)
                    .orElseThrow(() -> new BookNotFoundException("Book with id = " + id + " wasn't found!")));
            return new ResponseEntity(result, HttpStatus.OK);
        }
    }


    @PutMapping()
    public ResponseEntity<HttpStatus> updateUserInfo(@RequestBody RegisterRequestDTO registerRequestDTO) {
        peopleService.register(registerRequestDTO.toPerson());

        return ResponseEntity.ok(HttpStatus.OK);
    }


    @DeleteMapping("/users/delete/{id}")
    public String delete(@PathVariable int id) {
        peopleService.findOne(id).orElseThrow(() -> new PersonNotFoundException("Person with id " + id + " wasn't found"));
        peopleService.delete(id);

        return "Person was removed";
    }

    @PostMapping("books/create/{personId}")
    public ResponseEntity<BookDTO> createBook(@PathVariable(value = "personId") long personId,
                                              @RequestBody BookDTO bookRequest) {
        BookDTO bookDTO = Convert.convertToBookDTO(peopleService.findOne(personId).map(personDTO ->
        {
            bookRequest.setPerson(personDTO);
            bookRequest.setCreated(new Date());
            bookRequest.setUpdated(new Date());
            bookRequest.setStatus(Status.ACTIVE);

            return booksService.save(Convert.convertToBook(bookRequest));
        }).orElseThrow(() -> new BookNotCreatedException("Book wasn't created")));

        return new ResponseEntity<>(bookDTO, HttpStatus.CREATED);
    }

    @GetMapping("/books/personBooks/{personId}")
    public ResponseEntity<List<BookDTO>> getAllBooksByPersonId(@PathVariable(value = "personId") int personId) {
        if (!peopleService.exists(personId)) {
            throw new PersonNotFoundException("Person with id = " + personId + " wasn't found!");
        }

        List<BookDTO> books = booksService.findByPersonId(personId).stream().map(Convert::convertToBookDTO).collect(Collectors.toList());

        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookDTO> getBookByBookId(@PathVariable(value = "id") long id) {
        BookDTO bookDTO = Convert.convertToBookDTO(booksService.findById(id).orElseThrow(() -> new BookNotFoundException("Book with id = " + id + " wasn't found!")));

        return new ResponseEntity<>(bookDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/books")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> result = booksService.findAll().stream().map(Convert::convertToBookDTO).collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/books/books_range")
    public ResponseEntity<List<BookDTO>> getBooksInRange(@RequestParam @DateTimeFormat(pattern = "yyyy") Date from, @RequestParam @DateTimeFormat(pattern = "yyyy") Date to) {
        List<BookDTO> booksDTO = booksService.findBooksByCreatedDateBetween(from, to).stream().map(Convert::convertToBookDTO).collect(Collectors.toList());

        if (booksDTO.isEmpty())
            throw new BookNotFoundException("Books wasn't found");


        return new ResponseEntity<>(booksDTO, HttpStatus.OK);
    }


    @PutMapping("/books/{person_id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable("person_id") long id, @RequestBody BookDTO bookRequest) {
        List<BookDTO> books = booksService.findByPersonId(id).stream().map(Convert::convertToBookDTO).toList();

        Optional<BookDTO> bookDTO = books.stream()
                .filter(b -> b.getId() == bookRequest.getId())
                .findFirst();

        bookDTO.ifPresentOrElse(dto -> {
            dto.setName(bookRequest.getName());
            dto.setBookDate(bookRequest.getBookDate());
            dto.setUpdated(new Date());
            dto.setStatus(Status.ACTIVE);

            booksService.save(Convert.convertToBook(bookDTO.orElseThrow(() -> new BookNotCreatedException("Book wasn't updated"))));
        }, () -> {
            throw new BookNotFoundException("No such book with id = " + bookRequest.getId());
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/books/{id}")
    public String deleteBook(@PathVariable("id") long id) {
        getBookByBookId(id);
        booksService.deleteById(id);

        return "Book was removed";
    }

    @DeleteMapping("/books/removeAllBooks/{person_id}")
    public String removeBooks(@PathVariable("person_id") long id) {
        if (!peopleService.exists(id))
            throw new PersonNotFoundException("Person with id = " + id + " wasn't found!");

        booksService.deleteByPersonId(id);

        return "All books were removed";
    }

    @DeleteMapping("/destroyPeople")
    public String removeAllPeople() {
        peopleService.deleteAll();

        return "There is no one person anymore";
    }

    @DeleteMapping("/destroyBooks")
    public String removeAllBooks() {
        booksService.deleteAll();

        return "There is no one book anymore";
    }
}
