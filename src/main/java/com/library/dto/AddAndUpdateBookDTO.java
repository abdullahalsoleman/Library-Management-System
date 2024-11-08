package com.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddAndUpdateBookDTO {
    @NotBlank(message = "title is required")
    @Size(max = 50, message = "title can have a maximum of 50 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 50, message = "Author name can have a maximum of 50 characters")
    private String author;

    @Positive(message = "Publication year must be a positive number")
    private int publicationYear;

    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$", message = "ISBN must be a valid format")
    private String isbn;

    @NotBlank(message = "Genre is required")
    @Size(max = 30, message = "Genre can have a maximum of 30 characters")
    private String genre;

    public AddAndUpdateBookDTO(String title, String author, int publicationYear, String isbn, String genre) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.genre = genre;
    }
}
