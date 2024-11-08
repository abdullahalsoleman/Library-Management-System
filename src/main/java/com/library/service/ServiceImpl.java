package com.library.service;

import com.library.IService.BookService;
import com.library.IService.BorrowingService;
import com.library.IService.PatronService;
import com.library.IService.Service;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service {

    private PatronService patronService;
    private BookService bookService;
    private BorrowingService borrowingService;

    @Autowired
    public ServiceImpl(PatronService patronService,
                       BookService bookService,
                       BorrowingService borrowingService) {
        this.patronService = patronService;
        this.bookService = bookService;
        this.borrowingService = borrowingService;
    }

    @Override
    public PatronService getPatronService() {
        return patronService;
    }

    @Override
    public BorrowingService getBorrowingService() {
        return borrowingService;
    }

    @Override
    public BookService getBookService() {
        return bookService;
    }
}
