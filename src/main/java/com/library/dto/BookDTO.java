package com.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDTO {

    private Long id;

    private String title;

    private String author;

    private int publicationYear;

    private String isbn;

    private String genre;

    public BookDTO(String title, String author, int publicationYear, String isbn, String genre) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.genre = genre;
    }
}
