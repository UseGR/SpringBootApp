package ru.galeev.springcourse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.galeev.springcourse.dto.*;
import ru.galeev.springcourse.models.Status;
import ru.galeev.springcourse.security.jwt.JwtUser;
import ru.galeev.springcourse.services.BooksService;
import ru.galeev.springcourse.services.PeopleService;
import ru.galeev.springcourse.util.BookNotCreatedException;
import ru.galeev.springcourse.util.BookNotFoundException;
import ru.galeev.springcourse.util.Convert;
import ru.galeev.springcourse.util.SecurityHelper;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "api/users")
public class UserRestController {

    private final PeopleService peopleService;

    private final BooksService booksService;

    @Autowired
    public UserRestController(PeopleService peopleService, BooksService booksService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

    @GetMapping
    public ResponseEntity showUserInfo(@RequestParam(required = false) boolean verbose) {
        JwtUser user = SecurityHelper.getUser();

        if (verbose) {
            return new ResponseEntity<>(new PersonWithBooksDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getAge(),
                    user.getEmail(),
                    user.getBooks()),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new PersonWithoutBooksDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getAge(),
                    user.getEmail()),
                    HttpStatus.OK);
        }
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateUserInfo(@RequestBody RegisterRequestDTO registerRequestDTO) {
        peopleService.register(registerRequestDTO.toPerson());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping
    public String deleteUser() {
        JwtUser user = SecurityHelper.getUser();

        peopleService.delete(user.getId());

        return "User was removed";
    }

    @GetMapping("/showBooks")
    public ResponseEntity<List<BookDTO>> showAllBooks() {
        JwtUser user = SecurityHelper.getUser();

        List<BookDTO> books = booksService.findByPersonId(user.getId()).stream().map(Convert::convertToBookDTO).collect(Collectors.toList());

        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/showBooks/{id}")
    public ResponseEntity<BookDTO> showBook(@PathVariable(value = "id") long id) {
        JwtUser user = SecurityHelper.getUser();

        List<BookDTO> books = booksService.findByPersonId(user.getId()).stream().map(Convert::convertToBookDTO).toList();

        Optional<BookDTO> bookDTO = books.stream()
                .filter(b -> b.getId() == id)
                .findFirst();

        return bookDTO.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK)).orElseThrow(() -> new BookNotFoundException("Book with id = " + id + " wasn't found"));
    }

    @GetMapping("showBooks/booksRange")
    public ResponseEntity<List<BookDTO>> showBooksInRange(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
        JwtUser user = SecurityHelper.getUser();

        List<BookDTO> books = booksService.findBooksByCreatedDateBetween(from, to, user.getId()).stream()
                .map(Convert::convertToBookDTO).toList();

        if (books.isEmpty()) {
            throw new BookNotFoundException("Books wasn't found");
        }

        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createBook(@RequestBody BookDTO bookRequest) {
        JwtUser user = SecurityHelper.getUser();

        Convert.convertToBookDTO(peopleService.findOne(user.getId()).map(personId -> {
            bookRequest.setPerson(personId);
            bookRequest.setCreated(new Date());
            bookRequest.setUpdated(new Date());
            bookRequest.setStatus(Status.ACTIVE);
            return booksService.save(Convert.convertToBook(bookRequest));
        }).orElseThrow(() -> new BookNotCreatedException("Book wasn't created")));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("updateBook")
    public ResponseEntity<HttpStatus> updateBook(@RequestBody BookDTO bookRequest) {
        JwtUser user = SecurityHelper.getUser();

        List<BookDTO> books = booksService.findByPersonId(user.getId()).stream().map(Convert::convertToBookDTO).toList();

        Optional<BookDTO> bookDTO = books.stream()
                .filter(b -> b.getId() == bookRequest.getId())
                .findFirst();

        bookDTO.ifPresentOrElse(dto -> {
            dto.setName(bookRequest.getName());
            dto.setUpdated(new Date());
            dto.setStatus(Status.ACTIVE);
            booksService.save(Convert.convertToBook(bookDTO.orElseThrow(() -> new BookNotCreatedException("Book wasn't updated"))));
        }, () -> {
            throw new BookNotFoundException("No such book with id = " + bookRequest.getId());
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("deleteBook/{id}")
    public String deleteBook(@PathVariable("id") long id) {
        JwtUser user = SecurityHelper.getUser();

        List<BookDTO> books = booksService.findByPersonId(user.getId()).stream().map(Convert::convertToBookDTO).toList();

        Optional<BookDTO> bookDTO = books.stream()
                .filter(b -> b.getId() == id)
                .findFirst();

        bookDTO.ifPresentOrElse(dto -> booksService.deleteById(dto.getId()), () -> {
            throw new BookNotCreatedException("Book id = " + id + " wasn't removed");
        });

        return "Book was removed";
    }


    @DeleteMapping("deleteBooks")
    public String deleteAllBooks() {
        JwtUser user = SecurityHelper.getUser();

        List<BookDTO> books = booksService.findByPersonId(user.getId()).stream().map(Convert::convertToBookDTO).toList();

        books.forEach(b -> booksService.deleteById(b.getId()));

        return "All books were removed";
    }

}
