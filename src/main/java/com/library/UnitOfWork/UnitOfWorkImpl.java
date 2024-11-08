package com.library.UnitOfWork;

import com.library.IRepository.BookRepository;
import com.library.IRepository.BorrowingRecordRepository;
import com.library.IRepository.PatronRepository;
import com.library.IRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UnitOfWorkImpl implements UnitOfWork {

    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final UserRepository userRepository;
    // Constructor injection
    @Autowired
    public UnitOfWorkImpl(BookRepository bookRepository,
                          PatronRepository patronRepository,
                          BorrowingRecordRepository borrowingRecordRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.borrowingRecordRepository = borrowingRecordRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BookRepository getBookRepository() {
        return bookRepository;
    }

    @Override
    public PatronRepository getPatronRepository() {
        return patronRepository;
    }

    @Override
    public BorrowingRecordRepository getBorrowingRecordRepository() {
        return borrowingRecordRepository;
    }

    @Override
    public UserRepository getUserRepository() {
        return userRepository;
    }
}
