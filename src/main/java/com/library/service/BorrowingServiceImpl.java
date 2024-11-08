package com.library.service;

import com.library.IService.BorrowingService;
import com.library.UnitOfWork.UnitOfWork;
import com.library.model.Book;
import com.library.model.BorrowingRecord;
import com.library.model.Patron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class BorrowingServiceImpl implements BorrowingService {

    private final UnitOfWork unitOfWork;

    @Autowired
    public BorrowingServiceImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @Override
    @Transactional // Transaction management for borrowing operation
    public BorrowingRecord borrowBook(Long bookId, Long patronId) {
        Optional<Book> bookOpt = unitOfWork.getBookRepository().findById(bookId);
        Optional<Patron> patronOpt = unitOfWork.getPatronRepository().findById(patronId);

        if (bookOpt.isEmpty()) {
            throw new IllegalArgumentException("Book does not exist.");
        }

        if (patronOpt.isEmpty()) {
            throw new IllegalArgumentException("Patron does not exist.");
        }

        // Create a new borrowing record
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBookId(bookId);
        borrowingRecord.setPatronId(patronId);
        borrowingRecord.setBorrowDate(new Date(System.currentTimeMillis())); // Current date

        // Save the borrowing record and update book status
        unitOfWork.getBorrowingRecordRepository().save(borrowingRecord);

        return borrowingRecord;
    }

    @Override
    @Transactional // Transaction management for returning operation
    public BorrowingRecord returnBook(Long bookId, Long patronId) {
        Optional<Book> bookOpt = unitOfWork.getBookRepository().findById(bookId);
        Optional<Patron> patronOpt = unitOfWork.getPatronRepository().findById(patronId);

        if (bookOpt.isEmpty() || patronOpt.isEmpty()) {
            throw new IllegalArgumentException("Book or patron does not exist.");
        }

        // Find the borrowing record
        Optional<BorrowingRecord> borrowingRecordOpt = unitOfWork.getBorrowingRecordRepository()
                .findByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId);

        if (borrowingRecordOpt.isEmpty()) {
            throw new IllegalStateException("No active borrowing record found for this book and patron.");
        }

        BorrowingRecord borrowingRecord = borrowingRecordOpt.get();
        borrowingRecord.setReturnDate(new Date(System.currentTimeMillis())); // Set the returned date to today

        // Save the updated borrowing record and book status
        unitOfWork.getBorrowingRecordRepository().save(borrowingRecord);

        return borrowingRecord;
    }
}
