package com.example.book_service.dto;

public class BookDTO {
    private Long id;
    private String title;
    private Author author;

    public BookDTO(Long id, String title, Author author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }
    // Getters and setters
}