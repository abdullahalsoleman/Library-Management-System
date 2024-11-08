package com.library.service;

import com.library.IService.*;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service {

    private PatronService patronService;
    private BookService bookService;
    private BorrowingService borrowingService;
    private AuthenticationService authenticationService;
    private JwtService jwtService;

    @Autowired
    public ServiceImpl(PatronService patronService,
                       BookService bookService,
                       BorrowingService borrowingService,
                       AuthenticationService authenticationService,
                       JwtService jwtService) {
        this.patronService = patronService;
        this.bookService = bookService;
        this.borrowingService = borrowingService;
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
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

    @Override
    public JwtService getJwtService() {
        return jwtService;
    }

    @Override
    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }
}
