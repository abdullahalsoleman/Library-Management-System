package com.library.controller;

import com.library.IService.Service;
import com.library.dto.AddAndUpdateBookDTO;
import com.library.dto.BookDTO;
import com.library.dto.RequestStatus;
import com.library.model.Book;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@Validated
public class BookController {

    // Injected service and model mapper dependencies
    @Autowired
    private Service service;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Endpoint to retrieve a list of all books.
     */
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        // Retrieve all books from the service
        List<Book> books = service.getBookService().getAllBooks();
        // Convert List<Book> to List<BookDTO> using ModelMapper
        List<BookDTO> bookDTOs = books.stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
        // Return the list of BookDTOs with HTTP status 200 OK
        return ResponseEntity.ok(bookDTOs);
    }

    /**
     * Endpoint to retrieve a single book by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        try {
            // Retrieve the book by ID from the service
            Optional<Book> book = service.getBookService().getBookById(id);
            // If found, map the Book entity to BookDTO and return it
            BookDTO bookDTO = modelMapper.map(book.get(), BookDTO.class);
            return ResponseEntity.ok(bookDTO);
        } catch (Exception e) {
            // If an error occurs (example: book not found), return a bad request response with an error message
            return ResponseEntity.badRequest().body(new RequestStatus(false, "Book not found or error occurred."));
        }
    }

    /**
     * Endpoint to add a new book to the system.
     */
    @PostMapping
    public ResponseEntity<RequestStatus> addBook(@Valid @RequestBody AddAndUpdateBookDTO newBook) {
        try {
            // Convert the AddAndUpdateBookDTO to a Book entity
            Book book = modelMapper.map(newBook, Book.class);
            // Pass the Book entity to the service to add it
            service.getBookService().addBook(book);
            // Return a success message with HTTP status 200 OK
            return ResponseEntity.ok(new RequestStatus(true, "Book added successfully."));
        } catch (Exception e) {
            // If an error occurs during adding the book, return a bad request response with an error message
            return ResponseEntity.badRequest().body(new RequestStatus(false, "Error adding book."));
        }
    }

    /**
     * Endpoint to update an existing book.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RequestStatus> updateBook(@PathVariable Long id, @Valid @RequestBody AddAndUpdateBookDTO updatedBook) {
        try {
            // Convert the AddAndUpdateBookDTO to a Book entity
            Book book = modelMapper.map(updatedBook, Book.class);
            // Pass the Book entity to the service to update the book by its ID
            service.getBookService().updateBook(id, book);
            // Return a success message with HTTP status 200 OK
            return ResponseEntity.ok(new RequestStatus(true, "Book updated successfully."));
        } catch (Exception e) {
            // If an error occurs during updating the book (e.g., book not found), return a bad request response with an error message
            return ResponseEntity.badRequest().body(new RequestStatus(false, "Error updating book or book not found."));
        }
    }

    /**
     * Endpoint to delete a book from the system by its ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<RequestStatus> deleteBook(@PathVariable Long id) {
        try {
            // Call the service to delete the book by its ID
            service.getBookService().deleteBook(id);
            // Return a success message with HTTP status 200 OK
            return ResponseEntity.ok(new RequestStatus(true, "Book deleted successfully."));
        } catch (Exception e) {
            // If an error occurs during deletion (e.g., book not found), return a bad request response with an error message
            return ResponseEntity.badRequest().body(new RequestStatus(false, "Error deleting book or book not found."));
        }
    }
}
