package ru.galeev.springcourse.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.galeev.springcourse.dto.*;
import ru.galeev.springcourse.models.Person;
import ru.galeev.springcourse.security.jwt.JwtTokenProvider;
import ru.galeev.springcourse.security.jwt.UserNotFoundException;
import ru.galeev.springcourse.services.PeopleService;
import ru.galeev.springcourse.util.PersonNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthenticationRestController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PeopleService peopleService;

    public AuthenticationRestController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PeopleService peopleService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.peopleService = peopleService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody AuthenticationRequestDTO requestDto) {
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            Person person = peopleService.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username: " + username + " not found"));

            String token = jwtTokenProvider.createToken(username, person.getRoles());

            LoginResponseDTO loginRequestDTO = new LoginResponseDTO();
            loginRequestDTO.setUsername(username);
            loginRequestDTO.setToken(token);
            return new ResponseEntity<>(loginRequestDTO, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            LoginResponseDTO loginRequestDTO = new LoginResponseDTO();
            loginRequestDTO.setUsername("Bad credentials");
            return new ResponseEntity<>(loginRequestDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        Person person = registerRequestDTO.toPerson();
        peopleService.register(person);

        String token = jwtTokenProvider.createToken(person.getUsername(), person.getRoles());
        RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO();
        registerResponseDTO.setUsername(person.getUsername());
        registerResponseDTO.setToken(token);
        return new ResponseEntity<>(registerResponseDTO, HttpStatus.OK);
    }


    ///////////////////  Два метода контроллера для rest клиента(получение списка людей и получение человека по id ///////////////////

    @GetMapping(value = "/users")
    public ResponseEntity<List<AdminPersonDTO>> getAllPeople() {
        List<AdminPersonDTO> result = peopleService.findAll().stream().map(AdminPersonDTO::fromPerson).collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<AdminPersonDTO> getUserById(@PathVariable(name = "id") Long id) {
        Person person = peopleService.findOne(id).orElseThrow(() -> new PersonNotFoundException("Person with id: " + id + " wasn't found"));

        if (person == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);


        AdminPersonDTO result = AdminPersonDTO.fromPerson(person);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
