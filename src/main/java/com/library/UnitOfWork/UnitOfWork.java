package com.library.UnitOfWork;

import com.library.IRepository.BookRepository;
import com.library.IRepository.BorrowingRecordRepository;
import com.library.IRepository.PatronRepository;
import com.library.IRepository.UserRepository;

public interface UnitOfWork {

    BookRepository getBookRepository();

    PatronRepository getPatronRepository();

    BorrowingRecordRepository getBorrowingRecordRepository();

    UserRepository getUserRepository();
}
