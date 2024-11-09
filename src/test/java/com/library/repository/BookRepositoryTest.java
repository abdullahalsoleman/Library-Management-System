package com.library.repository;

import com.library.IRepository.BookRepository;
import com.library.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookRepositoryTest {

    @Mock
    private BookRepository bookRepository;

    @Test
    public void testSaveBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setPublicationYear(2018);
        book.setIsbn("9780134685991");
        book.setGenre("Programming");

        when(bookRepository.save(book)).thenReturn(book);

        Book savedBook = bookRepository.save(book);

        assertEquals("Effective Java", savedBook.getTitle());
        verify(bookRepository, times(1)).save(book);
    }
}
