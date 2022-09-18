package ru.galeev.springcourse.util;

import org.modelmapper.ModelMapper;
import ru.galeev.springcourse.dto.BookDTO;
import ru.galeev.springcourse.models.Book;

public class Convert {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static BookDTO convertToBookDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    public static Book convertToBook(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, Book.class);
    }
}
