package com.example.book_service.controller;


import com.example.book_service.domain.Book;
import com.example.book_service.dto.Author;
import com.example.book_service.dto.BookDTO;
import com.example.book_service.exception.AuthorNotFoundException;
import com.example.book_service.exception.BookNotFoundException;
import com.example.book_service.feign_client.AuthorClient;
import com.example.book_service.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorClient authorClient;

    @GetMapping("/books")
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        // Convert list of Book entities to list of BookDTOs
        return books.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/books/{id}")
    public BookDTO getBookById(@PathVariable Long id) {
        Book book = bookRepository.findById(id);
        if (book == null) {
            throw new BookNotFoundException("Book not found with ID: " + id);
        }
        return convertToDTO(book);
    }

    private BookDTO convertToDTO(Book book) {
        Author author = authorClient.getAuthorById(book.getAuthorId());
        if (author == null) {
            throw new AuthorNotFoundException("Author not found with ID: " + book.getAuthorId());
        }
        return new BookDTO(book.getId(), book.getTitle(), author);
    }

}
