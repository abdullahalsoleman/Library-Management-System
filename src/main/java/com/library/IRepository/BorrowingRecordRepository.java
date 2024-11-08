package com.library.IRepository;

import com.library.model.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {

    // Method to find all borrowing records by bookId
    List<BorrowingRecord> findByBookId(Long bookId);

    // Method to find a borrowing record by bookId, patronId, and returnedDate being null
    Optional<BorrowingRecord> findByBookIdAndPatronIdAndReturnDateIsNull(Long bookId, Long patronId);
}
