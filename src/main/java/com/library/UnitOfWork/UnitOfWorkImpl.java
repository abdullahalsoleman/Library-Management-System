package com.library.UnitOfWork;

import com.library.IRepository.BookRepository;
import com.library.IRepository.BorrowingRecordRepository;
import com.library.IRepository.PatronRepository;
import com.library.UnitOfWork.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UnitOfWorkImpl implements UnitOfWork {

    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;

    // Constructor injection
    @Autowired
    public UnitOfWorkImpl(BookRepository bookRepository,
                          PatronRepository patronRepository,
                          BorrowingRecordRepository borrowingRecordRepository) {
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.borrowingRecordRepository = borrowingRecordRepository;
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
}
