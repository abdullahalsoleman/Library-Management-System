package com.library.service;

import com.library.IRepository.BookRepository;
import com.library.UnitOfWork.UnitOfWork;
import com.library.dto.RequestStatus;
import com.library.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private UnitOfWork unitOfWork;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddBook_Success() {
        Book book = new Book();
        book.setTitle("Test Book");

        // Mock the repository save method to do nothing
        when(unitOfWork.getBookRepository()).thenReturn(bookRepository);
        doNothing().when(bookRepository).save(any(Book.class));

        RequestStatus response = bookService.addBook(book);
        assertTrue(response.isSuccess());
        assertEquals("Book added successfully.", response.getMessage());
    }

    @Test
    void testUpdateBook_BookExists() {
        Long bookId = 1L;
        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setTitle("Existing Book");

        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Book");

        // Mock the repository behavior
        when(unitOfWork.getBookRepository()).thenReturn(bookRepository);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));

        RequestStatus response = bookService.updateBook(bookId, updatedBook);
        assertTrue(response.isSuccess());
        assertEquals("Book updated successfully.", response.getMessage());
    }

    @Test
    void testUpdateBook_BookNotFound() {
        Long bookId = 1L;
        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Book");

        // Mock repository behavior
        when(unitOfWork.getBookRepository()).thenReturn(bookRepository);
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        RequestStatus response = bookService.updateBook(bookId, updatedBook);
        assertFalse(response.isSuccess());
        assertEquals("Book does not exist.", response.getMessage());
    }

    @Test
    void testDeleteBook_Success() {
        Long bookId = 1L;

        // Mock repository behavior
        when(unitOfWork.getBookRepository()).thenReturn(bookRepository);
        when(bookRepository.existsById(bookId)).thenReturn(true);

        RequestStatus response = bookService.deleteBook(bookId);
        assertTrue(response.isSuccess());
        assertEquals("Book deleted successfully.", response.getMessage());
    }

    @Test
    void testDeleteBook_BookNotFound() {
        Long bookId = 1L;

        // Mock repository behavior
        when(unitOfWork.getBookRepository()).thenReturn(bookRepository);
        when(bookRepository.existsById(bookId)).thenReturn(false);

        RequestStatus response = bookService.deleteBook(bookId);
        assertFalse(response.isSuccess());
        assertEquals("Book does not exist.", response.getMessage());
    }
}
