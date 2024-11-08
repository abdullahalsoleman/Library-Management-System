package com.library.controller;

import com.library.IService.BookService;
import com.library.IService.Service;
import com.library.dto.AddAndUpdateBookDTO;
import com.library.dto.BookDTO;
import com.library.dto.RequestStatus;
import com.library.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private Service service;

    @Mock
    private BookService bookService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookController bookController;

    private Book book;
    private BookDTO bookDTO;
    private AddAndUpdateBookDTO addAndUpdateBookDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new Book(1L, "Title", "Author", 2020, "9781234567890", "Genre");
        bookDTO = new BookDTO("Title", "Author", 2020, "9781234567890", "Genre");
        addAndUpdateBookDTO = new AddAndUpdateBookDTO("Title", "Author", 2020, "9781234567890", "Genre");

        // Set up the mock service to return bookService
        when(service.getBookService()).thenReturn(bookService);
    }

    @Test
    void testGetAllBooks() {
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book));
        when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDTO);

        ResponseEntity<List<BookDTO>> response = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testGetBookById_BookFound() {
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));
        when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDTO);

        ResponseEntity<?> response = bookController.getBookById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookDTO, response.getBody());
        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void testGetBookById_BookNotFound() {
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookController.getBookById(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new RequestStatus(false, "Book not found or error occurred."), response.getBody());
        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void testAddBook_Success() {
        when(modelMapper.map(addAndUpdateBookDTO, Book.class)).thenReturn(book);

        ResponseEntity<RequestStatus> response = bookController.addBook(addAndUpdateBookDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new RequestStatus(true, "Book added successfully."), response.getBody());
        verify(bookService, times(1)).addBook(book);
    }

    @Test
    void testAddBook_Error() {
        when(modelMapper.map(addAndUpdateBookDTO, Book.class)).thenReturn(book);
        doThrow(new RuntimeException()).when(bookService).addBook(book);

        ResponseEntity<RequestStatus> response = bookController.addBook(addAndUpdateBookDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new RequestStatus(false, "Error adding book."), response.getBody());
        verify(bookService, times(1)).addBook(book);
    }

    @Test
    void testUpdateBook_Success() {
        when(modelMapper.map(addAndUpdateBookDTO, Book.class)).thenReturn(book);

        ResponseEntity<RequestStatus> response = bookController.updateBook(1L, addAndUpdateBookDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new RequestStatus(true, "Book updated successfully."), response.getBody());
        verify(bookService, times(1)).updateBook(1L, book);
    }

    @Test
    void testUpdateBook_Error() {
        when(modelMapper.map(addAndUpdateBookDTO, Book.class)).thenReturn(book);
        doThrow(new RuntimeException()).when(bookService).updateBook(1L, book);

        ResponseEntity<RequestStatus> response = bookController.updateBook(1L, addAndUpdateBookDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new RequestStatus(false, "Error updating book or book not found."), response.getBody());
        verify(bookService, times(1)).updateBook(1L, book);
    }

    @Test
    void testDeleteBook_Success() {
        ResponseEntity<RequestStatus> response = bookController.deleteBook(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new RequestStatus(true, "Book deleted successfully."), response.getBody());
        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    void testDeleteBook_Error() {
        doThrow(new RuntimeException()).when(bookService).deleteBook(1L);

        ResponseEntity<RequestStatus> response = bookController.deleteBook(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new RequestStatus(false, "Error deleting book or book not found."), response.getBody());
        verify(bookService, times(1)).deleteBook(1L);
    }
}
