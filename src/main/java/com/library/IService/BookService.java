package com.library.IService;

import com.library.dto.RequestStatus;
import com.library.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getAllBooks();
    Optional<Book> getBookById(Long id);
    RequestStatus addBook(Book book);
    RequestStatus updateBook(Long id, Book book);
    RequestStatus deleteBook(Long id);
}
