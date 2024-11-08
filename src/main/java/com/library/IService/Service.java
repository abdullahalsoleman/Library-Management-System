package com.library.IService;

public interface Service {
    PatronService getPatronService();
    BorrowingService getBorrowingService();
    BookService getBookService();
    JwtService getJwtService();
    AuthenticationService getAuthenticationService();
}
