package com.library.repository;

import com.library.model.Book;
import com.library.IRepository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private Book mockBook;

    @BeforeEach
    public void setup() {
        mockBook = new Book();
        mockBook.setId(1L);
        mockBook.setTitle("Test Book");
        mockBook.setAuthor("Test Author");
    }

    @Test
    public void testFindById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));

        Optional<Book> result = bookRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Book", result.get().getTitle());
        assertEquals("Test Author", result.get().getAuthor());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void testSave() {
        when(bookRepository.save(mockBook)).thenReturn(mockBook);

        Book savedBook = bookRepository.save(mockBook);

        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
        assertEquals("Test Author", savedBook.getAuthor());
        verify(bookRepository, times(1)).save(mockBook);
    }

    @Test
    public void testDelete() {
        doNothing().when(bookRepository).deleteById(1L);

        bookRepository.deleteById(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindByIdNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Book> result = bookRepository.findById(1L);

        assertFalse(result.isPresent());
        verify(bookRepository, times(1)).findById(1L);
    }
}
