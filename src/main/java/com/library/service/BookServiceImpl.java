package com.library.service;

import com.library.IService.BookService;
import com.library.UnitOfWork.UnitOfWork;
import com.library.dto.RequestStatus;
import com.library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final UnitOfWork unitOfWork;

    @Autowired
    public BookServiceImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return unitOfWork.getBookRepository().findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> getBookById(Long id) {
        return unitOfWork.getBookRepository().findById(id);
    }

    @Override
    @Transactional
    public RequestStatus addBook(Book book) {
        unitOfWork.getBookRepository().save(book);
        return new RequestStatus(true, "Book added successfully.");
    }

    @Override
    @Transactional
    public RequestStatus updateBook(Long id, Book book) {
        Optional<Book> existingBook = unitOfWork.getBookRepository().findById(id);
        if (existingBook.isPresent()) {
            book.setId(id);
            unitOfWork.getBookRepository().save(book);
            return new RequestStatus(true, "Book updated successfully.");
        } else {
            return new RequestStatus(false, "Book does not exist.");
        }
    }

    @Override
    @Transactional
    public RequestStatus deleteBook(Long id) {
        if (unitOfWork.getBookRepository().existsById(id)) {
            unitOfWork.getBookRepository().deleteById(id);
            return new RequestStatus(true, "Book deleted successfully.");
        } else {
            return new RequestStatus(false, "Book does not exist.");
        }
    }
}
